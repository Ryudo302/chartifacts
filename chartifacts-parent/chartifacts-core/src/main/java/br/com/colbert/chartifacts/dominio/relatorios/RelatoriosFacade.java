package br.com.colbert.chartifacts.dominio.relatorios;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.dominio.relatorios.export.ToFormatedStringConverter;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;
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
	private Logger logger;

	@Inject
	private RelatoriosConfiguration config;

	@Inject
	@Any
	private Instance<AbstractRelatorioGenerator<?, ?>> relatorioGenerators;

	@Inject
	private ArtistasComMaisEstreias artistasComMaisEstreias;
	@Inject
	private ArtistasComMaisTempoEmTop artistasComMaisTempoEmTop;
	@Inject
	private ArtistasComMaisTop artistasComMaisTop;

	@Inject
	private CancoesComMaiorPermanencia cancoesComMaiorPermanencia;
	@Inject
	private CancoesComMaiorVariacao cancoesComMaiorVariacao;
	@Inject
	private CancoesComMaisTempoEmTop cancoesComMaisTempoEmTop;

	@Inject
	private CancaoFormatter cancaoFormatter;

	@Inject
	private RelatorioTextExporter relatorioTextExporter;

	@PostConstruct
	protected void config() {
		relatorioTextExporter.setLarguraPrimeiraColuna(config.larguraPrimeiraColuna());
		relatorioGenerators.forEach(relatorioGenerator -> relatorioGenerator.setLimiteTamanho(config.limiteTamanho()));
	}

	public String exportarTodosEmTxt(HistoricoParada historicoParada) {
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
		relatoriosTextBuilder.append(exportarCancoesComMaisTempoEmTop(historicoParada));
		return relatoriosTextBuilder.toString();
	}

	private StringBuilder exportarArtistasComMaisEstreias(HistoricoParada historicoParada) {
		StringBuilder builder = criarRelatorioTextBuilder(artistasComMaisEstreias);
		Optional<Relatorio<Artista, Integer>> relatorioArtistasComMaisEstreias = artistasComMaisEstreias.gerar(historicoParada);
		relatorioArtistasComMaisEstreias.ifPresent(relatorio -> builder.append(relatorioTextExporter.export(relatorio, null, null)));
		return builder;
	}

	private String exportarArtistasComMaisTempoEmTop(HistoricoParada historicoParada) {
		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = config.posicoesRelatorio(artistasComMaisTempoEmTop);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(artistasComMaisTempoEmTop, top));
			artistasComMaisTempoEmTop.setPosicao(top);
			artistasComMaisTempoEmTop.gerar(historicoParada).ifPresent(
					relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, null, null)));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	private String exportarArtistasComMaisTop(HistoricoParada historicoParada) {
		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = config.posicoesRelatorio(artistasComMaisTop);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(artistasComMaisTop, top));
			artistasComMaisTop.setPosicao(top);
			artistasComMaisTop.gerar(historicoParada).ifPresent(
					relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, null, null)));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	private String exportarCancoesComMaiorPermanencia(HistoricoParada historicoParada) {
		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = config.posicoesRelatorio(cancoesComMaiorPermanencia);
		int quantidadeSeparadores = tops.size() - 1;
		List<Integer> separadores = new ArrayList<>(quantidadeSeparadores);

		tops.forEach(top -> {
			builder.append(criarRelatorioTextBuilder(cancoesComMaiorPermanencia, top));
			cancoesComMaiorPermanencia.setPosicao(top);
			cancoesComMaiorPermanencia.gerar(historicoParada).ifPresent(
					relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao),
							permanencia -> permanencia.getQuantidade() + "x")));
			if (separadores.size() < quantidadeSeparadores) {
				separadores.add(builder.length());
			}
		});

		return inserirSeparadores(builder, separadores);
	}

	private String exportarCancoesComMaiorVariacao(HistoricoParada historicoParada, TipoVariacaoPosicao tipoVariacao) {
		StringBuilder builder = new StringBuilder();
		cancoesComMaiorVariacao.setTipoVariacao(tipoVariacao);

		builder.append(criarRelatorioTextBuilder(cancoesComMaiorVariacao, tipoVariacao));
		cancoesComMaiorVariacao.gerar(historicoParada).ifPresent(
				relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao),
						variacao -> '[' + variacao.getElementoA().toString() + '-' + variacao.getElementoB() + ']' + StringUtils.SPACE + '['
								+ signedNumber(variacao) + ']')));

		return builder.toString();
	}

	private String exportarCancoesComMaiorEntrada(HistoricoParada historicoParada, TipoVariacaoPosicao tipoVariacao) {
		StringBuilder builder = new StringBuilder();
		cancoesComMaiorVariacao.setTipoVariacao(tipoVariacao);

		builder.append(criarRelatorioTextBuilder(cancoesComMaiorVariacao, tipoVariacao));
		cancoesComMaiorVariacao.gerar(historicoParada).ifPresent(
				relatorioAtual -> builder.append(relatorioTextExporter.export(relatorioAtual, cancao -> cancaoFormatter.format(cancao),
						posicaoUnicaStringConverter())));

		return builder.toString();
	}

	private ToFormatedStringConverter<VariacaoPosicao> posicaoUnicaStringConverter() {
		return variacao -> '#' + variacao.getElementoB().toString();
	}

	private String exportarCancoesComMaisTempoEmTop(HistoricoParada historicoParada) {
		StringBuilder builder = new StringBuilder();

		Collection<ElementoChartRun> tops = config.posicoesRelatorio(cancoesComMaisTempoEmTop);
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

	private String signedNumber(VariacaoPosicao variacao) {
		int valor = variacao.getValorVariacao();
		return String.valueOf(valor > 0 ? "+" + valor : valor);
	}

	private StringBuilder criarRelatorioTextBuilder(RelatorioGenerator<?, ?> relatorioGenerator, Object... args) {
		String tituloRelatorio = config.getTituloRelatorio(relatorioGenerator, args);
		logger.info("Gerando relatório: {}", tituloRelatorio);
		return new StringBuilder(tituloRelatorio).append(StringUtils.LF).append(StringUtils.LF);
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
		return relatoriosTextBuilder.append(StringUtils.LF).append(config.separador()).append(StringUtils.LF).append(StringUtils.LF);
	}
}
