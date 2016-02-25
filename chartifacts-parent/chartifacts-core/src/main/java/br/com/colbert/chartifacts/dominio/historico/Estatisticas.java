package br.com.colbert.chartifacts.dominio.historico;

import org.apache.commons.lang3.builder.CompareToBuilder;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.chartifacts.negocio.chartrun.PermanenciaPosicao;

/**
 * Estatísticas de uma canção dentro de uma parada musical.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class Estatisticas extends AbstractEntidade implements Comparable<Estatisticas> {

	private static final long serialVersionUID = 8944862495237851542L;

	private final double pontuacao;
	private final int permanenciaTotal;
	private final PermanenciaPosicao melhorPosicao;

	/**
	 * Cria uma nova instância com todos os dados de estatísticas.
	 *
	 * @param pontuacao
	 *            a pontuação da canção
	 * @param permanenciaTotal
	 *            permanência total da canção na parada
	 * @param melhorPosicao
	 *            a melhor posição alcançada pela canção na parada
	 */
	Estatisticas(double pontuacao, int permanenciaTotal, PermanenciaPosicao melhorPosicao) {
		this.pontuacao = pontuacao;
		this.permanenciaTotal = permanenciaTotal;
		this.melhorPosicao = melhorPosicao;
	}

	public double getPontuacao() {
		return pontuacao;
	}

	public int getPermanenciaTotal() {
		return permanenciaTotal;
	}

	public PermanenciaPosicao getMelhorPosicao() {
		return melhorPosicao;
	}

	@Override
	public int compareTo(Estatisticas other) {
		return new CompareToBuilder().append(pontuacao, other.pontuacao).append(melhorPosicao, other.melhorPosicao)
				.append(permanenciaTotal, other.permanenciaTotal).toComparison();
	}
}
