package br.com.colbert.chartifacts.infraestrutura.properties;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * Classe que permite a resolução de propriedades a partir de todos os arquivos de propriedades existentes dentro do projeto.
 *
 * @author Chris Ritchies
 * @author Thiago Colbert
 * @since 8 de mai de 2016
 * @see <a href="http://blog.chris-ritchie.com/2015/03/inject-external-properties-cdi-java-wildfly.html">Original source</a>
 */
@ApplicationScoped
public class PropertiesFilesResolver implements Serializable {

	private static final long serialVersionUID = 7955976705533061357L;

	/**
	 * Nome da propriedade de sistema referente aos arquivos de propriedades a serem processados.
	 */
	public static final String SYSTEM_PROPERTY_ARQUIVOS = "arquivos.properties";

	private static final String ARQUIVOS_SEPARATOR = ",";

	@Inject
	private transient Logger logger;

	private transient Map<String, String> properties;

	/**
	 * Cria uma nova instância.
	 */
	public PropertiesFilesResolver() {
		properties = new HashMap<>();
	}

	@PostConstruct
	private void init() {
		Optional<String> arquivosOptional = Optional.ofNullable(System.getProperty(SYSTEM_PROPERTY_ARQUIVOS));
		arquivosOptional.ifPresent(arquivos -> {
			Arrays.stream(arquivos.split(ARQUIVOS_SEPARATOR)).forEach(arquivo -> {
				logger.info("Processando arquivo: {}", arquivo);
				InputStream propertiesInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(arquivo);
				if (propertiesInputStream == null) {
					logger.warn("Arquivo não localizado: {}", arquivo);
				} else {
					try {
						Properties properties = new Properties();
						properties.load(propertiesInputStream);
						logger.debug("Propriedades carregadas: {}", properties);
						properties.entrySet().stream()
								.forEach(entry -> Optional.ofNullable(this.properties.put((String) entry.getKey(), (String) entry.getValue()))
										.ifPresent(valorAnterior -> logger.warn("Propriedade sendo sobrescrita: {} = {} -> {}", entry.getKey(),
												valorAnterior, entry.getValue())));
					} catch (IOException exception) {
						throw new RuntimeException("Erro ao ler arquivo de propriedades: " + arquivo, exception);
					}
				}
			});
		});

		if (properties.isEmpty()) {
			logger.warn("Nenhuma propriedade carregada");
		}
	}

	/**
	 * Obtém uma propriedade a partir de sua chave.
	 *
	 * @param key
	 *            a chave da propriedade desejada
	 * @return o valor da propriedade ({@code null} caso não exista)
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}
}
