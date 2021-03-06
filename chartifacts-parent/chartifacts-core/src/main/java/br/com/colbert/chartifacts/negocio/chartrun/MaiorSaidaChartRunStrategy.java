package br.com.colbert.chartifacts.negocio.chartrun;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import br.com.colbert.chartifacts.dominio.chart.*;

/**
 * Estratégia para identificar a maior saída dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
@ApplicationScoped
public class MaiorSaidaChartRunStrategy implements MaiorVariacaoPosicaoEspecificaChartRunStrategy, Serializable {

	private static final long serialVersionUID = -2105161726188090363L;

	@Override
	public TipoVariacaoPosicao getTipoVariacao() {
		return TipoVariacaoPosicao.SAIDA;
	}

	@Override
	public Optional<VariacaoPosicao> identificar(ChartRun chartRun, PosicaoChart posicaoEspecifica) {
		List<PosicaoChart> elementos = new ArrayList<>(Objects.requireNonNull(chartRun, "Chart-run").getElementos());
		// adiciona uma ausência ao final para que a última posição seja considerada uma saída
		elementos.add(PosicaoChart.AUSENCIA);

		PosicaoChart elementoAnterior = elementos.get(0);
		PosicaoChart maiorSaida = null;
		for (PosicaoChart elementoAtual : elementos) {
			if (isSaida(elementoAnterior, elementoAtual) && elementoAnterior.compareTo(maiorSaida) < 0) {
				maiorSaida = elementoAnterior;
			}

			elementoAnterior = elementoAtual;
		}

		return maiorSaida != null && (posicaoEspecifica == null || posicaoEspecifica != null && posicaoEspecifica.equals(maiorSaida))
				? Optional.of(VariacaoPosicaoBuilder.saida().em(maiorSaida).build()) : Optional.empty();
	}

	private boolean isSaida(PosicaoChart elementoAnterior, PosicaoChart elementoAtual) {
		return elementoAnterior.isPresenca() && elementoAtual.isAusencia();
	}
}
