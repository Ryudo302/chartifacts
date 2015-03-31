package br.com.colbert.chartifacts.dominio.relatorios;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.relatorios.generator.RelatorioGenerator;

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
	@Any
	private Instance<RelatorioGenerator<?, ?>> relatorioGenerators;

	@Test
	public void testExportarTodosEmTxt() throws ParserException {
		HistoricoParada historicoParada = parseHistoricoParada();

		String relatoriosText = relatoriosFacade.exportarTodosEmTxt(historicoParada);

		relatorioGenerators.forEach(generator -> {
			String nomeRelatorio = generator.getClass().getSimpleName();
			assertThat("Não gerou relatório: " + nomeRelatorio.substring(0, nomeRelatorio.indexOf('$')), relatoriosText,
					containsString(nomeRelatorio));
		});
	}
}