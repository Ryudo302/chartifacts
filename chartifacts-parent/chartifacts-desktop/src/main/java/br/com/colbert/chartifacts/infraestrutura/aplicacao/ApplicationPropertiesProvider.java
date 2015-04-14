package br.com.colbert.chartifacts.infraestrutura.aplicacao;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

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
	 *
	 * @return
	 */
	@Produces
	@InformacoesBuild(numeroBuild = false, numeroVersao = true)
	@Singleton
	public String numeroVersao() {
		return BUNDLE.getString("app.version");
	}

	/**
	 *
	 * @return
	 */
	@Produces
	@InformacoesBuild(numeroBuild = true, numeroVersao = false)
	@Singleton
	public String numeroBuild() {
		return BUNDLE.getString("app.build");
	}

	/**
	 *
	 * @return
	 */
	@Produces
	@InformacoesBuild(numeroBuild = true, numeroVersao = true)
	@Singleton
	public String numeroVersaoAndBuild() {
		return numeroVersao() + StringUtils.SPACE + numeroBuild();
	}

	/**
	 * Obtém o nome do autor da aplicação.
	 *
	 * @return o nome do autor
	 */
	@Produces
	@NomeAutorApp
	@Singleton
	public String nomeAutor() {
		return BUNDLE.getString("app.author");
	}
}
