package br.com.colbert.chartifacts.infraestrutura.properties;

import java.lang.annotation.*;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Qualificador CDI que indica que uma propriedade deve ser injetada.
 * 
 * @author Chris Ritchies
 * @author Thiago Colbert
 * @since 8 de mai de 2016
 * @see <a href="http://blog.chris-ritchie.com/2015/03/inject-external-properties-cdi-java-wildfly.html">Original source</a>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
public @interface Property {

	/**
	 * O nome (chave) da propriedade desejada.
	 * 
	 * @return o nome da propriedade
	 */
	@Nonbinding
	String value();

	/**
	 * 
	 * @return
	 */
	@Nonbinding
	boolean watchChanges() default false;
}
