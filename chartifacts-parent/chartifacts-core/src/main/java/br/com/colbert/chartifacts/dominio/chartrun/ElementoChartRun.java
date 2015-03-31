package br.com.colbert.chartifacts.dominio.chartrun;

import java.util.*;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.AbstractEntidade;

/**
 * Um elemento de <em>chart-run</em> é um objeto podendo indicar tanto uma posição de uma canção dentro de uma parada em um
 * determinado segmento de período de tempo quanto uma "posição fora" da parada.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class ElementoChartRun extends AbstractEntidade implements Comparable<ElementoChartRun> {

	/**
	 * Número de posição indicando ausência da canção na parada.
	 */
	public static final int NUMERO_POSICAO_AUSENCIA = 0;

	/**
	 * Instância única do elemento que representa a ausência da canção dentro da parada.
	 */
	public static final ElementoChartRun AUSENCIA = new ElementoChartRun(NUMERO_POSICAO_AUSENCIA);

	private static final long serialVersionUID = -927488184107569051L;

	private static final Map<Integer, ElementoChartRun> cacheElementos = new HashMap<>();

	private final int numeroPosicao;

	private ElementoChartRun(int numeroPosicao) {
		this.numeroPosicao = numeroPosicao;
	}

	/**
	 * <p>
	 * Obtém um elemento a partir de um número de posição.
	 * </p>
	 * <p>
	 * <strong>OBS.:</strong> para elementos representando ausência, utilizar {@link ElementoChartRun#AUSENCIA}.
	 * </p>
	 *
	 * @param numeroPosicao
	 *            o número da posição
	 * @return elemento representando o número de posição informado
	 * @throws IllegalArgumentException
	 *             caso o número informado seja inválido (menor ou igual a 0)
	 */
	public static ElementoChartRun valueOf(int numeroPosicao) {
		Validate.isTrue(numeroPosicao > 0, "O número da posição deve ser > 0");

		ElementoChartRun elemento = cacheElementos.get(numeroPosicao);
		if (elemento == null) {
			elemento = new ElementoChartRun(numeroPosicao);
			cacheElementos.put(numeroPosicao, elemento);
		}

		return elemento;
	}

	/**
	 * Obtém o número da posição da parada musical que o elemento representa. O valor {@value #NUMERO_POSICAO_AUSENCIA} indica
	 * ausência da parada.
	 *
	 * @return o número da posição
	 */
	public int getNumeroPosicao() {
		return numeroPosicao;
	}

	/**
	 * Verifica se o elemento representa uma ausência de canção na parada.
	 *
	 * @return <code>true</code> caso represente uma ausência, <code>false</code> caso contrário
	 * @see #isPresenca()
	 */
	public boolean isAusencia() {
		return numeroPosicao == NUMERO_POSICAO_AUSENCIA;
	}

	/**
	 * Verifica se o elemento representa uma presença da canção na parada (ou seja, é um número de posição).
	 *
	 * @return <code>true</code> caso represente uma presença, <code>false</code> caso contrário
	 * @see #isAusencia()
	 */
	public boolean isPresenca() {
		return !isAusencia();
	}

	@Override
	public int compareTo(ElementoChartRun other) {
		return other != null ? numeroPosicao - other.numeroPosicao : -1;
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
		return String.valueOf(numeroPosicao);
	}
}
