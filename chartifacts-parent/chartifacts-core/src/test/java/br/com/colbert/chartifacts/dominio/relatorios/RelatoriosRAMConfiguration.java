package br.com.colbert.chartifacts.dominio.relatorios;

import java.text.MessageFormat;
import java.util.*;

import javax.enterprise.inject.Alternative;

import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;
import br.com.colbert.chartifacts.dominio.relatorios.generator.RelatorioGenerator;

/**
 * Implementação de {@link RelatoriosConfiguration} que mantém os dados em memória.
 *
 * @author Thiago Colbert
 * @since 20/03/2015
 */
@Alternative
public class RelatoriosRAMConfiguration implements RelatoriosConfiguration {

	@Override
	public int limiteTamanho() {
		return 0;
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
	public <T extends RelatorioGenerator<?, ?>> Collection<ElementoChartRun> posicoesRelatorio(T relatorioGenerator) {
		return Arrays.asList(ElementoChartRun.valueOf(2), ElementoChartRun.valueOf(3));
	}

	@Override
	public <T extends RelatorioGenerator<?, ?>> String getTituloRelatorio(T relatorioGenerator, Object... args) {
		return MessageFormat.format(relatorioGenerator.getClass().getSimpleName(), args);
	}
}
