package br.com.colbert.chartifacts.negocio.parser;

import java.util.*;
import java.util.regex.*;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.infraestrutura.io.*;
import br.com.colbert.chartifacts.negocio.chartrun.*;

/**
 * 
 * @author ThiagoColbert
 * @since 1 de mai de 2016
 */
public class ChartParser implements Parser<List<String>, Chart> {

	private static int contadorParadas = 0;

	@Inject
	private transient Logger logger;
	@Inject
	private transient ChartBuilder chartBuilder;

	@Inject
	private IntervaloDeDatasStringParser intervaloDeDatasStringParser;
	@Inject
	private CancaoStringParser cancaoStringParser;
	@Inject
	private VariacaoPosicaoStringParser variacaoPosicaoStringParser;

	private Pattern numeroParadaPattern;
	private Pattern posicaoPattern;
	private CalculadoraPontos calculadoraPontos;

	@Override
	public Chart parse(List<String> lines, int quantidadePosicoesParada) throws ParserException {
		logger.debug("Analisando {} linhas. Total de posições da parada: {}", Objects.requireNonNull(lines, "lines").size(),
				quantidadePosicoesParada);

		int numeroParada;

		try {
			logger.debug("Identificando número da parada pelo conteúdo do arquivo");
			numeroParada = parseNumeroParada(lines.get(0));
		} catch (ParserException exception) {
			logger.debug("Número da parada não identificado no arquivo", exception);
			logger.debug("Identificando número da parada pelo gerenciador");
			numeroParada = ++contadorParadas;
		}

		return chartBuilder.numero(numeroParada).comPeriodo(intervaloDeDatasStringParser.parse(lines)).anterior(null).proximo(null)
				.comCancoes(parseCancoes(lines)).build();
	}

	private int parseNumeroParada(String primeiraLinha) throws ParserException {
		int numero;

		Matcher matcher = numeroParadaPattern.matcher(primeiraLinha);
		if (matcher.find()) {
			numero = Integer.valueOf(matcher.group(1));
		} else {
			throw new ParserException(
					"Não foi possível identificar o número da parada.\nLinha: " + primeiraLinha + "\nPattern: " + numeroParadaPattern);
		}

		return numero;
	}

	private List<CancaoChart> parseCancoes(List<String> lines) throws ParserException {
		List<CancaoChart> cancoes = new ArrayList<>();

		lines.subList(0, lines.indexOf(identificarLinhaFinalParada(lines))).stream().map(line -> Jsoup.parse(line).text()).forEach(line -> {
			Matcher matcher = posicaoPattern.matcher(line);
			if (matcher.matches()) {
				PosicaoChart posicao = PosicaoChart.valueOf(Integer.valueOf(matcher.group(1)));
				VariacaoPosicao variacaoPosicao = variacaoPosicaoStringParser.parseVariacaoPosicao(line, posicao);
				
				cancoes.add(CancaoChartBuilder.novo(posicao, cancaoStringParser.parse(line))
						.comVariacao(variacaoPosicao.getTipoVariacao(), variacaoPosicao.getValorVariacao())
						.comEstatisticas(parseEstatisticas(line, calculadoraPontos.calcularPontos(posicao))).build());
			}
		});

		if (cancoes.isEmpty()) {
			throw new ParserException("Não foi possível identificar canções.\nLinhas: " + lines + "\nPattern: " + posicaoPattern);
		}

		return cancoes;
	}

	private String identificarLinhaFinalParada(List<String> lines) {
		return lines.stream().filter(line -> line.contains("Na Fila") || line.contains("[/blue]") || line.contains("NA FILA")).findFirst().get();
	}

	private Estatisticas parseEstatisticas(String line, double pontuacao) {
		Matcher matcher = matcherPermanenciaAndPeak(line);

		if (matcher.matches()) {
			return new Estatisticas(pontuacao, Integer.valueOf(matcher.group(1)), new PermanenciaPosicao(PosicaoChart.valueOf(matcher.group(2)),
					Integer.valueOf(StringUtils.defaultIfBlank(matcher.group(4), String.valueOf(1)))));
		} else {
			throw new IllegalArgumentException("Não foi possível identificar estatísticas: " + line);
		}
	}

	private Matcher matcherPermanenciaAndPeak(String line) {
		String qualquerCoisa = ".*";

		String peak = "PP:" + "(\\d+)";
		String permanenciaPeak = "\\(" + "(\\d+)" + "\\w" + "\\)";

		return Pattern.compile(qualquerCoisa + "\\[" + "(\\d+). \\w+" + "\\]" + StringUtils.SPACE + "\\[" + peak + '(' + StringUtils.SPACE
				+ permanenciaPeak + ')' + '?' + "\\]" + qualquerCoisa).matcher(line);
	}

	/**
	 * 
	 * @param pattern
	 * @throws NullPointerException
	 *             caso seja informado {@code null}
	 */
	public void setNumeroParadaPattern(Pattern pattern) {
		this.numeroParadaPattern = Objects.requireNonNull(pattern, "pattern");
	}

	/**
	 * 
	 * @param pattern
	 * @throws NullPointerException
	 *             caso seja informado {@code null}
	 */
	public void setPosicaoPattern(Pattern pattern) {
		this.posicaoPattern = Objects.requireNonNull(pattern, "pattern");
	}

	/**
	 * 
	 * @param calculadoraPontos
	 * @throws NullPointerException
	 *             caso seja informado {@code null}
	 */
	public void setCalculadoraPontos(CalculadoraPontos calculadoraPontos) {
		this.calculadoraPontos = Objects.requireNonNull(calculadoraPontos, "calculadoraPontos");
	}
}
