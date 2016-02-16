package br.com.colbert.chartifacts.dominio.relatorios;

import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoLocal.TOP;
import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoOcorrencia.ESTREIA;
import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoOcorrencia.*;
import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoVariacao.MAIOR;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.dominio.chartrun.analyze.*;
import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.dominio.relatorios.export.ToFormatedStringConverter;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;
import br.com.colbert.chartifacts.dominio.relatorios.generator.impl.*;
import br.com.colbert.chartifacts.infraestrutura.export.RelatorioTextExporter;
import br.com.colbert.chartifacts.infraestrutura.formatter.CancaoFormatter;

/**
 * Provê uma interface única e facilitada de interação com todas as funcionalidades de relatórios.
 *
 * @author Thiago Colbert
 * @since 19/03/2015
 * @see <a href="http://en.wikipedia.org/wiki/Facade_pattern">Facade Pattern</a>
 */
@ApplicationScoped
public class RelatoriosFacade implements Serializable {

	private static final long serialVersionUID = -7162749343483414207L;

	@Inject
	private transient Logger logger;

	@Inject
	private transient RelatorioGeneratorFactory relatorioGeneratorFactory;
	@Inject
	private transient CancaoFormatter cancaoFormatter;
	@Inject
	private transient RelatorioTextExporter relatorioTextExporter;

	private RelatoriosConfiguration relatoriosConfig;

	/**
	 * 
	 * @param historicoParada
	 * @return
	 * @throws NullPointerException
	 *             caso qualquer parâmetro seja <code>null</code>
	 */
	public String exportarAllTimeChartEmTxt(HistoricoParada historicoParada, RelatoriosConfiguration relatoriosConfig) {
		setRelatoriosConfiguration(relatoriosConfig);
		AllTimeChartCancao allTimeChart = getRelatorioGenerator(RelatorioGeneratorConfig.cancao().com(MAIOR));

		StringBuilder allTimeChartBuilder = new StringBuilder();
		allTimeChartBuilder.append(criarRelatorioTextBuilder(allTimeChart));
		Optional<Relatorio<ItemHistoricoParada, Integer>> relatorioAllTimeChart = allTimeChart.gerar(historicoParada);
		relatorioAllTimeChart.ifPresent(relatorio -> allTimeChartBuilder.append(relatorioTextExporter.export(relatorio, (itemHistorico) -> {
			Estatisticas estatisticas = itemHistorico.getEstatisticas();
			return new StringBuilder().append(cancaoFormatter.format(itemHistorico.getCancao())).append("\t\t\t").append(estatisticas.getPontuacao())
					.append("\t").append(estatisticas.getMelhorPosicao()).append("\t").append(estatisticas.getPermanenciaTotal()).toString();
		} , null)));

		return allTimeChartBuilder.toString();
	}

	/**
	 * 
	 * @param historicoParada
	 * @return
	 * @throws NullPointerException
	 *             caso qualquer parâmetro seja <code>null</code>
	 */
	public String exportarTodosRelatoriosEmTxt(HistoricoParada historicoParada, RelatoriosConfiguration relatoriosConfig) {
		setRelatoriosConfiguration(relatoriosConfig);

		StringBuilder relatoriosTextBuilder = new StringBuilder();
		relatoriosTextBuilder.append(exportarArtistasComMaisEstreias(historicoParada));
		separador(relatoriosTextBuilder);
		relatoriosTextBuilder.append(exportarArtistasComMaisTempoEmTop(historicoParada));
		separador(relatoriosTextBuilder);
		relatoriosTextBuilder.append(exportarArtistasComMaisTop(historicoParada));
		separador(relatoriosTextBuilder);
		relatoriosTextBuilder.append(exportarCancoesComMaiorPermanencia(historicoParada));
		separador(relatoriosTextBuilder);
		Stream.of(TipoVariacaoPosicao.QUEDA, TipoVariacaoPosicao.SUBIDA).forEach(tipoVariacao -> {
			relatoriosTextBuilder.append(exportarCancoesComMaiorVariacao(historicoParada, tipoVariacao));
			separador(relatoriosTextBuilder);
		});
		Stream.of(TipoVariacaoPosicao.ESTREIA, TipoVariacaoPosicao.RETORNO).forEach(tipoVariacao -> {
			relatoriosTextBuilder.append(exportarCancoesComMaiorEntrada(historicoParada, tipoVariacao));
			separador(relatoriosTextBuilder);
		});
		relatoriosTextBuilder.append(exportarCancoesComMaiorSaida(historicoParada));
		separador(relatoriosTextBuilder);
		relatoriosTextBuilder.append(exportarCancoesComMaisTempoEmTop(historicoParada));
		return relatoriosTextBuilder.toString();
	}

