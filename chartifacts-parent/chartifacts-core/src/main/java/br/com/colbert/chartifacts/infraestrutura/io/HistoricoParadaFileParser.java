package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;

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
	private Logger logger;

	@Inject
	private CancaoStringParser cancaoStringParser;
	@Inject
	private ChartRunStringParser chartRunStringParser;

	/**
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o arquivo informado não exista, não seja um arquivo real ou não possa ser lido
	 * @throws ParserException
	 *             caso ocorra algum erro ao ler o arquivo
	 */
	@Override
	public HistoricoParada parse(File arquivo, int quantidadePosicoesParada) throws ParserException {
		validar(Objects.requireNonNull(arquivo, "arquivo"));
		logger.info("Arquivo: {}", arquivo);

		Validate.isTrue(quantidadePosicoesParada > 0, "A quantidade de posições deve ser maior que zero");
		CalculadoraPontos calculadoraPontos = new CalculadoraPontos(ElementoChartRun.valueOf(quantidadePosicoesParada));

		Collection<ItemHistoricoParada> itens = new ArrayList<>();

		try (Stream<String> linhas = Files.lines(Paths.get(arquivo.toURI()))) {
			ItemHistoricoParadaBuilder itemBuilder = ItemHistoricoParadaBuilder.novo(calculadoraPontos);

			linhas.filter(linha -> StringUtils.isNotBlank(linha)).forEach(
					linha -> {
						logger.trace("Linha atual: {}", linha);

						if (isLinhaArtistaCancaoPeriodo(linha)) {
							logger.trace("Identificando artistas, canção e período");
							itemBuilder.comCancao(cancaoStringParser.parse(linha)).comPeriodo(
									IntervaloDeDatas.novo().de(LocalDate.now()).ate(LocalDate.now()));
						} else if (isLinhaChartRun(linha)) {
							logger.trace("Identificando chart-run");
							itemBuilder.comChartRun(chartRunStringParser.parse(linha));
						}

						if (itemBuilder.isReadyToBuild()) {
							itens.add(itemBuilder.build());
							itemBuilder.clear();
						}
					});
		} catch (IOException exception) {
			throw new ParserException("Erro ao ler arquivo: " + arquivo, exception);
		}

		logger.debug("Criando histórico com {} ítens", itens.size());
		return new HistoricoParada(itens);
	}

	private boolean isLinhaArtistaCancaoPeriodo(String linha) {
		// TODO
		return linha.contains("\"");
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