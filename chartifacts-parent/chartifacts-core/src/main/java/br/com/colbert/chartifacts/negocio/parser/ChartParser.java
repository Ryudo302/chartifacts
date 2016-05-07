package br.com.colbert.chartifacts.negocio.parser;

import java.util.*;
import java.util.regex.*;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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

	@Inject
	private transient Logger logger;
	@Inject
	private transient ChartBuilder chartBuilder;

	@Inject
	private IntervaloDeDatasStringParser intervaloDeDatasStringParser;
	@Inject
	private CancaoStringParser cancaoStringParser;

	private Pattern numeroParadaPattern;
	private Pattern posicaoPattern;
	private CalculadoraPontos calculadoraPontos;

	@Override
	public Chart parse(List<String> lines, int quantidadePosicoesParada) throws ParserException {
		logger.info("Analisando {} linhas. Total de posições da parada: {}", Objects.requireNonNull(lines, "lines").size(), quantidadePosicoesParada);
		return chartBuilder.numero(parseNumeroParada(lines.get(0))).comPeriodo(intervaloDeDatasStringParser.parse(lines.get(0))).anterior(null)
				.proximo(null).comCancoes(parseCancoes(lines)).build();
	}

	private int parseNumeroParada(String primeiraLinha) throws ParserException {
		int numero;

		Matcher matcher = numeroParadaPattern.matcher(primeiraLinha);
		if (matcher.matches()) {
			numero = Integer.valueOf(matcher.group(1));
		} else {
			throw new ParserException(
					"Não foi possível identificar o número da parada.\nLinha: " + primeiraLinha + "\nPattern: " + numeroParadaPattern);
		}

		return numero;
	}

	private List<CancaoChart> parseCancoes(List<String> lines) throws ParserException {
		List<CancaoChart> cancoes = new ArrayList<>();

		lines.subList(0, lines.indexOf(identificarLinhaFinalParada(lines))).forEach(line -> {
			Matcher matcher = posicaoPattern.matcher(line);
			if (matcher.matches()) {
				PosicaoChart posicao = PosicaoChart.valueOf(Integer.valueOf(matcher.group(1)));
				cancoes.add(parseVariacaoPosicao(line, CancaoChartBuilder.novo(posicao, cancaoStringParser.parse(line)))
						.comEstatisticas(parseEstatisticas(line, calculadoraPontos.calcularPontos(posicao))).build());
			}
		});

		if (cancoes.isEmpty()) {
			throw new ParserException("Não foi possível identificar canções.\nLinhas: " + lines + "\nPattern: " + posicaoPattern);
		}

		return cancoes;
	}

	private String identificarLinhaFinalParada(List<String> lines) {
		return lines.stream().filter(line -> line.contains("Na Fila")).findFirst().get();
	}

	private CancaoChartBuilder parseVariacaoPosicao(String line, CancaoChartBuilder builder) {
		TipoVariacaoPosicao tipoVariacaoPosicao;
		int valorVariacaoPosicao = 0;
		
		Matcher variacaoPosicaoMatcher = matcherVariacaoPosicao(line);
		if (variacaoPosicaoMatcher.matches()) {
			String group = variacaoPosicaoMatcher.group(1);
			String tipoVariacaoPosicaoString = group.replaceAll("\\d", StringUtils.EMPTY);

			switch (tipoVariacaoPosicaoString) {
			case "+":
				tipoVariacaoPosicao = TipoVariacaoPosicao.SUBIDA;
				valorVariacaoPosicao = Integer.parseInt(String.valueOf(group.charAt(1)));
				break;
			case "-":
				tipoVariacaoPosicao = TipoVariacaoPosicao.QUEDA;
				valorVariacaoPosicao = Integer.parseInt(String.valueOf(group.charAt(1)));
				break;
			case "=":
				tipoVariacaoPosicao = TipoVariacaoPosicao.PERMANENCIA;
				break;
			case " NEW ":
				tipoVariacaoPosicao = TipoVariacaoPosicao.ESTREIA;
				break;
			case " RET ":
				tipoVariacaoPosicao = TipoVariacaoPosicao.RETORNO;
				break;
			default:
				throw new IllegalArgumentException("Tipo de variação de posição desconhecido: " + tipoVariacaoPosicaoString);
			}
		} else {
			throw new IllegalArgumentException("Não foi possível identificar variação de posição: " + line);
		}

		return builder.comVariacao(tipoVariacaoPosicao, valorVariacaoPosicao);
	}

	private Matcher matcherVariacaoPosicao(String line) {
		String ou = "|";
		String valorVariacaoPosicao = "\\d{1,2}";
		String qualquerCoisa = ".+";
		
		Matcher variacaoPosicaoMatcher = Pattern.compile(
				qualquerCoisa
				+ '\"'
				+ StringUtils.SPACE
				+ "\\["
				+ '('
					+ '('
						+ '('
							+ '\\' + "+"
							+ ou
							+ '\\' + "-"
						+ ')'
						+ valorVariacaoPosicao
					+ ')'
					+ ou
					+ "="
					+ ou
					+ " NEW "
					+ ou
					+ " RET "
				+ ')'
				+ "\\]"
				+ StringUtils.SPACE
				+ qualquerCoisa).matcher(line);
		return variacaoPosicaoMatcher;
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
		String permanenciaPeak = "\\(" + "(\\d+)" + 'x' + "\\)";
		
		return Pattern.compile(
				qualquerCoisa
				+ "\\[" + "(\\d+)ª Semana" + "\\]"
				+ StringUtils.SPACE
				+ "\\[" + peak + '(' + StringUtils.SPACE + permanenciaPeak + ')' + '?' + "\\]"
				+ qualquerCoisa
				).matcher(line);
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
