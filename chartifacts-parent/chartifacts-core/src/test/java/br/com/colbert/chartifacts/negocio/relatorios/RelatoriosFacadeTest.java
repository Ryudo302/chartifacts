package br.com.colbert.chartifacts.negocio.relatorios;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.junit.Test;

import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.negocio.relatorios.generator.RelatorioGenerator;
import br.com.colbert.chartifacts.negocio.relatorios.generator.impl.AllTimeChartCancao;

/**
 * Testes da {@link RelatoriosFacade}.
 *
 * @author Thiago Colbert
 * @since 20/03/2015
 */
@ActivatedAlternatives(RelatoriosRAMConfiguration.class)
public class RelatoriosFacadeTest extends AbstractRelatorioTest {

	@Inject
	private RelatoriosFacade relatoriosFacade;

	@Inject
	private RelatoriosConfiguration relatoriosConfiguration;
	@Inject
	@Any
	private Instance<RelatorioGenerator<?, ?>> relatorioGenerators;

	@Test
	public void testExportarTodosEmTxt() throws ParserException {
		HistoricoParada historicoParada = parseHistoricoParada();

		String relatoriosText = relatoriosFacade.exportarTodosRelatoriosEmTxt(historicoParada, relatoriosConfiguration);

		relatorioGenerators.forEach(generator -> {
			if (!(generator instanceof AllTimeChartCancao)) {
				String nomeRelatorio = generator.getClass().getSimpleName();
				assertThat("Não gerou relatório: " + nomeRelatorio.substring(0, nomeRelatorio.indexOf('$')), relatoriosText,
						containsString(nomeRelatorio));
			}
		});
	}

	@Test
	public void testExportarAllTimeChart() throws ParserException {
		HistoricoParada historicoParada = parseHistoricoParada();

		String allTimeChart = relatoriosFacade.exportarAllTimeChartEmTxt(historicoParada, relatoriosConfiguration);

		assertThat(allTimeChart, containsString(AllTimeChartCancao.class.getSimpleName()));
	}
}
