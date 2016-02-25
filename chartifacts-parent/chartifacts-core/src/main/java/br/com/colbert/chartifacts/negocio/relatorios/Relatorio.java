package br.com.colbert.chartifacts.negocio.relatorios;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.infraestrutura.collections.MapUtils;

/**
 * Um relatório gerado a partir de um histórico de parada musical.
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 *
 * @param <T>
 *            o tipo de objeto listado no relatório
 * @param <V>
 *            o tipo de valor listado no relatório
 */
public class Relatorio<T extends Entidade, V extends Comparable<V>> implements Serializable {

	private static final long serialVersionUID = 3328515496787777077L;

	private final Map<T, V> itens;

	private Comparator<V> comparator;

	/**
	 * Cria um novo relatório com os itens informados e utilizando um <em>comparator</em> pesonalizado.
	 *
	 * @param itens
	 *            os itens do relatório
	 * @param comparator
	 *            a ser utilizado na ordenação dos itens
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code> como itens
	 * @throws IllegalArgumentException
	 *             caso a lista de itens informada esteja vazia
	 */
	public Relatorio(Map<T, V> itens, Comparator<V> comparator) {
		this.comparator = comparator;
		this.itens = MapUtils.sortByValue(validar(itens), comparator);
	}

	/**
	 * Cria um novo relatório com os itens informados.
	 *
	 * @param itens
	 *            os itens do relatório
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso a lista informada esteja vazia
	 */
	public Relatorio(Map<T, V> itens) {
		this(itens, null);
	}

	private Map<T, V> validar(Map<T, V> itens) {
		if (MapUtils.isEmpty(Objects.requireNonNull(itens, "Itens"))) {
			throw new IllegalArgumentException("O relatório não pode ser criado vazio");
		}

		return itens;
	}

	/**
	 * Limita o tamanho do relatório, descartando os itens que sobrarem, caso o tamanho seja superior ao limite informado.
	 *
	 * @param tamanho
	 *            o tamanho limite do relatório. <code>null</code> indica que não há limite.
	 * @return o relatório após a limitação de tamanho
	 */
	public Relatorio<T, V> limitarTamanho(Integer tamanho) {
		if (tamanho != null) {
			Map<T, V> itensLimitados = new HashMap<>(tamanho);

			if (size() > tamanho) {
				Iterator<Entry<T, V>> entryIterator = itens.entrySet().iterator();
				for (int i = 0; i < tamanho; i++) {
					Entry<T, V> entry = entryIterator.next();
					itensLimitados.put(entry.getKey(), entry.getValue());
				}
			}

			itens.clear();
			itens.putAll(MapUtils.sortByValue(itensLimitados, comparator));
		}

		return this;
	}

	/**
	 * Obtém o conteúdo do relatório.
	 *
	 * @return os itens representando o conteúdo do relatório
	 */
	public Map<T, V> getItens() {
		return Collections.unmodifiableMap(itens);
	}

	/**
	 * Obtém o tamanho do relatório, ou seja, a quantidade de itens que ele possui.
	 *
	 * @return o tamanho do relatório
	 */
	public int size() {
		return itens.size();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
