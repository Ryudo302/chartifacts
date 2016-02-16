package br.com.colbert.chartifacts.dominio.musica;

import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.AbstractEntidade;

/**
 * Uma canção é uma composição musical criada para um ou mais artistas musicais.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class Cancao extends AbstractEntidade implements Comparable<Cancao> {

	private static final long serialVersionUID = 452297918051012638L;

	private final String titulo;
	private final Collection<String> titulosAlternativos;
	private final List<Artista> artistas;

	/**
	 * Cria uma nova canção com um título principal, títulos alternativos e um grupo de artistas.
	 *
	 * @param titulo
	 *            da canção
	 * @param titulosAlternativos
	 *            outros títulos relevantes da canção
	 * @param artistas
	 *            da canção
	 */
	public Cancao(String titulo, List<Artista> artistas, Collection<String> titulosAlternativos) {
		this.titulo = titulo;
		this.artistas = new ArrayList<>(artistas);
		this.titulosAlternativos = new ArrayList<>(titulosAlternativos);
	}

	/**
	 * Cria uma nova canção com um título principal, um título alternativo e um grupo de artistas.
	 *
	 * @param titulo
	 *            da canção
	 * @param artistas
	 *            da canção
	 * @param titulosAlternativos
	 *            outros títulos relevantes da canção
	 */
	public Cancao(String titulo, List<Artista> artistas, String... titulosAlternativos) {
		this(titulo, artistas, titulosAlternativos != null ? Arrays.asList(titulosAlternativos) : null);
	}

	public String getTitulo() {
		return titulo;
	}

	public Collection<String> getTitulosAlternativos() {
		return Collections.unmodifiableCollection(titulosAlternativos);
	}

	public List<Artista> getArtistas() {
		return Collections.unmodifiableList(artistas);
	}

	/**
	 * Obtém qual o artista principal da canção.
	 * 
	 * @return o artista principal, caso a canção possua artistas associados
	 */
	public Optional<Artista> getArtistaPrincipal() {
		return CollectionUtils.size(artistas) > 0 ? Optional.ofNullable(artistas.get(0)) : Optional.empty();
	}

	@Override
	public int compareTo(Cancao other) {
		return new CompareToBuilder().append(titulo, other.titulo).toComparison();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
