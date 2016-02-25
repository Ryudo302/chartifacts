package br.com.colbert.chartifacts.negocio.chartrun;

import java.util.Optional;

import br.com.colbert.chartifacts.dominio.chartrun.*;

/**
 * Extensão de {@link MaiorVariacaoChartRunStrategy} que permite restringir para uma posição específica.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
public interface MaiorVariacaoPosicaoEspecificaChartRunStrategy extends MaiorVariacaoChartRunStrategy {

	/**
	 * Identifica a maior ocorrência do tipo de variação para a posição dentro do <em>chart-run</em> informados.
	 *
	 * @param chartRun
	 *            <em>chart-run</em> a ser analisado
	 * @param posicaoEspecifica
	 *            restringe para uma posição específica (opcional)
	 * @return a maior variação de posição encontrada (vazio caso não exista)
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code> como <em>chart-run</em>
	 */
	Optional<VariacaoPosicao> identificar(ChartRun chartRun, ElementoChartRun posicaoEspecifica);

	@Override
	public default Optional<VariacaoPosicao> identificar(ChartRun chartRun) {
		return identificar(chartRun, null);
	}
}
