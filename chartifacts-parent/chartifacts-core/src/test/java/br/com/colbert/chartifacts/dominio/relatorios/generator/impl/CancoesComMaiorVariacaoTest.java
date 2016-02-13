package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.ParserException;
import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.dominio.relatorios.*;

/**
 * Testes unitários da classe {@link CancoesComMaiorVariacao}.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */

public class CancoesComMaiorVariacaoTest extends AbstractRelatorioTest {

	@Inject @Any
	private CancoesComMaiorVariacao generator;

	@Test
	public void testGetMaiorSubida() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.SUBIDA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(1)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"), new Artista("TESTE2"))));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SUBIDA)));
		assertThat(variacaoPosicao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(4))));
		assertThat(variacaoPosicao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(1))));
	}

	@Test
	public void testGetMaiorQueda() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.QUEDA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(3)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"), new Artista("TESTE2"))));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.QUEDA)));
		assertThat(variacaoPosicao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(1))));
		assertThat(variacaoPosicao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(5))));
	}

	@Test
	public void testGetMaiorEstreia() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.ESTREIA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(5)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"), new Artista("TESTE2"))));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(variacaoPosicao.getElementoA(), is(equalTo(ElementoChartRun.AUSENCIA)));
		assertThat(variacaoPosicao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(3))));
	}

	@Test
	public void testGetMaiorEstreiaEmPosicaoEspecifica() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.ESTREIA);
		generator.setPosicao(ElementoChartRun.valueOf(1));

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(3)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("!@#$%sf56", Arrays.asList(new Artista("TESTE"))));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(variacaoPosicao.getElementoA(), is(equalTo(ElementoChartRun.AUSENCIA)));
		assertThat(variacaoPosicao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(1))));
	}

	@Test
	public void testGetMaiorSaida() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.SAIDA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(5)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"), new Artista("TESTE2"))));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SAIDA)));
		assertThat(variacaoPosicao.getElementoA(), is(equalTo(ElementoChartRun.valueOf(5))));
		assertThat(variacaoPosicao.getElementoB(), is(equalTo(ElementoChartRun.AUSENCIA)));
	}

	@Test
	public void testGetMaiorRetorno() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.RETORNO);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(2)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(new Artista("TESTE"))));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.RETORNO)));
		assertThat(variacaoPosicao.getElementoA(), is(equalTo(ElementoChartRun.AUSENCIA)));
		assertThat(variacaoPosicao.getElementoB(), is(equalTo(ElementoChartRun.valueOf(4))));
	}
}
