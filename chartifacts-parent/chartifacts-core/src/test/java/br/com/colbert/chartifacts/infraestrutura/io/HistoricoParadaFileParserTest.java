package br.com.colbert.chartifacts.infraestrutura.io;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da classe {@link HistoricoParadaFileParser}.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class HistoricoParadaFileParserTest extends AbstractTestCase {

	private static final int QUANTIDADE_POSICOES_PARADA = 20;

	@Inject
	private Logger logger;

	@Inject
	private HistoricoParadaFileParser parser;

	@Test(expected = IllegalArgumentException.class)
	public void deveriaLancarExcecaoQuandoArquivoNaoExiste() throws ParserException {
		parser.parse(new File("xxx"), QUANTIDADE_POSICOES_PARADA);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveriaLancarExcecaoQuandoInformadoDiretorio() throws ParserException {
		parser.parse(loadRecurso("."), QUANTIDADE_POSICOES_PARADA);
	}

	@Test
	public void testParse() throws ParserException {
		HistoricoParada historicoParada = parser.parse(loadRecurso("historico.txt"), QUANTIDADE_POSICOES_PARADA);
		logger.info("Histórico: {}", historicoParada);

		assertThat(historicoParada, is(notNullValue(HistoricoParada.class)));
		assertThat(historicoParada.size(), is(equalTo(5)));
	}

	@Test
	public void testParseArtistas() throws ParserException {
		HistoricoParada historicoParada = parser.parse(loadRecurso("teste-artistas.txt"), QUANTIDADE_POSICOES_PARADA);

		assertThat(historicoParada, is(notNullValue(HistoricoParada.class)));

		List<List<Artista>> artistas = historicoParada.getItens().stream().map(ItemHistoricoParada::getCancao).map(Cancao::getArtistas)
				.collect(Collectors.toList());
		Set<Artista> listaUnicaArtistas = new HashSet<>();
		artistas.forEach(artistasCancoes -> listaUnicaArtistas.addAll(artistasCancoes));

		logger.info("Artistas: {}", listaUnicaArtistas);

		for (int i = 1; i <= 8; i++) {
			assertThat("Não foi encontrado o Artista" + i, listaUnicaArtistas.contains(new Artista("Artista" + i)), is(true));
		}
	}

	private File loadRecurso(String caminho) {
		return FileUtils.toFile(Thread.currentThread().getContextClassLoader().getResource(caminho));
	}
}
