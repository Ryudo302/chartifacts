package br.com.colbert.chartifacts.dominio.historico;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.*;
import org.slf4j.*;

import br.com.colbert.chartifacts.dominio.chart.*;

/**
 * Permite o cálculo de pontuacao de uma canção a partir de seu <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class CalculadoraPontos implements Serializable {

	private static final long serialVersionUID = -7767670086381062079L;
	private static final Logger logger = LoggerFactory.getLogger(CalculadoraPontos.class);

	private final PosicaoChart maiorElemento;

	private double pontuacao;

	/**
	 * TODO Javadoc
	 *
	 * @param maiorElemento
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @see #CalculadoraPontos(Integer)
	 */
	public CalculadoraPontos(PosicaoChart maiorElemento) {
		if (Objects.requireNonNull(maiorElemento, "Maior elemento").isAusencia()) {
			throw new IllegalArgumentException("O maior elemento precisa ser uma posição: " + maiorElemento);
		}

		logger.debug("Definindo o maior elemento como sendo {}", maiorElemento);
		this.maiorElemento = maiorElemento;
	}

	/**
	 * TODO Javadoc
	 * 
	 * @param maiorPosicao
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @see PosicaoChart#valueOf(int)
	 */
	public CalculadoraPontos(int maiorPosicao) {
		this(PosicaoChart.valueOf(maiorPosicao));
	}

	/**
	 * Calcula a pontuação total referente ao <em>chart-run</em> informado.
	 * 
	 * @param chartRun
	 * @return a pontuação calculada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
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

	/**
	 * Calcula a pontuação a partir de uma única posição.
	 * 
	 * @param posicao
	 *            a posição a ser utilizada
	 * @return a pontuação calculada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public double calcularPontos(PosicaoChart posicao) {
		pontuacao = 0;

		int valorMaiorElemento = maiorElemento.getNumeroPosicao();
		int numeroPosicao = Objects.requireNonNull(posicao, "posicao").getNumeroPosicao();
		if (numeroPosicao > 0) {
			pontuacao += valorMaiorElemento - numeroPosicao + 1;
		}

		return pontuacao;
	}

	public PosicaoChart getMaiorElemento() {
		return maiorElemento;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("maiorElemento", maiorElemento).toString();
	}
}
