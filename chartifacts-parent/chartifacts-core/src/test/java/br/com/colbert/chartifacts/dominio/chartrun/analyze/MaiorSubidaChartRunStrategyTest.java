package br.com.colbert.chartifacts.dominio.chartrun.analyze;

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
		ChartRun chartRun = ChartRun.novo(10, 7, 1);

		Optional<VariacaoPosicao> maiorSubida = strategy.identificar(chartRun);
		assertThat(maiorSubida.isPresent(), is(true));

		VariacaoPosicao variacao = maiorSubida.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(7)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(1)));
	}

	@Test
	public void testIdentificarComSubidasESaidas() {
		ChartRun chartRun = ChartRun.novo(10, 7, 1, NUMERO_POSICAO_AUSENCIA, 5, 10);

		Optional<VariacaoPosicao> maiorQueda = strategy.identificar(chartRun);
		assertThat(maiorQueda.isPresent(), is(true));

		VariacaoPosicao variacao = maiorQueda.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(7)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(1)));
	}

	@Test
	public void testIdentificarSemSubidas() {
		ChartRun chartRun = ChartRun.novo(10, 12, 14);

		Optional<VariacaoPosicao> maiorSubida = strategy.identificar(chartRun);
		assertThat(maiorSubida.isPresent(), is(false));
	}
}
