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
 * Testes unitários da {@link MaiorEstreiaChartRunStrategy}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MaiorEstreiaChartRunStrategyTest extends AbstractTestCase {

	@Inject
	private MaiorEstreiaChartRunStrategy strategy;

	@Test
	public void testIdentificar() {
		ChartRun chartRun = ChartRun.novo(3, 2);

		Optional<VariacaoPosicao> maiorEstreia = strategy.identificar(chartRun);
		assertThat(maiorEstreia.isPresent(), is(true));

		VariacaoPosicao variacao = maiorEstreia.get();
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(3)));
	}
}
