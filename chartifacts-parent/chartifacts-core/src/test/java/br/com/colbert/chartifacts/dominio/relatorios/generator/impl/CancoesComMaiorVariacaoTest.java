package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import static br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun.NUMERO_POSICAO_AUSENCIA;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.dominio.chart.ParserException;
import br.com.colbert.chartifacts.dominio.chartrun.analyze.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.dominio.relatorios.*;

/**
 * Testes unitários da classe {@link CancoesComMaiorVariacao}.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
public class CancoesComMaiorVariacaoTest extends AbstractRelatorioTest {

	private static final Artista ARTISTA_TESTE1 = new Artista("TESTE");
	private static final Artista ARTISTA_TESTE2 = new Artista("TESTE2");
	private static final Cancao CANCAO_ARTISTA1_ARTISTA2 = new Cancao("TESTE", Arrays.asList(ARTISTA_TESTE1, ARTISTA_TESTE2));

	@Inject
	@Any
	private transient CancoesComMaiorVariacao generator;

	@Test
	public void testGetMaiorSubida() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.SUBIDA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(1)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(CANCAO_ARTISTA1_ARTISTA2);
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SUBIDA)));
		assertThat(variacaoPosicao.getNumeroPosicaoA(), is(equalTo(4)));
		assertThat(variacaoPosicao.getNumeroPosicaoB(), is(equalTo(1)));
	}

	@Test
	public void testGetMaiorQueda() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.QUEDA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(4)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(CANCAO_ARTISTA1_ARTISTA2);
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.QUEDA)));
		assertThat(variacaoPosicao.getNumeroPosicaoA(), is(equalTo(1)));
		assertThat(variacaoPosicao.getNumeroPosicaoB(), is(equalTo(5)));
	}

	@Test
	public void testGetMaiorEstreia() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.ESTREIA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(5)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(CANCAO_ARTISTA1_ARTISTA2);
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(variacaoPosicao.getNumeroPosicaoA(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
		assertThat(variacaoPosicao.getNumeroPosicaoB(), is(equalTo(3)));
	}

	@Test
	public void testGetMaiorEstreiaEmPosicaoEspecifica() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.ESTREIA);
		generator.setPosicao(1);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(3)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("!@#$%sf56", Arrays.asList(ARTISTA_TESTE1)));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.ESTREIA)));
		assertThat(variacaoPosicao.getNumeroPosicaoA(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
		assertThat(variacaoPosicao.getNumeroPosicaoB(), is(equalTo(1)));
	}

	@Test
	public void testGetMaiorSaida() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.SAIDA);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(4)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(CANCAO_ARTISTA1_ARTISTA2);
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.SAIDA)));
		assertThat(variacaoPosicao.getNumeroPosicaoA(), is(equalTo(5)));
		assertThat(variacaoPosicao.getNumeroPosicaoB(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
	}

	@Test
	public void testGetMaiorRetorno() throws ParserException {
		generator.setTipoVariacao(TipoVariacaoPosicao.RETORNO);

		Optional<Relatorio<Cancao, VariacaoPosicao>> relatorioOptional = generator.gerar(parseHistoricoParada());
		assertThat(relatorioOptional.isPresent(), is(true));

		Relatorio<Cancao, VariacaoPosicao> relatorio = relatorioOptional.get();
		assertThat(relatorio, is(notNullValue(Relatorio.class)));
		assertThat(relatorio.size(), is(equalTo(3)));

		VariacaoPosicao variacaoPosicao = relatorio.getItens().get(new Cancao("TESTE", Arrays.asList(ARTISTA_TESTE1)));
		assertThat(variacaoPosicao, is(notNullValue(VariacaoPosicao.class)));
		assertThat(variacaoPosicao.getTipoVariacao(), is(equalTo(TipoVariacaoPosicao.RETORNO)));
		assertThat(variacaoPosicao.getNumeroPosicaoA(), is(equalTo(NUMERO_POSICAO_AUSENCIA)));
		assertThat(variacaoPosicao.getNumeroPosicaoB(), is(equalTo(4)));
	}
}
