package br.com.colbert.chartifacts.infraestrutura.aplicacao;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.*;

import br.com.colbert.chartifacts.infraestrutura.properties.Property;

/**
 * Provê acesso às propriedades da aplicação.
 *
 * @author Thiago Colbert
 * @since 11/04/2015
 */
@ApplicationScoped
public class ApplicationPropertiesProvider implements Serializable {

	private static final long serialVersionUID = -812464708205462076L;

	@Inject
	@Property("app.name")
	private transient String appName;
	@Inject
	@Property("app.version")
	private transient String appVersion;
	@Inject
	@Property("app.build")
	private transient String appBuild;
	@Inject
	@Property("app.author")
	private transient String appAuthor;

	/**
	 * Obtém informações sobre a aplicação.
	 *
	 * @return as informações da aplicação
	 */
	@Produces
	@Singleton
	public InformacoesAplicacao informacoesAplicacao() {
		return new InformacoesAplicacao(appName, appVersion, appBuild, appAuthor);
	}
}
