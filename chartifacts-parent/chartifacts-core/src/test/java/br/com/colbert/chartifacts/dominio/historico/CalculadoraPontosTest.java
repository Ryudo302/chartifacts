package br.com.colbert.chartifacts.dominio.historico;

import static br.com.colbert.chartifacts.dominio.chart.PosicaoChart.NUMERO_POSICAO_AUSENCIA;
import static org.junit.Assert.assertEquals;

import org.junit.*;

import br.com.colbert.chartifacts.dominio.chart.*;

/**
 * Testes unitários da classe {@link CalculadoraPontos}.
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 */
public class CalculadoraPontosTest {

	private static final int VALOR_MAIOR_ELEMENTO = 20;
	private static final double DELTA = 0.0001;

	private CalculadoraPontos calculadora;

	@Before
	public void setUp() {
		calculadora = new CalculadoraPontos(VALOR_MAIOR_ELEMENTO);
	}

	@Test(expected = NullPointerException.class)
	public void testCalcularPontosListOfElementoChartRunNull() {
		calculadora.calcularPontos((ChartRun) null);
	}

	@Test
	public void testCalcularPontosListOfElementoChartRunZeros() {
		ChartRun chartRun = ChartRun.novo(NUMERO_POSICAO_AUSENCIA, NUMERO_POSICAO_AUSENCIA, NUMERO_POSICAO_AUSENCIA);
		double pontos = calculadora.calcularPontos(chartRun);
		assertEquals(0, pontos, DELTA);
	}

	@Test
	public void testCalcularPontosListOfElementoChartRunValida() {
		ChartRun chartRun = ChartRun.novo(1, 2, 3);
		double pontos = calculadora.calcularPontos(chartRun);
		assertEquals(20 + 19 + 18, pontos, DELTA);
	}

	@Test
	public void testCacularPontosUmaUnicaPosicao() {
		double pontos = calculadora.calcularPontos(PosicaoChart.valueOf(1));
		assertEquals(20, pontos, DELTA);
	}
}
