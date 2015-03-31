package br.com.colbert.chartifacts.dominio.chart;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.*;
import org.slf4j.*;

import br.com.colbert.chartifacts.dominio.chartrun.*;

/**
 * Permite o cálculo de pontuacao de uma canção a partir de seu <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class CalculadoraPontos implements Serializable {

	private static final long serialVersionUID = -7767670086381062079L;
	private static final Logger logger = LoggerFactory.getLogger(CalculadoraPontos.class);

	private final ElementoChartRun maiorElemento;

	private double pontuacao;

	/**
	 *
	 * @param maiorElemento
	 */
	public CalculadoraPontos(ElementoChartRun maiorElemento) {
		if (Objects.requireNonNull(maiorElemento, "Maior elemento").isAusencia()) {
			throw new IllegalArgumentException("O maior elemento precisa ser uma posição: " + maiorElemento);
		}

		logger.debug("Definindo o maior elemento como sendo {}", maiorElemento);
		this.maiorElemento = maiorElemento;
	}

	public double calcularPontos(ChartRun chartRun) {
		logger.debug("Calculando pontuação a partir do chart-run: {}", chartRun);

		pontuacao = 0;
		int valorMaiorElemento = maiorElemento.getNumeroPosicao();
		Objects.requireNonNull(chartRun, "Chart-run").forEachElemento(elemento -> {
			int valorElementoAtual = elemento.getNumeroPosicao();
			if (valorElementoAtual > 0) {
				pontuacao += valorMaiorElemento - valorElementoAtual + 1;
			}
		});

		return pontuacao;
	}

	public ElementoChartRun getMaiorElemento() {
		return maiorElemento;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("maiorElemento", maiorElemento).toString();
	}
}