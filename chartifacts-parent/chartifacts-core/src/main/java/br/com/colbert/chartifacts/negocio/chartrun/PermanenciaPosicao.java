package br.com.colbert.chartifacts.negocio.chartrun;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.CompareToBuilder;

import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;

/**
 * Uma permanência em uma mesma posição dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class PermanenciaPosicao implements Comparable<PermanenciaPosicao>, Serializable {

	private static final long serialVersionUID = 1423027728419665501L;

	private final ElementoChartRun posicao;
	private final int quantidade;

	/**
	 * Cria uma nova permanência.
	 *
	 * @param posicao
	 *            a posição
	 * @param quantidade
	 *            a permanência total na posição
	 * @throws NullPointerException
	 *             caso a posição informada seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma quantidade inválida (menor ou igual a 0)
	 */
	public PermanenciaPosicao(ElementoChartRun posicao, int quantidade) {
		this.posicao = Objects.requireNonNull(posicao, "Posição");

		Validate.isTrue(quantidade > 0, "A quantidade deve ser um valor maior que zero");
		this.quantidade = quantidade;
	}

	public ElementoChartRun getPosicao() {
		return posicao;
	}

	/**
	 * Obtém o número da posição.
	 * 
	 * @return o número da posição
	 */
	public Integer getNumeroPosicao() {
		return getPosicao().getNumeroPosicao();
	}

	public int getQuantidade() {
		return quantidade;
	}

	@Override
	public int compareTo(PermanenciaPosicao other) {
		return CompareToBuilder.reflectionCompare(this, other);
	}

	@Override
	public String toString() {
		return posicao.toString() + StringUtils.SPACE + '(' + quantidade + 'x' + ')';
	}
}
