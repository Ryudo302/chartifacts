package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.infraestrutura.properties.Property;

/**
 * Permite a obtenção de instâncias de {@link Artista} a partir dos dados presentes em uma {@link String}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@ApplicationScoped
public class ArtistaStringParser implements Serializable {

	private static final long serialVersionUID = -4631579773370668773L;

	@Inject
	private transient Logger logger;

	@Inject
	@Property(ParserProperties.NOME_ARTISTA_KEY)
	private transient Pattern nomeArtistaPattern;
	@Inject
	@Property(ParserProperties.SEPARADORES_ARTISTAS_KEY)
	private transient Pattern separadoresArtistasPattern;
	@Inject
	@Property(ParserProperties.SEPARADOR_ARTISTAS_E_CANCAO_KEY)
	private transient Pattern separadorArtistaCancaoPattern;

	/**
	 * Cria um novo {@link Artista} a partir da {@link String} informada.
	 *
	 * @param texto
	 *            a ser analizado
	 * @return a instância de canção criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia
	 */
	public List<Artista> parse(String texto) {
		Validate.notBlank(texto);
		logger.trace("Analisando: {}", texto);

		List<Artista> artistas = new ArrayList<>();
		parseNomesArtistas(texto).forEach(nomeArtista -> {
			nomeArtista = removerCaracteresEstranhosNomeArtista(nomeArtista);
			if (StringUtils.isNotBlank(nomeArtista)) {
				artistas.add(new Artista(nomeArtista));
			}
		});

		logger.trace("Artistas identificados: {}", artistas);
		return artistas;
	}

	private Stream<String> parseNomesArtistas(String linha) {
		Matcher matcher = nomeArtistaPattern.matcher(linha);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Padrão para artistas ('" + nomeArtistaPattern + "') não encontrado: " + linha);
		}

		return Stream.of(separadoresArtistasPattern
				.split(matcher.groupCount() > 1 ? StringUtils.defaultString(matcher.group(1), matcher.group(2)) : matcher.group(1)));
	}

	private String removerCaracteresEstranhosNomeArtista(String nomeArtista) {
		return StringUtils.trim(nomeArtista).replaceAll(separadorArtistaCancaoPattern.pattern(), StringUtils.EMPTY).replaceAll("]",
				StringUtils.EMPTY);
	}
}
