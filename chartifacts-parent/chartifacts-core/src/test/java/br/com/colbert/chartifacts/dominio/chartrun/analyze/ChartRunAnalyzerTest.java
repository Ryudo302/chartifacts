package br.com.colbert.chartifacts.dominio.chartrun.analyze;

import static br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun.NUMERO_POSICAO_AUSENCIA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalPackages;
import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chartrun.ChartRun;
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
		chartRun = ChartRun.novo(10, NUMERO_POSICAO_AUSENCIA, 1);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.SUBIDA);
		assertThat(optional.isPresent(), is(false));
	}

	@Test
	public void testGetMaiorSubida() {
		chartRun = ChartRun.novo(10, 5, 1);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.SUBIDA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SUBIDA)));
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(10)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(5)));
	}

	@Test
	public void testGetMaiorQuedaDeveriaRetornarVazioParaChartRunSemSubida() {
		chartRun = ChartRun.novo(1, NUMERO_POSICAO_AUSENCIA, 5);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.QUEDA);
		assertThat(optional.isPresent(), is(false));
	}

	@Test
	public void testGetMaiorQueda() {
		chartRun = ChartRun.novo(1, 5, 10);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.QUEDA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.QUEDA)));
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(5)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(10)));
	}

	@Test
	public void testGetMaiorEstreia() {
		chartRun = ChartRun.novo(5, NUMERO_POSICAO_AUSENCIA, 4);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.ESTREIA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(5)));
	}

	@Test
	public void testGetMaiorRetornoDeveriaRetornarVazioParaChartRunSemRetornos() {
		chartRun = ChartRun.novo(1, 5);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.RETORNO);
		assertThat(optional.isPresent(), is(false));
	}

	@Test
	public void testGetMaiorRetorno() {
		chartRun = ChartRun.novo(5, NUMERO_POSICAO_AUSENCIA, 4);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.RETORNO);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.RETORNO)));
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(4)));
	}

	@Test
	public void testGetMaiorSaida() {
		chartRun = ChartRun.novo(5, NUMERO_POSICAO_AUSENCIA, 4, NUMERO_POSICAO_AUSENCIA, 8);

		Optional<VariacaoPosicao> optional = analyzer.getMaiorVariacao(chartRun, TipoVariacaoPosicao.SAIDA);
		assertThat(optional.isPresent(), is(true));

		VariacaoPosicao variacao = optional.get();
		assertThat(variacao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SAIDA)));
		assertThat(variacao.getNumeroPosicaoA(), is(equalTo(4)));
		assertThat(variacao.getNumeroPosicaoB(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
	}

	@Test
	public void testGetMaiorPermanencia() {
		chartRun = ChartRun.novo(5, 5, NUMERO_POSICAO_AUSENCIA, NUMERO_POSICAO_AUSENCIA, NUMERO_POSICAO_AUSENCIA, 4);

		Optional<PermanenciaPosicao> optional = analyzer.getMaiorPermanencia(chartRun, 5);
		assertThat(optional.isPresent(), is(true));

		PermanenciaPosicao permanencia = optional.get();
		assertThat(permanencia.getNumeroPosicao(), is(equalTo(5)));
		assertThat(permanencia.getQuantidade(), is(equalTo(2)));
	}

	@Test
	public void testGetMaiorPermanenciaSemPosicao() {
		chartRun = ChartRun.novo(5, 4);

		Optional<PermanenciaPosicao> optional = analyzer.getMaiorPermanencia(chartRun, 3);
		assertThat(optional.isPresent(), is(false));
	}
}
