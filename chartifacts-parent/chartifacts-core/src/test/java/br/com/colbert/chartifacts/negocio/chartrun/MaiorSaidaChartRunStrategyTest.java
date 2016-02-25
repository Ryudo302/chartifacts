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
 * Testes unitários da {@link MaiorSaidaChartRunStrategy}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MaiorSaidaChartRunStrategyTest extends AbstractTestCase {

	@Inject
	private MaiorSaidaChartRunStrategy strategy;

	@Test
	public void testIdentificarComUmaUnicaSaida() {
		ChartRun chartRun = ChartRun.novo(10, 12, 15);

		Optional<VariacaoPosicao> maiorSaida = strategy.identificar(chartRun);
		assertThat(maiorSaida.isPresent(), is(true));

		VariacaoPosicao variacao = maiorSaida.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(15)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
	}

	@Test
	public void testIdentificarComDuasSaidas() {
		ChartRun chartRun = ChartRun.novo(3, 12, 15, NUMERO_POSICAO_AUSENCIA, 4, NUMERO_POSICAO_AUSENCIA, 8);

		Optional<VariacaoPosicao> maiorSaida = strategy.identificar(chartRun);
		assertThat(maiorSaida.isPresent(), is(true));

		VariacaoPosicao variacao = maiorSaida.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(4)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
	}
}