	private void setRelatoriosConfiguration(RelatoriosConfiguration relatoriosConfig) {
		this.relatoriosConfig = Objects.requireNonNull(relatoriosConfig, "relatoriosConfig");
		relatorioTextExporter.setLarguraPrimeiraColuna(relatoriosConfig.larguraPrimeiraColuna());
		Optional<Integer> limiteTamanho = relatoriosConfig.limiteTamanho();
		limiteTamanho.ifPresent(limite -> relatorioGeneratorFactory.setLimiteTamanhoRelatorios(limite));
	}

	private StringBuilder exportarArtistasComMaisEstreias(HistoricoParada historicoParada) {
		ArtistasComMaisEstreias artistasComMaisEstreias = getRelatorioGenerator(RelatorioGeneratorConfig.artista().com(MAIOR).ocorrencia(ESTREIA));

		StringBuilder builder = criarRelatorioTextBuilder(artistasComMaisEstreias);
		Optional<Relatorio<Artista, Integer>> relatorioArtistasComMaisEstreias = artistasComMaisEstreias.gerar(historicoParada);
		relatorioArtistasComMaisEstreias.ifPresent(relatorio -> builder.append(relatorioTextExporter.export(relatorio, null, null)));
		return builder;
	}

	private String exportarArtistasComMaisTempoEmTop(HistoricoParada historicoParada) {
		ArtistasComMaisTempoEmTop artistasComMaisTempoEmTop = getRelatorioGenerator(
				RelatorioGeneratorConfig.artista().com(MAIOR).ocorrencia(TEMPO).em(TOP));

		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = relatoriosConfig.posicoesRelatorio(artistasComMaisTempoEmTop);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(artistasComMaisTempoEmTop, top));
			artistasComMaisTempoEmTop.setPosicao(top);
			artistasComMaisTempoEmTop.gerar(historicoParada)
					.ifPresent(relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, null, null)));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	private String exportarArtistasComMaisTop(HistoricoParada historicoParada) {
		ArtistasComMaisTop artistasComMaisTop = getRelatorioGenerator(RelatorioGeneratorConfig.artista().com(MAIOR).em(TOP));

		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = relatoriosConfig.posicoesRelatorio(artistasComMaisTop);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(artistasComMaisTop, top));
			artistasComMaisTop.setPosicao(top);
			artistasComMaisTop.gerar(historicoParada)
					.ifPresent(relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, null, null)));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	private String exportarCancoesComMaiorPermanencia(HistoricoParada historicoParada) {
		StringBuilder builder = new StringBuilder();

		CancoesComMaiorPermanencia cancoesComMaiorPermanencia = getRelatorioGenerator(
				RelatorioGeneratorConfig.cancao().com(MAIOR).ocorrencia(TEMPO).em(TipoLocal.POSICAO));

		Collection<ElementoChartRun> tops = relatoriosConfig.posicoesRelatorio(cancoesComMaiorPermanencia);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(cancoesComMaiorPermanencia, top));
			cancoesComMaiorPermanencia.setPosicao(top);
			cancoesComMaiorPermanencia.gerar(historicoParada).ifPresent(relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual,
					cancao -> cancaoFormatter.format(cancao), permanencia -> permanencia.getQuantidade() + "x")));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	private String exportarCancoesComMaiorVariacao(HistoricoParada historicoParada, TipoVariacaoPosicao tipoVariacao) {
		CancoesComMaiorVariacao cancoesComMaiorVariacao = getRelatorioGenerator(
				RelatorioGeneratorConfig.cancao().com(MAIOR).ocorrencia(TipoOcorrencia.valueOf(tipoVariacao)));

		StringBuilder builder = new StringBuilder();
		cancoesComMaiorVariacao.setTipoVariacao(tipoVariacao);

		builder.append(criarRelatorioTextBuilder(cancoesComMaiorVariacao, tipoVariacao));
		cancoesComMaiorVariacao.gerar(historicoParada)
				.ifPresent(relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao),
						variacao -> '[' + variacao.getElementoA().toString() + '-' + variacao.getElementoB() + ']' + StringUtils.SPACE + '['
								+ signedNumber(variacao) + ']')));

		return builder.toString();
	}

	private String exportarCancoesComMaiorEntrada(HistoricoParada historicoParada, TipoVariacaoPosicao tipoVariacao) {
		CancoesComMaiorVariacao cancoesComMaiorVariacao = getRelatorioGenerator(
				RelatorioGeneratorConfig.cancao().com(MAIOR).ocorrencia(TipoOcorrencia.valueOf(tipoVariacao)));

		StringBuilder builder = new StringBuilder();
		cancoesComMaiorVariacao.setTipoVariacao(tipoVariacao);

		builder.append(criarRelatorioTextBuilder(cancoesComMaiorVariacao, tipoVariacao));
		cancoesComMaiorVariacao.gerar(historicoParada).ifPresent(relatorioAtual -> builder
				.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao), posicaoUnicaStringConverter())));

		return builder.toString();
	}
	
	private String exportarCancoesComMaiorSaida(HistoricoParada historicoParada) {
		CancoesComMaiorVariacao cancoesComMaiorVariacao = getRelatorioGenerator(
				RelatorioGeneratorConfig.cancao().com(MAIOR).ocorrencia(SAIDA));

		StringBuilder builder = new StringBuilder();
		cancoesComMaiorVariacao.setTipoVariacao(SAIDA.getTipoVariacaoPosicao());

		builder.append(criarRelatorioTextBuilder(cancoesComMaiorVariacao, SAIDA));
		cancoesComMaiorVariacao.gerar(historicoParada).ifPresent(relatorioAtual -> builder
				.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao), posicaoUnicaStringConverter())));

		return builder.toString();
	}

	private ToFormatedStringConverter<VariacaoPosicao> posicaoUnicaStringConverter() {
		return variacao -> '#' + variacao.getElementoB().toString();
	}

	private String exportarCancoesComMaisTempoEmTop(HistoricoParada historicoParada) {
		CancoesComMaisTempoEmTop cancoesComMaisTempoEmTop = getRelatorioGenerator(
				RelatorioGeneratorConfig.cancao().com(MAIOR).ocorrencia(TEMPO).em(TOP));

		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = relatoriosConfig.posicoesRelatorio(cancoesComMaisTempoEmTop);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(cancoesComMaisTempoEmTop, top));
			cancoesComMaisTempoEmTop.setPosicao(top);
			cancoesComMaisTempoEmTop.gerar(historicoParada).ifPresent(
					relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao), null)));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	@SuppressWarnings("unchecked")
	private <T extends Entidade, V extends Comparable<V>, R extends RelatorioGenerator<T, V>> R getRelatorioGenerator(
			RelatorioGeneratorConfig config) {
		return (R) relatorioGeneratorFactory.get(config).get();
	}

	private String signedNumber(VariacaoPosicao variacao) {
		int valor = variacao.getValorVariacao();
		return String.valueOf(valor > 0 ? "+" + valor : valor);
	}

	private StringBuilder criarRelatorioTextBuilder(RelatorioGenerator<?, ?> relatorioGenerator, Object... args) {
		String tituloRelatorio = relatoriosConfig.getTituloRelatorio(relatorioGenerator, args);
		logger.info("Gerando relatório: {}", tituloRelatorio);
		return new StringBuilder(tituloRelatorio).append(StringUtils.CR).append(StringUtils.LF).append(StringUtils.CR).append(StringUtils.LF);
	}

	private String inserirSeparadores(StringBuilder builder, List<Integer> indicesSeparadores) {
		ThreadLocal<Integer> diferenca = ThreadLocal.withInitial(new Supplier<Integer>() {
			// não usar lambda - bug do Eclipse
			@Override
			public Integer get() {
				return 0;
			}
		});

		indicesSeparadores.forEach(indice -> {
			int tamanhoAntes = builder.length();
			builder.insert(indice + diferenca.get(), separador(new StringBuilder()));
			diferenca.set(builder.length() - tamanhoAntes);
		});

		return builder.toString();
	}

	private StringBuilder separador(StringBuilder relatoriosTextBuilder) {
		return relatoriosTextBuilder.append(StringUtils.CR).append(StringUtils.LF).append(relatoriosConfig.separador()).append(StringUtils.CR)
				.append(StringUtils.LF).append(StringUtils.CR).append(StringUtils.LF);
	}
}
