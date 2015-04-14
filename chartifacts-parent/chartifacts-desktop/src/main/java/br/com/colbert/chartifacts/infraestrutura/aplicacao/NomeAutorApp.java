package br.com.colbert.chartifacts.infraestrutura.aplicacao;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.inject.Qualifier;

/**
 * Qualificador indicando o nome do autor da aplicação.
 *
 * @author Thiago Colbert
 * @since 11/04/2015
 */
@Qualifier
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NomeAutorApp {

}
