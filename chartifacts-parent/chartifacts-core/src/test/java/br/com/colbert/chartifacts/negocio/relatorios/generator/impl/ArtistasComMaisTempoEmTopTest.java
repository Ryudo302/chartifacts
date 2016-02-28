package br.com.colbert.chartifacts.negocio.relatorios.generator.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.negocio.parser.ParserException;
import br.com.colbert.chartifacts.negocio.relatorios.*;
import br.com.colbert.chartifacts.negocio.relatorios.generator.impl.ArtistasComMaisTempoEmTop;

/**
 * Testes unitários da {@link ArtistasComMaisTempoEmTop}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class ArtistasComMaisTempoEmTopTest extends AbstractRelatorioTest {

	@Inject @Any
	private ArtistasComMaisTempoEmTop generator;

	@Test
	public void testGerarRelatorioHistoricoParada() throws ParserException {
		generator.setPosicao(PosicaoChart.valueOf(3));

		Optional<Relatorio<Artista, Integer>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Artista, Integer> relatorio = relatorioOptional.get();
		assertThat(relatorio.size(), is(equalTo(6)));

		Map<Artista, Integer> itens = relatorio.getItens();
		assertThat(itens.get(new Artista("TESTE")), is(equalTo(11)));
	}
}
