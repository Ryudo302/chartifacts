package br.com.colbert.chartifacts.dominio.chartrun.analyze;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

import br.com.colbert.chartifacts.dominio.musica.*;

/**
 * Uma ocorrência de um artista que substituiu a si próprio no topo de uma parada com canções distintas.
 * 
 * @author Thiago Miranda
 * @since 16 de fev de 2016
 */
public class ArtistaAutoSubstituicaoTopo implements Comparable<ArtistaAutoSubstituicaoTopo>, Serializable {

	private static final long serialVersionUID = 2326655395880151593L;

	private final Cancao primeiraCancao;
	private final Cancao segundaCancao;
	private final Calendar data;

	/**
	 * Cria uma nova ocorrência informando todos os parâmetros.
	 * 
	 * @param primeiraCancao
	 *            a canção que estava no topo inicialmente
	 * @param segundaCancao
	 *            a canção que tirou a anterior do topo
	 * @param data
	 *            data da ocorrência
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso ambas as canções não possuam um mesmo artista em comum
	 */
	public ArtistaAutoSubstituicaoTopo(Cancao primeiraCancao, Cancao segundaCancao, Calendar data) {
		this.primeiraCancao = Objects.requireNonNull(primeiraCancao, "primeiraCancao");
		this.segundaCancao = Objects.requireNonNull(segundaCancao, "segundaCancao");
		this.data = Objects.requireNonNull(data, "data");

		if (primeiraCancao.getArtistas().stream().filter(segundaCancao.getArtistas()::contains).collect(Collectors.counting()).intValue() == 0) {
			throw new IllegalArgumentException(
					"As canções não possuem nenhum artista em comum:" + StringUtils.LF + primeiraCancao + StringUtils.LF + segundaCancao);
		}
	}

	public Artista getArtista() {
		return primeiraCancao.getArtistaPrincipal().get();
	}

	public Cancao getPrimeiraCancao() {
		return primeiraCancao;
	}

	public Cancao getSegundaCancao() {
		return segundaCancao;
	}

	public Calendar getData() {
		return data;
	}

	@Override
	public int compareTo(ArtistaAutoSubstituicaoTopo other) {
		return data.compareTo(other.data);
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
		return getArtista() + " — " + primeiraCancao.getTitulo() + " → " + segundaCancao.getTitulo();
	}
}
