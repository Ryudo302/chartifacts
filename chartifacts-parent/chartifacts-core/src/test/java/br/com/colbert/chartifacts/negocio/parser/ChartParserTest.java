package br.com.colbert.chartifacts.negocio.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.any23.encoding.TikaEncodingDetector;
import org.junit.*;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.negocio.chartrun.*;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes da classe {@link ChartParser}.
 * 
 * @author ThiagoColbert
 * @since 1 de mai de 2016
 */
public class ChartParserTest extends AbstractTestCase {

	private static final int QUANTIDADE_POSICOES = 3;

	@Inject
	private ChartParser chartParser;

	private List<String> chartFileLines;

	@Before
	public void setUp() throws IOException, URISyntaxException {
		Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource("chart.txt").toURI());

		InputStream inputStream = Files.newInputStream(path);
		chartFileLines = Files.readAllLines(path, Charset.forName(new TikaEncodingDetector().guessEncoding(inputStream)));
		inputStream.close();

		chartParser.setNumeroParadaPattern(Pattern.compile("[\\w\\s]+ #(\\d+) .*"));
		chartParser.setCalculadoraPontos(new CalculadoraPontos(QUANTIDADE_POSICOES));
	}

	@Test
	public void testParse() throws ParserException {
		Cancao cancao1 = new Cancao("In the Night", Arrays.asList(new Artista("The Weeknd")));
		Cancao cancao2 = new Cancao("Work from Home", Arrays.asList(new Artista("Fifth Harmony"), new Artista("Ty Dolla $ign")));
		Cancao cancao3 = new Cancao("True Colors", Arrays.asList(new Artista("Zedd"), new Artista("Kesha")));

		Chart chart = chartParser.parse(chartFileLines, QUANTIDADE_POSICOES);

		assertThat(chart.getNumero(), is(equalTo(496)));
		assertThat(chart.getQuantidadePosicoes(), is(equalTo(QUANTIDADE_POSICOES)));

		List<CancaoChart> cancoes = chart.getCancoes();
		assertThat(cancoes.size(), is(equalTo(QUANTIDADE_POSICOES)));

		CancaoChart cancaoChart = cancoes.get(0);
		assertThat(cancaoChart.getNumeroPosicao(), is(equalTo(1)));
		assertThat(cancaoChart.getCancao(), is(equalTo(cancao1)));
		assertThat(cancaoChart.getTipoVariacaoPosicao(), is(equalTo(TipoVariacaoPosicao.PERMANENCIA)));
		assertThat(cancaoChart.getValorVariacaoPosicao(), is(equalTo(0)));

		Estatisticas estatisticas = cancaoChart.getEstatisticas();
		assertThat(estatisticas.getPermanenciaTotal(), is(equalTo(5)));
		assertThat(estatisticas.getMelhorPosicao(), is(equalTo(new PermanenciaPosicao(PosicaoChart.valueOf(1), 3))));
		assertThat(estatisticas.getPontuacao(), is(equalTo(3.0)));

		cancaoChart = cancoes.get(1);
		assertThat(cancaoChart.getNumeroPosicao(), is(equalTo(2)));
		assertThat(cancaoChart.getCancao(), is(equalTo(cancao2)));
		assertThat(cancaoChart.getTipoVariacaoPosicao(), is(equalTo(TipoVariacaoPosicao.SUBIDA)));
		assertThat(cancaoChart.getValorVariacaoPosicao(), is(equalTo(1)));

		estatisticas = cancaoChart.getEstatisticas();
		assertThat(estatisticas.getPermanenciaTotal(), is(equalTo(10)));
		assertThat(estatisticas.getMelhorPosicao(), is(equalTo(new PermanenciaPosicao(PosicaoChart.valueOf(1), 2))));
		assertThat(estatisticas.getPontuacao(), is(equalTo(2.0)));

		cancaoChart = cancoes.get(2);
		assertThat(cancaoChart.getNumeroPosicao(), is(equalTo(3)));
		assertThat(cancaoChart.getCancao(), is(equalTo(cancao3)));
		assertThat(cancaoChart.getTipoVariacaoPosicao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(cancaoChart.getValorVariacaoPosicao(), is(equalTo(0)));

		estatisticas = cancaoChart.getEstatisticas();
		assertThat(estatisticas.getPermanenciaTotal(), is(equalTo(1)));
		assertThat(estatisticas.getMelhorPosicao(), is(equalTo(new PermanenciaPosicao(PosicaoChart.valueOf(3), 1))));
		assertThat(estatisticas.getPontuacao(), is(equalTo(1.0)));

		IntervaloDeDatas periodo = chart.getPeriodo();
		assertThat(periodo.getDataInicial(), is(equalTo(LocalDate.of(2016, Month.JANUARY, 3))));
		assertThat(periodo.getDataFinal().get(), is(equalTo(LocalDate.of(2016, Month.JANUARY, 9))));
	}
}
