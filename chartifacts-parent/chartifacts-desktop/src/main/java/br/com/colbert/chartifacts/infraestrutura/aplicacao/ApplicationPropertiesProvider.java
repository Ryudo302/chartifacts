package br.com.colbert.chartifacts.infraestrutura.aplicacao;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Provê acesso às propriedades da aplicação.
 *
 * @author Thiago Colbert
 * @since 11/04/2015
 */
@ApplicationScoped
public class ApplicationPropertiesProvider implements Serializable {

	private static final long serialVersionUID = -812464708205462076L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("application");

	/**
	 * Obtém informações sobre a aplicação.
	 *
	 * @return as informações da aplicação
	 */
	@Produces
	@Singleton
	public InformacoesAplicacao informacoesAplicacao() {
		return new InformacoesAplicacao(getNome(), getNumeroVersao(), getNumeroBuild(), getNomeAutor());
	}

	private String getNome() {
		return BUNDLE.getString("app.name");
	}

	private String getNumeroVersao() {
		return BUNDLE.getString("app.version");
	}

	private String getNumeroBuild() {
		return BUNDLE.getString("app.build");
	}

	private String getNomeAutor() {
		return BUNDLE.getString("app.author");
	}
}
