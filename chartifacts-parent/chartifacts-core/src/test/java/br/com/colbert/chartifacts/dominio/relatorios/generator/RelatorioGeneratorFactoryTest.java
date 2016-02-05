package br.com.colbert.chartifacts.dominio.relatorios.generator;

import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoLocal.TOP;
import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoOcorrencia.ESTREIA;
import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoOcorrencia.TEMPO;
import static br.com.colbert.chartifacts.dominio.relatorios.generator.TipoVariacao.MAIOR;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalPackages;
import org.junit.Test;

import br.com.colbert.chartifacts.dominio.relatorios.generator.impl.*;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes da classe {@link RelatorioGeneratorFactory}.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
@AdditionalPackages(AbstractRelatorioGenerator.class)
public class RelatorioGeneratorFactoryTest extends AbstractTestCase {

	@Inject
	private transient RelatorioGeneratorFactory factory;

	@Test(expected = NullPointerException.class)
	public void deveriaLancarExceptionCasoSejaInformadoNull() {
		factory.get(null);
	}

	@Test
	public void deveriaRetornarVazioCasoConfigSejaInvalida() {
		Optional<RelatorioGenerator<?, ?>> generator = factory.get(RelatorioGeneratorConfig.artista().com(MAIOR));

		assertThat(generator.isPresent(), is(false));
	}

	@Test
	public void testGetArtistasComMaisEstreias() {
		Optional<RelatorioGenerator<?, ?>> generator = factory.get(RelatorioGeneratorConfig.artista().com(MAIOR).ocorrencia(ESTREIA));

		assertThat(generator.isPresent(), is(true));
		assertThat(generator.get(), instanceOf(ArtistasComMaisEstreias.class));
	}

	@Test
	public void testGetCancoesComMaisTempoEmTop() {
		Optional<RelatorioGenerator<?, ?>> generator = factory.get(RelatorioGeneratorConfig.cancao().com(MAIOR).ocorrencia(TEMPO).em(TOP));

		assertThat(generator.isPresent(), is(true));
		assertThat(generator.get(), instanceOf(CancoesComMaisTempoEmTop.class));
	}
}
