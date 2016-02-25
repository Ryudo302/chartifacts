package br.com.colbert.chartifacts.negocio.relatorios;

import java.text.MessageFormat;
import java.util.*;

import javax.enterprise.inject.Alternative;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.negocio.relatorios.RelatoriosConfiguration;
import br.com.colbert.chartifacts.negocio.relatorios.generator.RelatorioGenerator;

/**
 * Implementação de {@link RelatoriosConfiguration} que mantém os dados em memória.
 *
 * @author Thiago Colbert
 * @since 20/03/2015
 */
@Alternative
public class RelatoriosRAMConfiguration implements RelatoriosConfiguration {

	@Override
	public Optional<Integer> limiteTamanho() {
		return Optional.empty();
	}

	@Override
	public int larguraPrimeiraColuna() {
		return 60;
	}

	@Override
	public String separador() {
		return "------------------------------------------------------------";
	}

	@Override
	public <T extends RelatorioGenerator<?, ?>> Collection<PosicaoChart> posicoesRelatorio(T relatorioGenerator) {
		return Arrays.asList(PosicaoChart.valueOf(2), PosicaoChart.valueOf(3));
	}

	@Override
	public <T extends RelatorioGenerator<?, ?>> String getTituloRelatorio(T relatorioGenerator, Object... args) {
		return MessageFormat.format(relatorioGenerator.getClass().getSimpleName(), args);
	}
}
