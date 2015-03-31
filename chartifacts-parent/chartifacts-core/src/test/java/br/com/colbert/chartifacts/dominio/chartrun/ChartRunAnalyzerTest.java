package br.com.colbert.chartifacts.dominio.chartrun;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalPackages;
import org.junit.Test;

import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da classe {@link ChartRunAnalyzer}.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
@AdditionalPackages(ChartRun.class)
public class ChartRunAnalyzerTest extends AbstractTestCase {

	private ChartRun chartRun;

	@Inject
	private ChartRunAnalyzer analyzer;

	@Test
	public void testGetMaiorSubidaDeveriaRetornarVazioParaChartRunSemSubida() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.AUSENCIA, ElementoChartRun.valueOf(1));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.SUBIDA);
		assertThat(optional.isPresent(), is(false));
	}

	@Test
	public void testGetMaiorSubida() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(10), ElementoChartRun.valueOf(5), ElementoChartRun.valueOf(1));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.SUBIDA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SUBIDA)));
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(10))));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(5))));
	}

	@Test
	public void testGetMaiorQuedaDeveriaRetornarVazioParaChartRunSemSubida() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(1), ElementoChartRun.AUSENCIA, ElementoChartRun.valueOf(5));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.QUEDA);
		assertThat(optional.isPresent(), is(false));
	}

	@Test
	public void testGetMaiorQueda() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(1), ElementoChartRun.valueOf(5), ElementoChartRun.valueOf(10));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.QUEDA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.QUEDA)));
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(5))));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(10))));
	}

	@Test
	public void testGetMaiorEstreia() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(5), ElementoChartRun.AUSENCIA, ElementoChartRun.valueOf(4));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.ESTREIA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.AUSENCIA)));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(5))));
	}

	@Test
	public void testGetMaiorRetornoDeveriaRetornarVazioParaChartRunSemRetornos() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(1), ElementoChartRun.valueOf(5));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.RETORNO);
		assertThat(optional.isPresent(), is(false));
	}

	@Test
	public void testGetMaiorRetorno() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(5), ElementoChartRun.AUSENCIA, ElementoChartRun.valueOf(4));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.RETORNO);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.RETORNO)));
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.AUSENCIA)));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(4))));
	}

	@Test
	public void testGetMaiorSaida() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(5), ElementoChartRun.AUSENCIA, ElementoChartRun.valueOf(4), ElementoChartRun.AUSENCIA,
				ElementoChartRun.valueOf(8));

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.SAIDA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SAIDA)));
		assertThat(variacao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(4))));
		assertThat(variacao.getElementoB(), is(equalTo(ElementoChartRun.AUSENCIA)));
	}

	@Test
	public void testGetMaiorPermanencia() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(5), ElementoChartRun.valueOf(5), ElementoChartRun.AUSENCIA, ElementoChartRun.AUSENCIA,
				ElementoChartRun.AUSENCIA, ElementoChartRun.valueOf(4));

		Optional<PermanenciaPosicao> optional = analyzer.getMaiorPermanencia(chartRun, ElementoChartRun.valueOf(5));
		assertThat(optional.isPresent(), is(true));

		PermanenciaPosicao permanencia = optional.get();
		assertThat(permanencia.getPosicao(), is(equalTo(ElementoChartRun.valueOf(5))));
		assertThat(permanencia.getQuantidade(), is(equalTo(2)));
	}

	@Test
	public void testGetMaiorPermanenciaSemPosicao() {
		chartRun = ChartRun.novo(ElementoChartRun.valueOf(5), ElementoChartRun.valueOf(4));

		Optional<PermanenciaPosicao> optional = analyzer.getMaiorPermanencia(chartRun, ElementoChartRun.valueOf(3));
		assertThat(optional.isPresent(), is(false));
	}
}
