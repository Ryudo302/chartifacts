package br.com.colbert.chartifacts.infraestrutura.io;

/**
 * Exceção lançada quando um <em>chart-run</em> inválido é identificado.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
public class ChartRunInvalidoException extends RuntimeException {

	private static final long serialVersionUID = -370631047393824607L;

	private final int limiteMaiorPosicao;
	private final String chartRun;

	/**
	 * Cria uma nova exceção indicando o <em>chart-run</em> faltoso e qual o limite definido para valor de posição.
	 *
	 * @param chartRun
	 * @param limiteMaiorPosicao
	 */
	public ChartRunInvalidoException(String chartRun, int limiteMaiorPosicao) {
		super("Possui uma ou mais posições maiores que o limite definido de " + limiteMaiorPosicao + ": " + chartRun);
		this.chartRun = chartRun;
		this.limiteMaiorPosicao = limiteMaiorPosicao;
	}

	public int getLimiteMaiorPosicao() {
		return limiteMaiorPosicao;
	}

	public String getChartRun() {
		return chartRun;
	}
}
