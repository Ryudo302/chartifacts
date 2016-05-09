package br.com.colbert.chartifacts.infraestrutura.properties;

import java.io.Serializable;
import java.util.regex.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

/**
 * Provê instâncias de String referentes a propriedades carregadas em memória.
 * 
 * @author Chris Ritchies
 * @author Thiago Colbert
 * @since 8 de mai de 2016
 * @see <a href="http://blog.chris-ritchie.com/2015/03/inject-external-properties-cdi-java-wildfly.html">Original source</a>
 */
@ApplicationScoped
public class PropertyProducer implements Serializable {

	private static final long serialVersionUID = -4030735285483785604L;

	@Inject
	private transient PropertiesFilesResolver propertiesFilesResolver;

	/**
	 * Obtém o valor de uma propriedade.
	 * 
	 * @param injectionPoint
	 * @return o valor da propriedade
	 * @throws IllegalArgumentException
	 *             caso a propriedade não exista
	 */
	@Produces
	@Property("")
	public String getPropertyAsString(InjectionPoint injectionPoint) {
		Property propertyAnnotation = injectionPoint.getAnnotated().getAnnotation(Property.class);

		String propertyName = propertyAnnotation.value();
		String value = propertiesFilesResolver.getProperty(propertyName);

		if (value == null || StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("No property found with name '" + propertyName + "'");
		}

		return value;
	}

	/**
	 * Obtém o valor de uma propriedade na forma de um {@link Pattern}.
	 * 
	 * @param injectionPoint
	 * @return o valor da propriedade como um {@link Pattern}
	 * @throws IllegalArgumentException
	 *             caso a propriedade não exista
	 * @throws PatternSyntaxException
	 *             caso o valor da propriedade não seja um regex válido
	 */
	@Produces
	@Property("")
	public Pattern getPropertyAsPattern(InjectionPoint injectionPoint) {
		String value = getPropertyAsString(injectionPoint);
		return StringUtils.isBlank(value) ? null : Pattern.compile(value);
	}

	/**
	 * Obtém o valor numérico de uma propriedade.
	 * 
	 * @param injectionPoint
	 * @return o valor da propriedade
	 * @throws IllegalArgumentException
	 *             caso a propriedade não exista
	 * @throws NumberFormatException
	 *             caso a propriedade não possua um valor numérico
	 */
	@Produces
	@Property("")
	public Integer getPropertyAsInteger(InjectionPoint injectionPoint) {
		String value = getPropertyAsString(injectionPoint);
		return value == null ? null : Integer.valueOf(value);
	}
}
