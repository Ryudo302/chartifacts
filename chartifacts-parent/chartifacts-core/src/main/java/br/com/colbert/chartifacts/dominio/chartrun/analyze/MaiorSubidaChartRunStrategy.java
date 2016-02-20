package br.com.colbert.chartifacts.dominio.chartrun.analyze;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import br.com.colbert.chartifacts.dominio.chartrun.*;

/**
 * Estratégia para identificar a maior subida de posições dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
@ApplicationScoped
public class MaiorSubidaChartRunStrategy implements MaiorVariacaoChartRunStrategy, Serializable {

	private static final long serialVersionUID = -2105161726188090363L;

	@Override
	public TipoVariacaoPosicao getTipoVariacao() {
		return TipoVariacaoPosicao.SUBIDA;
	}

	@Override
	public Optional<VariacaoPosicao> identificar(ChartRun chartRun) {
		ElementoChartRun posicaoA = ElementoChartRun.AUSENCIA;
		ElementoChartRun posicaoB = ElementoChartRun.AUSENCIA;

		int maiorSubida = 0;
		List<ElementoChartRun> elementos = Objects.requireNonNull(chartRun, "Chart-run").getElementos();
		ElementoChartRun elementoAnterior = elementos.get(0);
		for (ElementoChartRun elementoAtual : elementos) {
			if (elementoAnterior.isPresenca() && elementoAtual.isPresenca()) {
				int variacao = elementoAnterior.compareTo(elementoAtual);
				if (variacao > maiorSubida) {
					posicaoA = elementoAnterior;
					posicaoB = elementoAtual;
					maiorSubida = variacao;
				}
			}

			elementoAnterior = elementoAtual;
		}

		if (posicaoA.isPresenca() && posicaoB.isPresenca()) {
			return Optional.of(VariacaoPosicaoBuilder.subida().de(posicaoA).para(posicaoB).build());
		} else {
			return Optional.empty();
		}
	}
}
