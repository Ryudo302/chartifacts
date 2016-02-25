package br.com.colbert.chartifacts.negocio.chartrun;

import java.util.Optional;

import br.com.colbert.chartifacts.dominio.chart.ChartRun;

/**
 * Estratégia para identificar a maior ocorrência de um tipo de variação em um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
public interface MaiorVariacaoChartRunStrategy {

	/**
	 * Obtém o tipo de variação de posição que é identificado.
	 *
	 * @return o tipo de variação
	 */
	TipoVariacaoPosicao getTipoVariacao();

	/**
	 * Identifica uma ocorrência dentro do <em>chart-run</em> informado.
	 *
	 * @param chartRun
	 *            <em>chart-run</em> a ser analisado
	 * @return a maior variação de posição encontrada (vazio caso não exista)
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	Optional<VariacaoPosicao> identificar(ChartRun chartRun);
}
