package br.com.colbert.chartifacts.dominio;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.*;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.chartrun.*;

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
		calculadora = new CalculadoraPontos(ElementoChartRun.valueOf(VALOR_MAIOR_ELEMENTO));
	}

	@Test(expected = NullPointerException.class)
	public void testCalcularPontosListOfElementoChartRunNull() {
		calculadora.calcularPontos(null);
	}

	@Test
	public void testCalcularPontosListOfElementoChartRunZeros() {
		ChartRun chartRun = new ChartRun(Arrays.asList(ElementoChartRun.AUSENCIA, ElementoChartRun.AUSENCIA, ElementoChartRun.AUSENCIA));
		double pontos = calculadora.calcularPontos(chartRun);
		assertEquals(0, pontos, DELTA);
	}

	@Test
	public void testCalcularPontosListOfElementoChartRunValida() {
		ChartRun chartRun = new ChartRun(Arrays.asList(ElementoChartRun.valueOf(1), ElementoChartRun.valueOf(2), ElementoChartRun.valueOf(3)));
		double pontos = calculadora.calcularPontos(chartRun);
		assertEquals(20 + 19 + 18, pontos, DELTA);
	}
}
