package br.com.colbert.chartifacts.dominio.musica;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.AbstractEntidade;

/**
 * Um artista é um indíviduo ou grupo dentro do ramo musical. É alguém que encabeça trabalhos musicais como canções.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class Artista extends AbstractEntidade implements Comparable<Artista> {

	private static final long serialVersionUID = 6082202432730881896L;

	private final String nome;

	/**
	 * Cria um novo artista com um nome.
	 *
	 * @param nome
	 *            do artista
	 */
	public Artista(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int compareTo(Artista other) {
		return CompareToBuilder.reflectionCompare(this, other);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return getNome();
	}
}
