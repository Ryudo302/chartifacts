package br.com.colbert.chartifacts.dominio.chart;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.*;

import javax.inject.Inject;

import org.junit.*;

import br.com.colbert.chartifacts.dominio.historico.CalculadoraPontos;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link ChartBuilder}.
 * 
 * @author ThiagoColbert
 * @since 25 de fev de 2016
 */
public class ChartBuilderTest extends AbstractTestCase {

	@Inject
	private ChartBuilder chartBuilder;

	private CalculadoraPontos calculadoraPontos;

	@Before
	public void setUp() {
		calculadoraPontos = new CalculadoraPontos(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveriaLancarExceptionCasoNumeroSejaInvalido() {
		chartBuilder.numero(-1);
	}

	@Test(expected = IllegalStateException.class)
	public void deveriaLancarExceptionCasoIntervaloNaoSejaDefinido() {
		chartBuilder.numero(1).comCancoes(Arrays.asList(new Cancao("Teste", Arrays.asList(new Artista("Teste")))), calculadoraPontos).build();
	}

	@Test(expected = IllegalStateException.class)
	public void deveriaLancarExceptionCasoCancoesNaoSejamDefinidas() {
		chartBuilder.numero(1).comPeriodo(IntervaloDeDatas.novo().de(LocalDate.now()).ate(LocalDate.now())).build();
	}

	public void testNovo() {
		IntervaloDeDatas periodo = IntervaloDeDatas.novo().de(LocalDate.now()).ate(LocalDate.now());
		List<Cancao> cancoes = Arrays.asList(new Cancao("Teste", Arrays.asList(new Artista("Teste"))));

		Chart chart = chartBuilder.numero(1).comPeriodo(periodo).comCancoes(cancoes, calculadoraPontos).build();

		assertThat(chart, is(notNullValue(Chart.class)));
		assertThat(chart.getNumero(), is(equalTo(1)));
		assertThat(chart.getPeriodo(), is(equalTo(periodo)));
		assertThat(chart.getCancoes(), is(equalTo(cancoes)));
	}
}
