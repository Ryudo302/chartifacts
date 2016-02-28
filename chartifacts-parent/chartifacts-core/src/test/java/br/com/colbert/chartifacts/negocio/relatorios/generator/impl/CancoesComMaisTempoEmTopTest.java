package br.com.colbert.chartifacts.negocio.relatorios.generator.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.negocio.parser.ParserException;
import br.com.colbert.chartifacts.negocio.relatorios.*;
import br.com.colbert.chartifacts.negocio.relatorios.generator.impl.CancoesComMaisTempoEmTop;

/**
 * Testes unitários da {@link CancoesComMaisTempoEmTop}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class CancoesComMaisTempoEmTopTest extends AbstractRelatorioTest {

	@Inject @Any
	private CancoesComMaisTempoEmTop generator;

	@Test
	public void testGerarRelatorioHistoricoParada() throws ParserException {
		generator.setPosicao(PosicaoChart.valueOf(4));

		Optional<Relatorio<Cancao, Integer>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, Integer> relatorio = relatorioOptional.get();
		assertThat(relatorio.size(), is(equalTo(5)));

		Integer permanenciaNoTop = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"))));
		assertThat(permanenciaNoTop, is(equalTo(4)));
	}
}
