package br.com.colbert.chartifacts.dominio.chartrun;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link MaiorSubidaChartRunStrategy}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MaiorSubidaChartRunStrategyTest extends AbstractTestCase {

	@Inject
	private MaiorSubidaChartRunStrategy strategy;

	@Test
	public void testIdentificarComSubidas() {
		ChartRun chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.valueOf(7), ElementoChartRun.valueOf(1));

		Optional<VariacaoPosicao> maiorSubida = strategy.identificar(chartRun);
		assertThat(maiorSubida.isPresent(), is(true));

		VariacaoPosicao variacao = maiorSubida.get();
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(7))));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(1))));
	}

	@Test
	public void testIdentificarSemSubidas() {
		ChartRun chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.valueOf(12), ElementoChartRun.valueOf(14));

		Optional<VariacaoPosicao> maiorSubida = strategy.identificar(chartRun);
		assertThat(maiorSubida.isPresent(), is(false));
	}
}
