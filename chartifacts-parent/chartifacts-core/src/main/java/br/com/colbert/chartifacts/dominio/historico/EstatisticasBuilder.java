package br.com.colbert.chartifacts.dominio.historico;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;
import org.slf4j.*;

import br.com.colbert.chartifacts.dominio.chartrun.*;
import br.com.colbert.chartifacts.negocio.chartrun.PermanenciaPosicao;

/**
 * Classe que facilita a criação de instâncias de {@link Estatisticas}.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class EstatisticasBuilder implements Builder<Estatisticas>, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(EstatisticasBuilder.class);

	private static final long serialVersionUID = 6270601220111843476L;

	private final CalculadoraPontos calculadoraPontos;

	private double pontuacao;
	private int permanenciaTotal;
	private int melhorPosicao;
	private int permanenciaMelhorPosicao;

	private EstatisticasBuilder(CalculadoraPontos calculadoraPontos) {
		logger.debug("Utilizando calculadora: {}", calculadoraPontos);
		this.calculadoraPontos = calculadoraPontos;
	}

	/**
	 * Inicia a criação de uma instância, definindo a calculadora de pontos a ser utilizada.
	 *
	 * @param calculadoraPontos
	 *            a calculadora de pontos
	 * @return uma instância do <em>builder</em>
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public static EstatisticasBuilder utilizando(CalculadoraPontos calculadoraPontos) {
		return new EstatisticasBuilder(Objects.requireNonNull(calculadoraPontos, "Calculadora de pontos"));
	}

	/**
	 * Obtém as informações a partir de um <em>chart-run</em>.
	 *
	 * @param chartRun
	 *            o <em>chart-run</em> a ser utilizado
	 * @return <code>this</code>, para chamadas encadeadas
	 */
	public EstatisticasBuilder aPartirDo(ChartRun chartRun) {
		melhorPosicao = Integer.MAX_VALUE;
		permanenciaMelhorPosicao = 1;

		logger.debug("Identificando permanência total, melhor posição e permanência");
		chartRun.forEachElemento(elemento -> {
			if (elemento.isPresenca()) {
				permanenciaTotal++;

				Integer valorElementoAtual = elemento.getNumeroPosicao();
				if (valorElementoAtual < melhorPosicao) {
					melhorPosicao = valorElementoAtual;
					permanenciaMelhorPosicao = 1;
				} else if (valorElementoAtual == melhorPosicao) {
					permanenciaMelhorPosicao++;
				}
			}
		});

		pontuacao = calculadoraPontos.calcularPontos(chartRun);
		logger.debug("Pontuação: {}", pontuacao);

		return this;
	}

	@Override
	public Estatisticas build() {
		return new Estatisticas(pontuacao, permanenciaTotal,
				new PermanenciaPosicao(ElementoChartRun.valueOf(melhorPosicao), permanenciaMelhorPosicao));
	}
}
