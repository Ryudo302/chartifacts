package br.com.colbert.chartifacts.dominio.chart;

import static br.com.colbert.chartifacts.negocio.chartrun.TipoVariacaoPosicao.*;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.dominio.musica.Cancao;

/**
 * {@link Builder} de instâncias de {@link CancaoChart}.
 * 
 * @author ThiagoColbert
 * @since 25 de fev de 2016
 */
public class CancaoChartBuilder implements Builder<CancaoChart>, Serializable {

	private static final long serialVersionUID = 3362541391811849925L;

	private final CancaoChart cancaoChart;

	private CalculadoraPontos calculadoraPontos;

	private CancaoChartBuilder(Chart chart, PosicaoChart posicao, Cancao cancao) {
		cancaoChart = new CancaoChart(chart, posicao, cancao);
	}

	/**
	 * Inicia a criação de um {@link CancaoChart}, informando a parada musical, a posição e canção.
	 * 
	 * @param chart
	 *            a parada musical
	 * @param posicao
	 *            a posição
	 * @param cancao
	 *            a canção
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 */
	public static CancaoChartBuilder novo(Chart chart, PosicaoChart posicao, Cancao cancao) {
		return new CancaoChartBuilder(Objects.requireNonNull(chart, "chart"), Objects.requireNonNull(posicao, "posicao"),
				Objects.requireNonNull(cancao, "cancao"));
	}

	/**
	 * Define a calculadora de pontos.
	 * 
	 * @param calculadoraPontos
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso qualquer um dos parâmetros seja <code>null</code>
	 */
	public CancaoChartBuilder utilizando(CalculadoraPontos calculadoraPontos) {
		this.calculadoraPontos = Objects.requireNonNull(calculadoraPontos, "calculadoraPontos");
		return this;
	}

	/**
	 * Define que a canção está estreando na parada musical.
	 * 
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public CancaoChartBuilder estreando() {
		cancaoChart.setTipoVariacaoPosicao(ESTREIA);
		cancaoChart.setValorVariacaoPosicao(0);
		return this;
	}

	/**
	 * Define que a canção está retornando à parada musical.
	 * 
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public CancaoChartBuilder retornando() {
		cancaoChart.setTipoVariacaoPosicao(RETORNO);
		cancaoChart.setValorVariacaoPosicao(0);
		return this;
	}

	/**
	 * Define que a canção está variando de posição dentro da parada musical.
	 * 
	 * @param valorVariacao
	 *            o valor da variação - negativo para queda, positivo para subida ou zero para permanência
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public CancaoChartBuilder comVariacao(int valorVariacao) {
		cancaoChart.setTipoVariacaoPosicao(valorVariacao < 0 ? QUEDA : valorVariacao > 0 ? SUBIDA : PERMANENCIA);
		cancaoChart.setValorVariacaoPosicao(valorVariacao);
		return this;
	}

	@Override
	public CancaoChart build() {
		Validate.validState(calculadoraPontos != null, "Calculadora de pontos não definida");
		cancaoChart.setEstatisticas(EstatisticasBuilder.utilizando(calculadoraPontos).aPartirDaPosicao(cancaoChart.getPosicao()).build());
		return cancaoChart;
	}
}
