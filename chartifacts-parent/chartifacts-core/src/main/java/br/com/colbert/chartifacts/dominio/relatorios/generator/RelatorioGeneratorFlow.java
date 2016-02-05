package br.com.colbert.chartifacts.dominio.relatorios.generator;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.*;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Anotação que qualifica uma classe como sendo parte do fluxo de configuração de um relatório.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD })
@Qualifier
public @interface RelatorioGeneratorFlow {

	/**
	 * Tipo de entidade do relatório
	 * 
	 * @return
	 */
	TipoEntidade tipoEntidade();

	/**
	 * Tipo de variação do relatório.
	 * 
	 * @return
	 */
	TipoVariacao tipoVariacao();

	/**
	 * Tipos de ocorrências do relatório. O padrão é {@link TipoOcorrencia#GERAL}.
	 * 
	 * @return
	 */
	@Nonbinding
	TipoOcorrencia[] tipoOcorrencia() default TipoOcorrencia.GERAL;

	/**
	 * Tipos de locais do relatório. O padrão é {@link TipoLocal#PARADA}.
	 * 
	 * @return
	 */
	TipoLocal tipoLocal() default TipoLocal.PARADA;
}
