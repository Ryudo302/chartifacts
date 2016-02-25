package br.com.colbert.chartifacts.dominio.chart;

import java.util.*;
import java.util.function.Consumer;

import org.jboss.weld.exceptions.IllegalArgumentException;

import br.com.colbert.base.dominio.AbstractEntidade;

/**
 * Um <em>chart-run</em> é uma descrição da situação da canção em cada segmento do período em que ela esteve dentro da parada musical. Essa situação
 * pode ser tanto uma posição dentro da parada quanto uma "posição fora", que ocorre quando uma música entra na parada, sai e posteriormente retorna.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class ChartRun extends AbstractEntidade {

	private static final long serialVersionUID = -623585849046907048L;

	private final List<PosicaoChart> elementos;

	/**
	 * Cria um novo <em>chart-run</em> com os elementos informados.
	 *
	 * @param elementos
	 *            do <em>chart-run</em>
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso a lista informada esteja vazia
	 */
	public ChartRun(List<PosicaoChart> elementos) {
		if (Objects.requireNonNull(elementos, "elementos").isEmpty()) {
			throw new IllegalArgumentException("Um chart-run não pode estar vazio");
		}

		this.elementos = new ArrayList<>(elementos);
	}

	/**
	 * Método utilitário para criar um <em>chart-run</em> a partir dos elementos informados.
	 *
	 * @param elementos
	 *            do <em>chart-run</em>
	 * @return a instância criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informado um array vazio
	 */
	public static ChartRun novo(PosicaoChart... elementos) {
		return new ChartRun(Arrays.asList(elementos));
	}

	/**
	 * Método utilitário para criar um <em>chart-run</em> a partir dos valores de posições informados.
	 *
	 * @param elementos
	 *            do <em>chart-run</em>
	 * @return a instância criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informado um array vazio ou caso algum dos valores de posições seja inválido
	 * @see PosicaoChart#valueOf(int)
	 */
	public static ChartRun novo(Integer... posicoes) {
		return novo(Arrays.stream(posicoes).map(posicao -> PosicaoChart.valueOf(posicao)).toArray(PosicaoChart[]::new));
	}

	/**
	 * Executa uma ação sobre cada um dos elementos do <em>chart-run</em>.
	 *
	 * @param action
	 *            a ação a ser executada
	 */
	public void forEachElemento(Consumer<? super PosicaoChart> action) {
		elementos.forEach(action);
	}

	/**
	 * Obtém a quantidade de elementos dentro do <em>chart-run</em>.
	 *
	 * @return a quantidade de elementos
	 */
	public int size() {
		return elementos.size();
	}

	public List<PosicaoChart> getElementos() {
		return Collections.unmodifiableList(elementos);
	}

	@Override
	public String toString() {
		return elementos.toString();
	}
}
