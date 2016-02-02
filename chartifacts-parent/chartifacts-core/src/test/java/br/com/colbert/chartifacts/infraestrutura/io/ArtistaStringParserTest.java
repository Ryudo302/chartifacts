package br.com.colbert.chartifacts.infraestrutura.io;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da classe {@link ArtistaStringParser}.
 *
 * @author Thiago Colbert
 * @since 16/03/2015
 */
public class ArtistaStringParserTest extends AbstractTestCase {

	@Inject
	private ArtistaStringParser parser;

	@Test
	public void testParseArtistaIniciandoComLetra() {
		List<Artista> artistas = parser.parse("TesteA - \"TesteB\"");

		assertThat(artistas.size(), is(equalTo(1)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
	}

	@Test
	public void testParseArtistaIniciandoComNumero() {
		List<Artista> artistas = parser.parse("1TesteA - \"TesteB\"");

		assertThat(artistas.size(), is(equalTo(1)));
		assertThat(artistas.get(0).getNome(), is(equalTo("1TesteA")));
	}

	@Test
	public void testParseMultiplosArtistasFeat() {
		List<Artista> artistas = parser.parse("TesteA feat. TesteB - \"TesteD\"");

		assertThat(artistas.size(), is(equalTo(2)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
	}

	@Test
	public void testParseMultiplosArtistasFeaturing() {
		List<Artista> artistas = parser.parse("TesteA featuring TesteB - \"TesteD\"");

		assertThat(artistas.size(), is(equalTo(2)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
	}

	@Test
	public void testParseMultiplosArtistasPart() {
		List<Artista> artistas = parser.parse("TesteA part. TesteB - \"TesteD\"");

		assertThat(artistas.size(), is(equalTo(2)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
	}

	@Test
	public void testParseMultiplosArtistasFeatAnd() {
		List<Artista> artistas = parser.parse("TesteA feat. TesteB & TesteC - \"TesteD\"");

		assertThat(artistas.size(), is(equalTo(3)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
		assertThat(artistas.get(2).getNome(), is(equalTo("TesteC")));
	}

	@Test
	public void testParseMultiplosArtistasDueto() {
		List<Artista> artistas = parser.parse("TesteA & TesteB - \"TesteC\"");

		assertThat(artistas.size(), is(equalTo(2)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
	}

	@Test
	public void testParseMultiplosArtistasPresents() {
		List<Artista> artistas = parser.parse("TesteA presents TesteB - \"TesteC\"");

		assertThat(artistas.size(), is(equalTo(2)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
	}

	@Test
	public void testParseMultiplosArtistasVersus() {
		List<Artista> artistas = parser.parse("TesteA vs. TesteB - \"TesteC\"");

		assertThat(artistas.size(), is(equalTo(2)));
		assertThat(artistas.get(0).getNome(), is(equalTo("TesteA")));
		assertThat(artistas.get(1).getNome(), is(equalTo("TesteB")));
	}
}
