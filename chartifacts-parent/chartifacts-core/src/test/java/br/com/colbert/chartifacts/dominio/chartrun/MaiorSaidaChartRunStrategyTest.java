package br.com.colbert.chartifacts.dominio.chartrun;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link MaiorSaidaChartRunStrategy}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MaiorSaidaChartRunStrategyTest extends AbstractTestCase {

	@Inject
	private MaiorSaidaChartRunStrategy strategy;

	@Test
	public void testIdentificar() {
		ChartRun chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.valueOf(12), ElementoChartRun.valueOf(15));

		Optional<VariacaoPosicao> maiorSaida = strategy.identificar(chartRun);
		assertThat(maiorSaida.isPresent(), is(true));

		VariacaoPosicao variacao = maiorSaida.get();
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(15))));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.AUSENCIA)));
	}
}
