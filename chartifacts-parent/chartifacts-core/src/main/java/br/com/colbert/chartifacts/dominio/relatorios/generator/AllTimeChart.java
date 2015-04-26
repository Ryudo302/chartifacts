package br.com.colbert.chartifacts.dominio.relatorios.generator;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;

/**
 * Responsável por gerar a <em>All-Time Chart</em> da parada musical.
 *
 * @author Thiago Colbert
 * @since 25/04/2015
 */
public class AllTimeChart extends AbstractRelatorioGenerator<ItemHistoricoParada, Integer> {

	private static final long serialVersionUID = 4597176155522040573L;

	@Inject
	private Logger logger;

	@Override
	protected Optional<Relatorio<ItemHistoricoParada, Integer>> gerarRelatorio(HistoricoParada historico) {
		logger.debug("All-Time Chart");

		Map<ItemHistoricoParada, Integer> itens = new HashMap<>();

		List<ItemHistoricoParada> itensOrdenados = historico.getItens().stream()
				.sorted((item1, item2) -> item1.getEstatisticas().compareTo(item2.getEstatisticas())).collect(Collectors.toList());
		for (int i = 0; i < itensOrdenados.size(); i++) {
			itens.put(itensOrdenados.get(i), i + 1);
		}

		return criarRelatorio(itens);
	}
}
