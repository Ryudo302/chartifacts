package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.musica.Artista;

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
	private Logger logger;
	@Inject
	private StringParsersConfig parserConfig;

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
		logger.trace("Utilizando configurações: {}", parserConfig);

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
		Matcher matcher = parserConfig.nomeArtistaPattern().matcher(linha);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Padrão para artistas ('" + parserConfig.nomeArtistaPattern() + "') não encontrado: " + linha);
		}

		return Stream.of(parserConfig.separadoresArtistasPattern().split(StringUtils.defaultString(matcher.group(1), matcher.group(2))));
	}

	private String removerCaracteresEstranhosNomeArtista(String nomeArtista) {
		return StringUtils.trim(nomeArtista).replaceAll(parserConfig.separadorArtistaCancaoPattern().pattern(), StringUtils.EMPTY).replaceAll("]",
				StringUtils.EMPTY);
	}
}
