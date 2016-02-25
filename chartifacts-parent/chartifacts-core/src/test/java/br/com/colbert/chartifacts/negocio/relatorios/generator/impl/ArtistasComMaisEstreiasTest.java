package br.com.colbert.chartifacts.negocio.relatorios.generator.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.historico.ParserException;
import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.negocio.relatorios.*;
import br.com.colbert.chartifacts.negocio.relatorios.generator.impl.ArtistasComMaisEstreias;

/**
 * Testes unitários da classe {@link ArtistasComMaisEstreias}.
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 */
public class ArtistasComMaisEstreiasTest extends AbstractRelatorioTest {

	@Inject @Any
	private ArtistasComMaisEstreias generator;

	@Test
	public void testGerar() throws ParserException {
		Optional<Relatorio<Artista, Integer>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Artista, Integer> relatorio = relatorioOptional.get();
		assertThat(relatorio.size(), is(equalTo(6)));

		Map<Artista, Integer> itens = relatorio.getItens();
		assertThat(itens.get(new Artista("TESTE")), is(equalTo(4)));
		assertThat(itens.get(new Artista("TESTE + TESTE2")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE 2")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE 3")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE2")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE3")), is(equalTo(1)));
	}
}
