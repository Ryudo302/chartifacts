package br.com.colbert.chartifacts.dominio.chart;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import br.com.colbert.base.dominio.AbstractEntidade;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;

/**
 * Uma parada musical.
 * 
 * @author ThiagoColbert
 * @since 24 de fev de 2016
 */
public class Chart extends AbstractEntidade {

	private static final long serialVersionUID = 1438581498819654021L;

	private final int numero;

	private IntervaloDeDatas periodo;
	private Chart anterior;
	private Chart proximo;

	private List<CancaoChart> cancoes;

	Chart(int numero) {
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public IntervaloDeDatas getPeriodo() {
		return periodo;
	}

	void setPeriodo(IntervaloDeDatas periodo) {
		this.periodo = periodo;
	}

	public Chart getAnterior() {
		return anterior;
	}

	void setAnterior(Chart anterior) {
		this.anterior = anterior;
	}

	public Chart getProximo() {
		return proximo;
	}

	void setProximo(Chart proximo) {
		this.proximo = proximo;
	}

	public List<CancaoChart> getCancoes() {
		return cancoes;
	}

	void setCancoes(List<CancaoChart> cancoes) {
		this.cancoes = cancoes;
	}

	/**
	 * Obtém a quantidade de posições da parada.
	 * 
	 * @return a quantidade de posições
	 */
	public int getQuantidadePosicoes() {
		return CollectionUtils.size(cancoes);
	}
}
