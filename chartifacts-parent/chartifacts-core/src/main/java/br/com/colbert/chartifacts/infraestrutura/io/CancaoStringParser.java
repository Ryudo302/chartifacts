package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.regex.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.infraestrutura.properties.Property;

/**
 * Permite a obtenção de instâncias de {@link Cancao} a partir dos dados presentes em uma {@link String}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@ApplicationScoped
public class CancaoStringParser implements Serializable {

	private static final long serialVersionUID = 7828111894518661714L;

	@Inject
	private transient Logger logger;

	@Inject
	private transient ArtistaStringParser artistaStringParser;

	@Inject
	@Property(ParserProperties.TITULO_CANCAO_KEY)
	private transient Pattern tituloCancaoPattern;
	@Inject
	@Property(ParserProperties.SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY)
	private transient Pattern titulosAlternativosCancaoSeparadorPattern;

	/**
	 * Cria uma nova {@link Cancao} a partir da {@link String} informada.
	 *
	 * @param texto
	 *            a ser analizado
	 * @return a instância de canção criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia
	 */
	public Cancao parse(String texto) {
		Validate.notBlank(texto);
		logger.trace("Analisando: {}", texto);

		String[] titulos = parseTitulosCancao(texto);
		logger.trace("Títulos identificados: {}", (Object) titulos);

		if (titulos.length == 1) {
			return new Cancao(titulos[0], artistaStringParser.parse(texto));
		} else {
			return new Cancao(titulos[0], artistaStringParser.parse(texto), ArrayUtils.subarray(titulos, 1, titulos.length));
		}
	}

	private String[] parseTitulosCancao(String linha) {
		Matcher matcher = tituloCancaoPattern.matcher(linha);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Padrão para canção ('" + tituloCancaoPattern + "') não encontrado: " + linha);
		}

		return titulosAlternativosCancaoSeparadorPattern.split(matcher.group(1));
	}
}
