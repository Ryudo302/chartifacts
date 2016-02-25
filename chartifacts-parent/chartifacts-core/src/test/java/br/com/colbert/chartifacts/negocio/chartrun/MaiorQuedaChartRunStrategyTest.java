package br.com.colbert.chartifacts.negocio.chartrun;

import static br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun.NUMERO_POSICAO_AUSENCIA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chartrun.ChartRun;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link MaiorQuedaChartRunStrategy}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MaiorQuedaChartRunStrategyTest extends AbstractTestCase {

	@Inject
	private transient MaiorQuedaChartRunStrategy strategy;

	@Test
	public void testIdentificarComQuedas() {
		ChartRun chartRun = ChartRun.novo(10, 12, 20);

		Optional<VariacaoPosicao> maiorQueda = strategy.identificar(chartRun);
		assertThat(maiorQueda.isPresent(), is(true));

		VariacaoPosicao variacao = maiorQueda.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(12)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(20)));
	}

	@Test
	public void testIdentificarComQuedasESaidas() {
		ChartRun chartRun = ChartRun.novo(5, 10, 12, NUMERO_POSICAO_AUSENCIA, 15, 18);

		Optional<VariacaoPosicao> maiorQueda = strategy.identificar(chartRun);
		assertThat(maiorQueda.isPresent(), is(true));

		VariacaoPosicao variacao = maiorQueda.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(5)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(10)));
	}

	@Test
	public void testIdentificarSemQuedas() {
		ChartRun chartRun = ChartRun.novo(10, 7, 5);

		Optional<VariacaoPosicao> maiorQueda = strategy.identificar(chartRun);
		assertThat(maiorQueda.isPresent(), is(false));
	}
}
