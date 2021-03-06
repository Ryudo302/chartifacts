package br.com.colbert.chartifacts.negocio.relatorios.generator.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.negocio.chartrun.PermanenciaPosicao;
import br.com.colbert.chartifacts.negocio.parser.ParserException;
import br.com.colbert.chartifacts.negocio.relatorios.*;
import br.com.colbert.chartifacts.negocio.relatorios.generator.impl.CancoesComMaiorPermanencia;

/**
 * Testes unitários da {@link CancoesComMaiorPermanencia}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class CancoesComMaiorPermanenciaTest extends AbstractRelatorioTest {

	@Inject @Any
	private CancoesComMaiorPermanencia generator;

	@Test
	public void testGerarRelatorioHistoricoParada() throws ParserException {
		generator.setPosicao(PosicaoChart.valueOf(3));

		Optional<Relatorio<Cancao, PermanenciaPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, PermanenciaPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(4)));

		PermanenciaPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"))));
		assertThat(variacaoPosicao, is(notNullValue(PermanenciaPosicao.class)));
		assertThat(variacaoPosicao.getPosicao(), is(equalTo(PosicaoChart.valueOf(3))));
		assertThat(variacaoPosicao.getQuantidade(), is(equalTo(3)));
	}
}
