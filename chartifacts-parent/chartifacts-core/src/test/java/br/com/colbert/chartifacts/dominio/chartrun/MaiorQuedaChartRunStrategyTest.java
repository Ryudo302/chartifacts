package br.com.colbert.chartifacts.dominio.chartrun;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link MaiorQuedaChartRunStrategy}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MaiorQuedaChartRunStrategyTest extends AbstractTestCase {

	@Inject
	private MaiorQuedaChartRunStrategy strategy;

	@Test
	public void testIdentificarComQuedas() {
		ChartRun chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.valueOf(12), ElementoChartRun.valueOf(20));

		Optional<VariacaoPosicao> maiorQueda = strategy.identificar(chartRun);
		assertThat(maiorQueda.isPresent(), is(true));

		VariacaoPosicao variacao = maiorQueda.get();
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(12))));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(20))));
	}

	@Test
	public void testIdentificarSemQuedas() {
		ChartRun chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.valueOf(7), ElementoChartRun.valueOf(5));

		Optional<VariacaoPosicao> maiorQueda = strategy.identificar(chartRun);
		assertThat(maiorQueda.isPresent(), is(false));
	}
}
