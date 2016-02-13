package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.ParserException;
import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;
import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.dominio.relatorios.*;

/**
 * Testes unitários da {@link ArtistasComMaisTop}.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
public class ArtistasComMaisTopTest extends AbstractRelatorioTest {

	@Inject @Any
	private ArtistasComMaisTop generator;

	@PostConstruct
	public void setUp() {
		generator.setPosicao(ElementoChartRun.valueOf(2));
	}

	@Test
	public void testGerar() throws ParserException {
		Optional<Relatorio<Artista, Integer>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Artista, Integer> relatorio = relatorioOptional.get();
		assertThat(relatorio.size(), is(equalTo(6)));

		Map<Artista, Integer> itens = relatorio.getItens();
		assertThat(itens.get(new Artista("TESTE")), is(equalTo(3)));
		assertThat(itens.get(new Artista("TESTE + TESTE2")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE 2")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE 3")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE2")), is(equalTo(1)));
		assertThat(itens.get(new Artista("TESTE3")), is(equalTo(1)));
	}
}
