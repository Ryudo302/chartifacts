package br.com.colbert.chartifacts.dominio.chartrun.analyze;

import br.com.colbert.chartifacts.dominio.chartrun.*;

/**
 * Estratégia para identificar a maior ocorrência de um tipo de variação em um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
public interface MaiorVariacaoChartRunStrategy extends ChartRunStrategy<VariacaoPosicao> {

	/**
	 * Obtém o tipo de variação de posição que é identificado.
	 *
	 * @return o tipo de variação
	 */
	TipoVariacaoPosicao getTipoVariacao();
}
