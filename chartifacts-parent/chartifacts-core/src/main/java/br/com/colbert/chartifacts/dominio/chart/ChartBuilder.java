package br.com.colbert.chartifacts.dominio.chart;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.*;

import br.com.colbert.chartifacts.dominio.historico.CalculadoraPontos;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.infraestrutura.util.Holder;

/**
 * {@link Builder} de instâncias de {@link Chart}.
 * 
 * @author ThiagoColbert
 * @since 24 de fev de 2016
 */
public class ChartBuilder implements Builder<Chart>, Serializable {

	private static final long serialVersionUID = 4894083072736909580L;

	private final Chart chart;
	private final CalculadoraPontos calculadoraPontos;

	private ChartBuilder(int numero, CalculadoraPontos calculadoraPontos) {
		chart = new Chart(numero);
		this.calculadoraPontos = calculadoraPontos;
	}

	/**
	 * Inicia a criação de um {@link Chart} informando o seu número.
	 * 
	 * @param numero
	 *            número da parada a ser criada
	 * @param calculadoraPontos
	 *            calculadora utilizada para obter a pontuação das canções
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code> como calculdora
	 * @throws IllegalArgumentException
	 *             caso seja informado um número inválido (menor ou igual a zero)
	 */
	public static ChartBuilder novo(int numero, CalculadoraPontos calculadoraPontos) {
		Validate.isTrue(numero > 0, "Número inválido - deve ser maior que zero");
		return new ChartBuilder(numero, Objects.requireNonNull(calculadoraPontos, "calculadoraPontos"));
	}

	/**
	 * Define o período da parada musical.
	 * 
	 * @param periodo
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public ChartBuilder comPeriodo(IntervaloDeDatas periodo) {
		chart.setPeriodo(Objects.requireNonNull(periodo, "periodo"));
		return this;
	}

	/**
	 * Define qual parada musical vem logo antes desta.
	 * 
	 * @param anterior
	 *            (pode ser <code>null</code>)
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public ChartBuilder anterior(Chart anterior) {
		chart.setAnterior(anterior);
		return this;
	}

	/**
	 * Define qual parada musical vem logo após esta.
	 * 
	 * @param proximo
	 *            (pode ser <code>null</code>)
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public ChartBuilder proximo(Chart proximo) {
		chart.setProximo(proximo);
		return this;
	}

	/**
	 * Define a lista de canções da parada musical.
	 * 
	 * @param cancoes
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public ChartBuilder comCancoes(List<Cancao> cancoes) {
		Objects.requireNonNull(cancoes, "cancoes");

		final Holder<Integer> numeroPosicao = new Holder<>();
		numeroPosicao.setValue(0);

		chart.setCancoes(cancoes.stream()
				.map(cancao -> CancaoChartBuilder
						.novo(chart, PosicaoChart.valueOf(numeroPosicao.setValue(numeroPosicao.getValue() + 1).getValue()), cancao)
						.utilizando(calculadoraPontos).build())
				.collect(Collectors.toList()));

		return this;
	}

	/**
	 * @throws IllegalStateException
	 *             caso os valores obrigatórios para criação de {@link Chart} ainda não tenham sido definidos.
	 */
	@Override
	public Chart build() {
		Validate.validState(chart.getPeriodo() != null, "Período não informado");
		Validate.validState(chart.getCancoes() != null, "Canções não informadas");

		return chart;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
