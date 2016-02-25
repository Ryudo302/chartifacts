package br.com.colbert.chartifacts.negocio.chartrun;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;

/**
 * Uma variação de posições dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
public class VariacaoPosicao implements Comparable<VariacaoPosicao>, Serializable {

	private static final long serialVersionUID = 6501780629438286038L;

	private final PosicaoChart elementoA;
	private final PosicaoChart elementoB;
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
	public VariacaoPosicao(PosicaoChart elementoA, PosicaoChart elementoB, TipoVariacaoPosicao tipoVariacao) {
		this.elementoA = Objects.requireNonNull(elementoA, "Elemento A");
		this.elementoB = Objects.requireNonNull(elementoB, "Elemento B");
		this.tipoVariacao = Objects.requireNonNull(tipoVariacao, "Tipo de variação");

		if (elementoA.isAusencia() && elementoB.isAusencia()) {
			throw new IllegalArgumentException("Ambos elementos não podem ser ausências ao mesmo tempo");
		}
	}

	public PosicaoChart getElementoA() {
		return elementoA;
	}

	/**
	 * Obtém o número da posição do primeiro elemento.
	 * 
	 * @return o número da posição
	 */
	public Integer getNumeroPosicaoA() {
		return getElementoA().getNumeroPosicao();
	}

	public PosicaoChart getElementoB() {
		return elementoB;
	}

	/**
	 * Obtém o número da posição do segundo elemento.
	 * 
	 * @return o número da posição
	 */
	public Integer getNumeroPosicaoB() {
		return getElementoB().getNumeroPosicao();
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
