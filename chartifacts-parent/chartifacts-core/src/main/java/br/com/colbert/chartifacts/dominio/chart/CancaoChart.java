package br.com.colbert.chartifacts.dominio.chart;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.chartifacts.dominio.historico.Estatisticas;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.negocio.chartrun.TipoVariacaoPosicao;

/**
 * Classe que associa uma {@link Cancao} dentro de um {@link Chart}.
 * 
 * @author ThiagoColbert
 * @since 24 de fev de 2016
 */
public class CancaoChart extends AbstractEntidade {

	private static final long serialVersionUID = 2550403927447405800L;

	private final Chart chart;
	private final PosicaoChart posicao;
	private final Cancao cancao;

	private TipoVariacaoPosicao tipoVariacaoPosicao;
	private int valorVariacaoPosicao;
	private Estatisticas estatisticas;

	/**
	 * Cria uma nova instância.
	 * 
	 * @param chart
	 *            a parada musical
	 * @param posicao
	 *            a posição dentro da parada musical
	 * @param cancao
	 *            a canção
	 */
	CancaoChart(Chart chart, PosicaoChart posicao, Cancao cancao) {
		this.chart = chart;
		this.posicao = posicao;
		this.cancao = cancao;
	}

	public Chart getChart() {
		return chart;
	}

	public PosicaoChart getPosicao() {
		return posicao;
	}

	public Cancao getCancao() {
		return cancao;
	}

	public TipoVariacaoPosicao getTipoVariacaoPosicao() {
		return tipoVariacaoPosicao;
	}

	void setTipoVariacaoPosicao(TipoVariacaoPosicao tipoVariacaoPosicao) {
		this.tipoVariacaoPosicao = tipoVariacaoPosicao;
	}

	public int getValorVariacaoPosicao() {
		return valorVariacaoPosicao;
	}

	void setValorVariacaoPosicao(int valorVariacaoPosicao) {
		this.valorVariacaoPosicao = valorVariacaoPosicao;
	}

	public Estatisticas getEstatisticas() {
		return estatisticas;
	}

	void setEstatisticas(Estatisticas estatisticas) {
		this.estatisticas = estatisticas;
	}
}
