package br.com.colbert.chartifacts.dominio.chartrun;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.*;

/**
 * Uma variação de posições dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
public class VariacaoPosicao implements Comparable<VariacaoPosicao>, Serializable {

	private static final long serialVersionUID = 6501780629438286038L;

	private final ElementoChartRun elementoA;
	private final ElementoChartRun elementoB;
	private final TipoVariacaoPosicao tipoVariacao;

	/**
	 * Cria uma nova variação de posição com os elementos e tipo informados.
	 *
	 * @param elementoA
	 *            o primeiro elemento
	 * @param elementoB
	 *            o segundo elemento
	 * @param tipoVariacao
	 *            tipo de variação
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso sejam informadas duas ausências
	 */
	public VariacaoPosicao(ElementoChartRun elementoA, ElementoChartRun elementoB, TipoVariacaoPosicao tipoVariacao) {
		this.elementoA = Objects.requireNonNull(elementoA, "Elemento A");
		this.elementoB = Objects.requireNonNull(elementoB, "Elemento B");
		this.tipoVariacao = Objects.requireNonNull(tipoVariacao, "Tipo de variação");

		if (elementoA.isAusencia() && elementoB.isAusencia()) {
			throw new IllegalArgumentException("Ambos elementos não podem ser ausências ao mesmo tempo");
		}
	}

	public ElementoChartRun getElementoA() {
		return elementoA;
	}

	public ElementoChartRun getElementoB() {
		return elementoB;
	}

	public TipoVariacaoPosicao getTipoVariacao() {
		return tipoVariacao;
	}

	/**
	 * Obtém o valor referente à variação de posição.
	 *
	 * @return o valor da variação
	 */
	public int getValorVariacao() {
		return elementoA.compareTo(elementoB);
	}

	@Override
	public int compareTo(VariacaoPosicao other) {
		return getValorVariacao() - other.getValorVariacao();
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
		return tipoVariacao.toString() + '[' + elementoA + '-' + elementoB + ']';
	}
}