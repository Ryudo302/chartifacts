package br.com.colbert.chartifacts.dominio.chart;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.chartifacts.dominio.chartrun.ChartRun;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;

/**
 * Um item dentro de um histórico de parada musical.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public class ItemHistoricoParada extends AbstractEntidade {

	private static final long serialVersionUID = 6711078927640979863L;

	private final Cancao cancao;
	private final IntervaloDeDatas periodo;
	private final ChartRun chartRun;
	private final Estatisticas estatisticas;

	/**
	 * Cria um novo item de histórico com a canção e as informações definidas.
	 *
	 * @param cancao
	 *            a canção
	 * @param periodo
	 *            o período em que a canção esteve dentro da parada
	 * @param chartRun
	 *            da canção dentro da parada
	 * @param estatisticas
	 *            estatísticas da canção dentro da parada
	 */
	ItemHistoricoParada(Cancao cancao, IntervaloDeDatas periodo, ChartRun chartRun, Estatisticas estatisticas) {
		this.cancao = cancao;
		this.periodo = periodo;
		this.chartRun = chartRun;
		this.estatisticas = estatisticas;
	}

	public Cancao getCancao() {
		return cancao;
	}

	public IntervaloDeDatas getPeriodo() {
		return periodo;
	}

	public ChartRun getChartRun() {
		return chartRun;
	}

	public Estatisticas getEstatisticas() {
		return estatisticas;
	}
}
