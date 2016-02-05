package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;

/**
 * Responsável por gerar a <em>All-Time Chart</em> de canções da parada musical.
 *
 * @author Thiago Colbert
 * @since 25/04/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.CANCAO, tipoVariacao = TipoVariacao.MAIOR)
public class AllTimeChartCancao extends AbstractRelatorioGenerator<ItemHistoricoParada, Integer> {

	private static final long serialVersionUID = 4597176155522040573L;

	@Inject
	private transient Logger logger;

	@Override
	protected Optional<Relatorio<ItemHistoricoParada, Integer>> gerarRelatorio(HistoricoParada historico) {
		logger.debug("All-Time Chart de canções");

		Map<ItemHistoricoParada, Integer> itens = new HashMap<>();

		List<ItemHistoricoParada> itensOrdenados = historico.getItens().stream()
				.sorted((item1, item2) -> item1.getEstatisticas().compareTo(item2.getEstatisticas())).collect(Collectors.toList());
		for (int i = 0; i < itensOrdenados.size(); i++) {
			itens.put(itensOrdenados.get(i), i + 1);
		}

		return criarRelatorio(itens);
	}
}
