package br.com.colbert.chartifacts.infraestrutura.export;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.junit.*;

import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.negocio.relatorios.Relatorio;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes unitários da {@link RelatorioTextExporter}.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 */
public class RelatorioTextExporterTest extends AbstractTestCase {

	private static final int LARGURA_PRIMEIRA_COLUNA = 30;

	@Inject
	private RelatorioTextExporter exporter;

	@Before
	public void setUpExporter() {
		exporter.setLarguraPrimeiraColuna(LARGURA_PRIMEIRA_COLUNA);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testExport() {
		Relatorio<Artista, Integer> relatorio = mock(Relatorio.class);

		final Artista artistaA = new Artista("TesteA");
		final Artista artistaB = new Artista("TesteB");
		final Artista artistaC = new Artista("TesteC");

		final int artistaAvalue = 1;
		final int artistaBvalue = 5;
		final int artistaCvalue = 2;

		Map<Artista, Integer> itens = new HashMap<>();
		itens.put(artistaA, artistaAvalue);
		itens.put(artistaB, artistaBvalue);
		itens.put(artistaC, artistaCvalue);

		when(relatorio.getItens()).thenReturn(itens);

		String relatorioString = exporter.export(relatorio);

		assertThat("Não contém o nome do Artista A", relatorioString, containsString(artistaA.getNome()));
		assertThat("Não contém o nome do Artista B", relatorioString, containsString(artistaB.getNome()));
		assertThat("Não contém o nome do Artista C", relatorioString, containsString(artistaC.getNome()));

		assertThat("Não contém o valor do Artista A", relatorioString, containsString(String.valueOf(artistaAvalue)));
		assertThat("Não contém o valor do Artista B", relatorioString, containsString(String.valueOf(artistaBvalue)));
		assertThat("Não contém o valor do Artista C", relatorioString, containsString(String.valueOf(artistaCvalue)));

		assertThat("Não contém os espaçamentos", relatorioString,
				containsString(StringUtils.repeat(StringUtils.SPACE, LARGURA_PRIMEIRA_COLUNA - artistaA.getNome().length())));
	}
}
