package br.com.colbert.chartifacts.dominio.chart;

import java.util.*;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.*;

import br.com.colbert.base.dominio.AbstractEntidade;

/**
 * Um elemento de <em>chart</em> é um objeto podendo indicar tanto uma posição de uma canção dentro de uma parada em um determinado segmento de
 * período de tempo quanto uma "posição fora" da parada.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class PosicaoChart extends AbstractEntidade implements Comparable<PosicaoChart> {

	/**
	 * Número de posição indicando ausência da canção na parada.
	 */
	public static final int NUMERO_POSICAO_AUSENCIA = 0;

	/**
	 * Instância única do elemento que representa a ausência da canção dentro da parada.
	 */
	public static final PosicaoChart AUSENCIA = new PosicaoChart(NUMERO_POSICAO_AUSENCIA);

	private static final long serialVersionUID = -927488184107569051L;

	private static final Map<Integer, PosicaoChart> cacheElementos = new HashMap<>();

	private final int numeroPosicao;

	private PosicaoChart(int numeroPosicao) {
		this.numeroPosicao = numeroPosicao;
	}

	/**
	 * <p>
	 * Obtém um elemento a partir de um número de posição.
	 * </p>
	 * <p>
	 * <strong>OBS.:</strong> para elementos representando ausência, utilizar {@link PosicaoChart#AUSENCIA}.
	 * </p>
	 *
	 * @param numeroPosicao
	 *            o número da posição
	 * @return elemento representando o número de posição informado
	 * @throws IllegalArgumentException
	 *             caso o número informado seja inválido (menor que 0)
	 */
	public static PosicaoChart valueOf(int numeroPosicao) {
		Validate.isTrue(numeroPosicao >= 0, "O número da posição deve ser >= 0");

		PosicaoChart elemento = cacheElementos.get(numeroPosicao);
		if (elemento == null) {
			elemento = new PosicaoChart(numeroPosicao);
			cacheElementos.put(numeroPosicao, elemento);
		}

		return elemento;
	}

	/**
	 * Método utilitário que tenta obter uma instância de {@link PosicaoChart} a partir do conteúdo da String informada.
	 * 
	 * @param numeroPosicao
	 *            String contendo o número da posição desejada
	 * @return elemento representando o número de posição informado
	 * @throws NumberFormatException
	 *             caso o valor informado não seja um número
	 * @throws IllegalArgumentException
	 *             caso o número informado seja inválido (menor que 0)
	 */
	public static PosicaoChart valueOf(String numeroPosicao) {
		return valueOf(Integer.parseInt(numeroPosicao));
	}

	/**
	 * Obtém o número da posição da parada musical que o elemento representa. O valor {@value #NUMERO_POSICAO_AUSENCIA} indica ausência da parada.
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

	/**
	 * Retorna a posição cujo número corresponda ao resultado da soma do número desta posição com o valor informado. Para subtrações, informar um
	 * valor negativo.
	 * 
	 * @param value
	 *            valor a ser subtraído do número desta posição
	 * @return instância cujo número corresponde ao resultado da operação
	 * @throws IllegalArgumentException
	 *             caso o resultado de {@link #getNumeroPosicao()} + {@code value} seja < 0
	 */
	public PosicaoChart sum(int value) {
		int resultado = numeroPosicao + value;
		Validate.isTrue(resultado >= 0, "O resultado da operação é um valor negativo!");
		return valueOf(resultado);
	}

	@Override
	public int compareTo(PosicaoChart other) {
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
