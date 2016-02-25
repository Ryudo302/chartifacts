package br.com.colbert.chartifacts.negocio.chartrun;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import br.com.colbert.chartifacts.dominio.chart.*;

/**
 * Estratégia para identificar a maior estreia dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
@ApplicationScoped
public class MaiorEstreiaChartRunStrategy implements MaiorVariacaoPosicaoEspecificaChartRunStrategy, Serializable {

	private static final long serialVersionUID = -2105161726188090363L;

	@Override
	public TipoVariacaoPosicao getTipoVariacao() {
		return TipoVariacaoPosicao.ESTREIA;
	}

	@Override
	public Optional<VariacaoPosicao> identificar(ChartRun chartRun, PosicaoChart posicaoEspecifica) {
		PosicaoChart posicaoEstreia = Objects.requireNonNull(chartRun, "Chart-run").getElementos().get(0);
		return posicaoEspecifica == null || posicaoEspecifica != null && posicaoEstreia.equals(posicaoEspecifica)
				? Optional.of(VariacaoPosicaoBuilder.estreia().em(posicaoEstreia).build()) : Optional.empty();
	}
}
