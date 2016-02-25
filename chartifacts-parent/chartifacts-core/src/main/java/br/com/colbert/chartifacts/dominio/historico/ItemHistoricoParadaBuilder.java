package br.com.colbert.chartifacts.dominio.historico;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.Builder;
import org.jboss.weld.exceptions.IllegalStateException;

import br.com.colbert.chartifacts.dominio.chart.ChartRun;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;

/**
 * Classe que facilida a construção de instâncias de {@link ItemHistoricoParada}.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class ItemHistoricoParadaBuilder implements Builder<ItemHistoricoParada>, Serializable {

	private static final long serialVersionUID = 6729031750904767433L;

	private final CalculadoraPontos calculadoraPontos;

	private Cancao cancao;
	private IntervaloDeDatas periodo;
	private ChartRun chartRun;
	private Estatisticas estatisticas;

	private ItemHistoricoParadaBuilder(CalculadoraPontos calculadoraPontos) {
		this.calculadoraPontos = calculadoraPontos;
	}

	/**
	 * Inicia a criação de um ítem de histórico.
	 *
	 * @param calculadoraPontos
	 *            calculadora de pontos a ser utilizada durante o cálculo das estatísticas da canção
	 * @return uma instância do <em>builder</em>
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public static ItemHistoricoParadaBuilder novo(CalculadoraPontos calculadoraPontos) {
		return new ItemHistoricoParadaBuilder(Objects.requireNonNull(calculadoraPontos, "Calculadora de pontos"));
	}

	/**
	 * Define a canção do item de histórico.
	 *
	 * @param cancao
	 *            a canção
	 * @return <code>this</code>, para chamadas encadeadas
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public ItemHistoricoParadaBuilder comCancao(Cancao cancao) {
		this.cancao = Objects.requireNonNull(cancao, "canção");
		return this;
	}

	/**
	 * Define o período do item de histórico.
	 *
	 * @param periodo
	 *            o período
	 * @return <code>this</code>, para chamadas encadeadas
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public ItemHistoricoParadaBuilder comPeriodo(IntervaloDeDatas periodo) {
		this.periodo = Objects.requireNonNull(periodo, "período");
		return this;
	}

	/**
	 * Define o <em>chart-run</em> do item de histórico.
	 *
	 * @param chartRun
	 *            o <em>chart-run</em>
	 * @return <code>this</code>, para chamadas encadeadas
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public ItemHistoricoParadaBuilder comChartRun(ChartRun chartRun) {
		this.chartRun = Objects.requireNonNull(chartRun, "chart-run");
		estatisticas = EstatisticasBuilder.utilizando(calculadoraPontos).aPartirDo(chartRun).build();
		return this;
	}

	/**
	 * Limpa o valor de todos os atributos para que o <em>builder</em> possa ser reutilizado.
	 */
	public void clear() {
		cancao = null;
		chartRun = null;
		periodo = null;
	}

	/**
	 * Verifica se o <em>builder</em> está com todos os valores definidos e pronto para criar um novo item de histórico.
	 *
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean isReadyToBuild() {
		return cancao != null && chartRun != null && periodo != null;
	}

	/**
	 * @throws IllegalStateException
	 *             caso o <em>builder</em> ainda não esteja pronto para criar um novo objeto
	 * @see #isReadyToBuild()
	 */
	@Override
	public ItemHistoricoParada build() {
		if (isReadyToBuild()) {
			return new ItemHistoricoParada(cancao, periodo, chartRun, estatisticas);
		} else {
			throw new IllegalStateException("Not ready to build");
		}
	}
}
