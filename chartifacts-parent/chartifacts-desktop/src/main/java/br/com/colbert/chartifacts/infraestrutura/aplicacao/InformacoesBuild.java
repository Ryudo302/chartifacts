package br.com.colbert.chartifacts.infraestrutura.aplicacao;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando as informações de <em>build</em> da aplicação.
 *
 * @author Thiago Colbert
 * @since 11/04/2015
 */
@Qualifier
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface InformacoesBuild {

	/**
	 * Define se o número de versão deve ser incluído.
	 *
	 * @return <code>true</code> caso deva ser incluído, <code>false</code> caso contrário
	 */
	boolean numeroVersao() default true;

	/**
	 * Define se o número da construção (<em>build</em>) deve ser incluído.
	 *
	 * @return <code>true</code> caso deva ser incluído, <code>false</code> caso contrário
	 */
	boolean numeroBuild() default true;
}
