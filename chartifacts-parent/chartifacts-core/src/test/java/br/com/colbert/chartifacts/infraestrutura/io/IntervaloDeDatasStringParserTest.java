package br.com.colbert.chartifacts.infraestrutura.io;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import javax.inject.Inject;

import org.junit.Test;

import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link IntervaloDeDatasStringParser}.
 * 
 * @author ThiagoColbert
 * @since 23 de fev de 2016
 */
public class IntervaloDeDatasStringParserTest extends AbstractTestCase {

	@Inject
	private transient IntervaloDeDatasStringParser parser;

	@Test
	public void testParseIntervaloCompleto() {
		IntervaloDeDatas intervaloDeDatas = parser.parse("lala - \"lala\" (01/01/2001 ~ 02/02/2002)");

		assertThat(intervaloDeDatas, is(notNullValue(IntervaloDeDatas.class)));
		assertThat(intervaloDeDatas.getDataInicial(), is(equalTo(LocalDate.parse("2001-01-01"))));
		assertThat(intervaloDeDatas.getDataFinal().isPresent(), is(true));
		assertThat(intervaloDeDatas.getDataFinal().get(), is(equalTo(LocalDate.parse("2002-02-02"))));
	}

	@Test
	public void testParseIntervaloApenasComDataInicial() {
		IntervaloDeDatas intervaloDeDatas = parser.parse("lala - \"lala\" (01/01/2001 ~ )");

		assertThat(intervaloDeDatas, is(notNullValue(IntervaloDeDatas.class)));
		assertThat(intervaloDeDatas.getDataInicial(), is(equalTo(LocalDate.parse("2001-01-01"))));
		assertThat(intervaloDeDatas.getDataFinal().isPresent(), is(false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseIntervaloInvalido() {
		parser.parse("(xxx ~ yyy)");
	}
}
