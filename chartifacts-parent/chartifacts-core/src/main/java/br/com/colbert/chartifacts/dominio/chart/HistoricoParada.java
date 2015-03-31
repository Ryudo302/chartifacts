package br.com.colbert.chartifacts.dominio.chart;

import java.util.*;

import br.com.colbert.base.dominio.AbstractEntidade;

/**
 * O histórico de uma parada musical, contendo o <em>chart-run</em> e outras informações de todas as músicas que passaram pela
 * parada. É a partir desse arquivo que todas as informações necessárias para a geração de relatórios são extraídas.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class HistoricoParada extends AbstractEntidade {

	private static final long serialVersionUID = -8337988123075582874L;

	private final Collection<ItemHistoricoParada> itens;

	/**
	 * Cria um novo histórico contendo os itens informados.
	 *
	 * @param itens
	 *            do histórico
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso a coleção informada esteja vazia
	 */
	public HistoricoParada(Collection<ItemHistoricoParada> itens) {
		if (Objects.requireNonNull(itens, "itens").isEmpty()) {
			throw new IllegalArgumentException("Um histórico não pode estar vazio");
		}

		this.itens = itens;
	}

	/**
	 * Obtém a quantidade de itens presente no histórico.
	 *
	 * @return a quantidade de itens
	 */
	public int size() {
		return itens.size();
	}

	public Collection<ItemHistoricoParada> getItens() {
		return Collections.unmodifiableCollection(itens);
	}
}
