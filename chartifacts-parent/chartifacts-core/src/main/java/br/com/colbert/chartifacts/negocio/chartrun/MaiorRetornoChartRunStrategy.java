package br.com.colbert.chartifacts.negocio.chartrun;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import br.com.colbert.chartifacts.dominio.chart.*;

/**
 * Estratégia para identificar o maior retorno dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
@ApplicationScoped
public class MaiorRetornoChartRunStrategy implements MaiorVariacaoPosicaoEspecificaChartRunStrategy, Serializable {

	private static final long serialVersionUID = -2105161726188090363L;

	@Override
	public TipoVariacaoPosicao getTipoVariacao() {
		return TipoVariacaoPosicao.RETORNO;
	}

	@Override
	public Optional<VariacaoPosicao> identificar(ChartRun chartRun, PosicaoChart posicaoEspecifica) {
		List<PosicaoChart> elementos = Objects.requireNonNull(chartRun, "Chart-run").getElementos();

		PosicaoChart elementoAnterior = elementos.get(0);
		PosicaoChart maiorRetorno = null;
		for (PosicaoChart elementoAtual : elementos) {
			if (isRetorno(elementoAnterior, elementoAtual) && elementoAtual.compareTo(maiorRetorno) < 0) {
				maiorRetorno = elementoAtual;
			}

			elementoAnterior = elementoAtual;
		}

		return maiorRetorno != null && (posicaoEspecifica == null || posicaoEspecifica != null && posicaoEspecifica.equals(maiorRetorno))
				? Optional.of(VariacaoPosicaoBuilder.retorno().em(maiorRetorno).build()) : Optional.empty();
	}

	private boolean isRetorno(PosicaoChart elementoAnterior, PosicaoChart elementoAtual) {
		return elementoAnterior.isAusencia() && elementoAtual.isPresenca();
	}
}
