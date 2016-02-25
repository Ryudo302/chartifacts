package br.com.colbert.chartifacts.dominio.chart;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.*;

import org.junit.*;

import br.com.colbert.chartifacts.dominio.historico.CalculadoraPontos;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;

/**
 * Testes unitários da {@link ChartBuilder}.
 * 
 * @author ThiagoColbert
 * @since 25 de fev de 2016
 */
public class ChartBuilderTest {

	private CalculadoraPontos calculadoraPontos;

	@Before
	public void setUp() {
		calculadoraPontos = new CalculadoraPontos(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveriaLancarExceptionCasoNumeroSejaInvalido() {
		ChartBuilder.novo(-1, calculadoraPontos);
	}

	@Test(expected = NullPointerException.class)
	public void deveriaLancarExceptionCasoCalculadoraNaoSejaDefinida() {
		ChartBuilder.novo(1, null);
	}

	@Test(expected = IllegalStateException.class)
	public void deveriaLancarExceptionCasoIntervaloNaoSejaDefinido() {
		ChartBuilder.novo(1, calculadoraPontos).comCancoes(Arrays.asList(new Cancao("Teste", Arrays.asList(new Artista("Teste"))))).build();
	}

	@Test(expected = IllegalStateException.class)
	public void deveriaLancarExceptionCasoCancoesNaoSejamDefinidas() {
		ChartBuilder.novo(1, calculadoraPontos).comPeriodo(IntervaloDeDatas.novo().de(LocalDate.now()).ate(LocalDate.now())).build();
	}

	public void testNovo() {
		IntervaloDeDatas periodo = IntervaloDeDatas.novo().de(LocalDate.now()).ate(LocalDate.now());
		List<Cancao> cancoes = Arrays.asList(new Cancao("Teste", Arrays.asList(new Artista("Teste"))));

		Chart chart = ChartBuilder.novo(1, calculadoraPontos).comPeriodo(periodo).comCancoes(cancoes).build();

		assertThat(chart, is(notNullValue(Chart.class)));
		assertThat(chart.getNumero(), is(equalTo(1)));
		assertThat(chart.getPeriodo(), is(equalTo(periodo)));
		assertThat(chart.getCancoes(), is(equalTo(cancoes)));
	}
}
