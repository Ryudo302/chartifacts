package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import java.util.*;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;

/**
 * Gera relatórios informando as canções com as maiores permanência em uma determinada posição.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.CANCAO, tipoVariacao = TipoVariacao.MAIOR, tipoOcorrencia = TipoOcorrencia.TEMPO, tipoLocal = TipoLocal.POSICAO)
public class CancoesComMaiorPermanencia extends AbstractRelatorioGenerator<Cancao, PermanenciaPosicao> {

	private static final long serialVersionUID = -1293203212227492588L;

	@Inject
	private Logger logger;
	@Inject
	private ChartRunAnalyzer chartRunAnalyzer;

	private ElementoChartRun posicao;

	@Override
	protected Optional<Relatorio<Cancao, PermanenciaPosicao>> gerarRelatorio(HistoricoParada historico) {
		logger.debug("Restringindo posição? {}", posicao != null ? (posicao != null) + ": " + posicao : posicao != null);

		Map<Cancao, PermanenciaPosicao> itens = new HashMap<>();

		historico.getItens().stream().forEach(itemHistorico -> {
			logger.trace("-----------");
			Cancao cancao = itemHistorico.getCancao();
			ChartRun chartRun = itemHistorico.getChartRun();

			Optional<PermanenciaPosicao> maiorPermanencia = chartRunAnalyzer.getMaiorPermanencia(chartRun, posicao);
			logger.trace("{} = {}", cancao, maiorPermanencia);
			maiorPermanencia.ifPresent(permanencia -> itens.put(cancao, permanencia));
		});

		return itens.isEmpty() ? Optional.empty() : Optional.of(new Relatorio<>(itens, (valor1, valor2) -> valor2.compareTo(valor1)));
	}

	public void setPosicao(ElementoChartRun posicao) {
		this.posicao = posicao;
	}
}
