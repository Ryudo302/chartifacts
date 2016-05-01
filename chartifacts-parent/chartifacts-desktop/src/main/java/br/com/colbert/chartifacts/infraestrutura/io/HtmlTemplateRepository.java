package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.*;
import java.text.MessageFormat;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Repositórios de arquivos <em>template</em> em HTML.
 * 
 * @author Thiago Miranda
 * @since 16 de fev de 2016
 */
@ApplicationScoped
public class HtmlTemplateRepository implements Serializable {

	private static final long serialVersionUID = 6440623918570227089L;

	private static final String DIRETORIO_ARQUIVOS = "html";

	@Inject
	private transient Logger logger;

	/**
	 * Carrega um template com o nome informado.
	 * 
	 * @param nomeArquivo
	 *            nome do arquivo de template
	 * @param argumentos
	 *            parâmetros a serem utilizados como substituição dentro do template
	 * @return o conteúdo do template carregado
	 * @throws FileNotFoundException
	 *             caso não seja encontrado o arquivo informado
	 * @throws IOException
	 *             caso ocorra algum erro de I/O ao carregar o arquivo
	 */
	public Optional<String> carregarTemplate(String nomeArquivo, Object... argumentos) throws IOException {
		logger.debug("Carregando arquivo '{}'. Argumentos: {}", nomeArquivo, argumentos);

		InputStream templateStream = getClass().getClassLoader().getResourceAsStream(buildCaminhoArquivo(nomeArquivo));
		if (templateStream != null) {
			String template = IOUtils.toString(templateStream);
			logger.debug("Arquivo carregado:\n{}", template);
			return Optional.of(MessageFormat.format(template, argumentos));
		} else {
			throw new FileNotFoundException(nomeArquivo);
		}
	}

	private String buildCaminhoArquivo(String nomeArquivo) {
		return DIRETORIO_ARQUIVOS + '/' + nomeArquivo;
	}
}
