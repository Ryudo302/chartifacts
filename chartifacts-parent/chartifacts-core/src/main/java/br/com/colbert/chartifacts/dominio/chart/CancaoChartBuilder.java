package br.com.colbert.chartifacts.dominio.chart;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;

import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.negocio.chartrun.TipoVariacaoPosicao;

/**
 * {@link Builder} de instâncias de {@link CancaoChart}.
 * 
 * @author ThiagoColbert
 * @since 25 de fev de 2016
 */
public class CancaoChartBuilder implements Builder<CancaoChart>, Serializable {

	private static final long serialVersionUID = 3362541391811849925L;

	private final CancaoChart cancaoChart;

	private CancaoChartBuilder(PosicaoChart posicao, Cancao cancao) {
		cancaoChart = new CancaoChart(posicao, cancao);
	}

	/**
	 * Inicia a criação de um {@link CancaoChart}, informando a posição e canção.
	 * 
	 * @param posicao
	 *            a posição
	 * @param cancao
	 *            a canção
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 */
	public static CancaoChartBuilder novo(PosicaoChart posicao, Cancao cancao) {
		return new CancaoChartBuilder(Objects.requireNonNull(posicao, "posicao"), Objects.requireNonNull(cancao, "cancao"));
	}

	/**
	 * Define que a canção está variando de posição dentro da parada musical.
	 * 
	 * @param tipoVariacaoPosicao
	 *            tipo de variação
	 * @param valorVariacao
	 *            o valor da variação - negativo para queda, positivo para subida ou zero para permanência
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public CancaoChartBuilder comVariacao(TipoVariacaoPosicao tipoVariacaoPosicao, int valorVariacao) {
		cancaoChart.setTipoVariacaoPosicao(tipoVariacaoPosicao);
		cancaoChart.setValorVariacaoPosicao(valorVariacao);
		return this;
	}

	/**
	 * Atualiza as estatísticas da canção utilizando a calculadora de pontos informada.
	 * 
	 * @param calculadoraPontos
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public CancaoChartBuilder atualizarEstatisticasUtilizando(CalculadoraPontos calculadoraPontos) {
		Objects.requireNonNull(calculadoraPontos, "calculadoraPontos");
		cancaoChart.setEstatisticas(EstatisticasBuilder.utilizando(calculadoraPontos).aPartirDaPosicao(cancaoChart.getPosicao()).build());
		return this;
	}

	/**
	 * Atualiza a pontuação das estatísticas da canção utilizando a calculadora de pontos informada.
	 * 
	 * @param calculadoraPontos
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public CancaoChartBuilder atualizarPontuacaoUtilizando(CalculadoraPontos calculadoraPontos) {
		Objects.requireNonNull(calculadoraPontos, "calculadoraPontos");
		Estatisticas estatisticasAnterior = cancaoChart.getEstatisticas();
		if (estatisticasAnterior != null) {
			cancaoChart.setEstatisticas(new Estatisticas(calculadoraPontos.calcularPontos(cancaoChart.getPosicao()), estatisticasAnterior.getPermanenciaTotal(),
					estatisticasAnterior.getMelhorPosicao()));
			return this;
		} else {
			return atualizarEstatisticasUtilizando(calculadoraPontos);
		}
	}

	/**
	 * Define as estatísticas da canção.
	 * 
	 * @param estatisticas
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public CancaoChartBuilder comEstatisticas(Estatisticas estatisticas) {
		cancaoChart.setEstatisticas(Objects.requireNonNull(estatisticas, "estatisticas"));
		return this;
	}

	@Override
	public CancaoChart build() {
		return cancaoChart;
	}
}
