package br.com.colbert.chartifacts.infraestrutura.properties;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.*;

import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Testes da classe {@link PropertiesFilesResolver}.
 * 
 * @author Thiago Colbert
 * @since 8 de mai de 2016
 */
public class PropertiesFilesResolverTest extends AbstractTestCase {

	@Inject
	private PropertiesFilesResolver propertiesFilesResolver;

	@BeforeClass
	public static void setUpClass() {
		System.setProperty(PropertiesFilesResolver.SYSTEM_PROPERTY_ARQUIVOS, "props1.properties,props2.properties,props3.properties");
	}

	@Test
	public void testGetProperty() {
		assertThat(propertiesFilesResolver.getProperty("A"), is(equalTo("1")));
		assertThat(propertiesFilesResolver.getProperty("B"), is(equalTo("2")));
		assertThat(propertiesFilesResolver.getProperty("C"), is(equalTo("6")));
		assertThat(propertiesFilesResolver.getProperty("D"), is(equalTo("4")));
		assertThat(propertiesFilesResolver.getProperty("E"), is(equalTo("5")));
		assertThat(propertiesFilesResolver.getProperty("F"), is(nullValue(String.class)));
	}
}
