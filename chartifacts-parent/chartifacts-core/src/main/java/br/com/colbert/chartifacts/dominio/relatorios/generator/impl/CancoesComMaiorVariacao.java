package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import java.util.*;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;
import br.com.colbert.chartifacts.infraestrutura.ordenacao.TipoOrdenacao;

/**
 * Gera relatórios informando as canções com as maiores ocorrências de um determinado tipo de variação de posição.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.CANCAO, tipoVariacao = TipoVariacao.MAIOR, tipoOcorrencia = { TipoOcorrencia.SUBIDA, TipoOcorrencia.QUEDA,
		TipoOcorrencia.ESTREIA, TipoOcorrencia.RETORNO, TipoOcorrencia.SAIDA })
public class CancoesComMaiorVariacao extends AbstractRelatorioGenerator<Cancao, VariacaoPosicao> {

	private static final long serialVersionUID = -1293203212227492588L;

	private static final Map<TipoVariacaoPosicao, TipoOrdenacao> ORDENACOES_DAS_VARIACOES;

	static {
		// TODO Aqui é o melhor lugar pra isso?
		ORDENACOES_DAS_VARIACOES = new HashMap<>(TipoVariacaoPosicao.values().length);
		ORDENACOES_DAS_VARIACOES.put(TipoVariacaoPosicao.ESTREIA, TipoOrdenacao.ASCENDENTE);
		ORDENACOES_DAS_VARIACOES.put(TipoVariacaoPosicao.QUEDA, TipoOrdenacao.DESCENCENTE);
		ORDENACOES_DAS_VARIACOES.put(TipoVariacaoPosicao.RETORNO, TipoOrdenacao.ASCENDENTE);
		ORDENACOES_DAS_VARIACOES.put(TipoVariacaoPosicao.SAIDA, TipoOrdenacao.ASCENDENTE);
		ORDENACOES_DAS_VARIACOES.put(TipoVariacaoPosicao.SUBIDA, TipoOrdenacao.ASCENDENTE);
	}

	@Inject
	private Logger logger;
	@Inject
	private ChartRunAnalyzer chartRunAnalyzer;

	private TipoVariacaoPosicao tipoVariacao;
	private TipoOrdenacao tipoOrdenacao;
	private ElementoChartRun posicao;

	@Override
	protected Optional<Relatorio<Cancao, VariacaoPosicao>> gerarRelatorio(HistoricoParada historico) {
		if (tipoVariacao == null) {
			throw new IllegalStateException("O tipo de variação não foi definido");
		}

		logger.debug("Canções com maior {}", tipoVariacao);
		logger.debug("Utilizando ordenação {}", tipoOrdenacao);
		logger.debug("Restringindo posição? {}", posicao != null ? (posicao != null) + ": " + posicao : posicao != null);

		Map<Cancao, VariacaoPosicao> itens = new HashMap<>();

		historico.getItens().stream().forEach(itemHistorico -> {
			Cancao cancao = itemHistorico.getCancao();
			ChartRun chartRun = itemHistorico.getChartRun();

			Optional<VariacaoPosicao> maiorVariacao = chartRunAnalyzer.getMaiorVariacao(chartRun, tipoVariacao, posicao);
			maiorVariacao.ifPresent(variacao -> itens.put(cancao, variacao));
		});

		return itens.isEmpty() ? Optional.empty() : Optional.of(new Relatorio<>(itens, getComparator()));
	}

	private Comparator<VariacaoPosicao> getComparator() {
		return tipoOrdenacao == TipoOrdenacao.DESCENCENTE ? (valor1, valor2) -> valor1.compareTo(valor2) : (valor1, valor2) -> valor2.compareTo(valor1);
	}

	public void setTipoVariacao(TipoVariacaoPosicao tipoVariacao) {
		this.tipoVariacao = tipoVariacao;
		tipoOrdenacao = ORDENACOES_DAS_VARIACOES.get(tipoVariacao);
	}

	public void setPosicao(ElementoChartRun posicao) {
		this.posicao = posicao;
	}
}
