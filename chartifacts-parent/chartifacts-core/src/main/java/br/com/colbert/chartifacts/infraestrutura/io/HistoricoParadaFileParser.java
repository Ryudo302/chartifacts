package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.negocio.parser.*;

/**
 * <em>Parser</em> que permite a obtenção de instâncias de {@link HistoricoParada} a partir de um arquivo externo.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
@ApplicationScoped
public class HistoricoParadaFileParser implements Serializable, HistoricoParadaParser<File> {

	private static final long serialVersionUID = 3515381357514035259L;

	@Inject
	private transient Logger logger;

	@Inject
	private transient CancaoStringParser cancaoStringParser;
	@Inject
	private transient ChartRunStringParser chartRunStringParser;
	@Inject
	private transient IntervaloDeDatasStringParser intervaloDeDatasStringParser;

	/**
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o arquivo informado não exista, não seja um arquivo real ou não possa ser lido
	 * @throws ParserException
	 *             caso ocorra algum erro ao ler o arquivo
	 * @throws ChartRunInvalidoException
	 *             caso algum <em>chart-run</em> identificado possua posições inválidas
	 */
	@Override
	public HistoricoParada parse(File arquivo, int quantidadePosicoesParada) throws ParserException {
		validar(Objects.requireNonNull(arquivo, "arquivo"));
		logger.info("Analisando arquivo de histórico de uma parada com um total de {} posições: {}", quantidadePosicoesParada, arquivo);

		Validate.isTrue(quantidadePosicoesParada > 0, "A quantidade de posições deve ser maior que zero");
		CalculadoraPontos calculadoraPontos = new CalculadoraPontos(quantidadePosicoesParada);

		Collection<ItemHistoricoParada> itens = new ArrayList<>();

		try (Stream<String> linhas = Files.lines(Paths.get(arquivo.toURI()))) {
			ItemHistoricoParadaBuilder itemBuilder = ItemHistoricoParadaBuilder.novo(calculadoraPontos);

			linhas.filter(linha -> StringUtils.isNotBlank(linha)).forEach(linha -> {
				logger.trace("Linha atual: {}", linha);

				if (isLinhaArtistaCancaoPeriodo(linha)) {
					logger.trace("Identificando artistas, canção e período");
					itemBuilder.comCancao(cancaoStringParser.parse(linha)).comPeriodo(intervaloDeDatasStringParser.parse(linha));
				} else if (isLinhaChartRun(linha)) {
					logger.trace("Identificando chart-run");
					itemBuilder.comChartRun(chartRunStringParser.parse(linha, quantidadePosicoesParada));
				}

				if (itemBuilder.isReadyToBuild()) {
					itens.add(itemBuilder.build());
					itemBuilder.clear();
				}
			});
		} catch (IOException | UncheckedIOException exception) {
			throw new ParserException("Erro ao ler arquivo: " + arquivo, exception);
		}

		if (itens.isEmpty()) {
			throw new ParserException("Nenhum histórico identificado no arquivo: " + arquivo);
		}

		logger.debug("Criando histórico com {} ítens", itens.size());
		return new HistoricoParada(itens);
	}

	private boolean isLinhaArtistaCancaoPeriodo(String linha) {
		return cancaoStringParser.getParserConfig().separadorArtistaCancaoPattern().matcher(linha).find();
	}

	private boolean isLinhaChartRun(String linha) {
		return !isLinhaArtistaCancaoPeriodo(linha);
	}

	private void validar(File arquivo) {
		if (!arquivo.exists()) {
			throw new IllegalArgumentException("O arquivo informado não existe: " + arquivo);
		} else if (!arquivo.isFile()) {
			throw new IllegalArgumentException("Não é um arquivo: " + arquivo);
		} else if (!arquivo.canRead()) {
			throw new IllegalArgumentException("Não é possível ler o arquivo: " + arquivo);
		}
	}
}
