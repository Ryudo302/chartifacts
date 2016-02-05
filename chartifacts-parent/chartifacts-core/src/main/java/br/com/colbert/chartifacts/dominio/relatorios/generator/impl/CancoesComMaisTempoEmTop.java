package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;

/**
 * Identifica as canções com maior tempo acumulado dentro de um top.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.CANCAO, tipoVariacao = TipoVariacao.MAIOR, tipoOcorrencia = TipoOcorrencia.TEMPO, tipoLocal = TipoLocal.TOP)
public class CancoesComMaisTempoEmTop extends AbstractRelatorioGenerator<Cancao, Integer> {

	private static final long serialVersionUID = 2091891652660676343L;

	@Inject
	private Logger logger;

	private ElementoChartRun posicao;

	@Override
	protected Optional<Relatorio<Cancao, Integer>> gerarRelatorio(HistoricoParada historico) {
		if (posicao == null) {
			throw new IllegalStateException("A posição de corte não foi definida");
		}

		logger.debug("Canções com mais tempo em top {}", posicao);
		Map<Cancao, Integer> itens = new HashMap<>();
		historico.getItens().stream().forEach(itemHistorico -> {
			Cancao cancao = itemHistorico.getCancao();
			ChartRun chartRun = itemHistorico.getChartRun();
			int permanenciaEmTopCancao = chartRun.getElementos().stream().filter(elemento -> elemento.isPresenca() && elemento.compareTo(posicao) <= 0)
					.collect(Collectors.counting()).intValue();
			itens.put(cancao, permanenciaEmTopCancao);
		});
		return criarRelatorio(itens);
	}

	/**
	 * Define a posição de corte do top.
	 *
	 * @param posicao
	 *            a posição de corte
	 */
	public void setPosicao(ElementoChartRun posicao) {
		Validate.isTrue(posicao.isPresenca(), "A posição não pode ser uma ausência");
		this.posicao = posicao;
	}
}
