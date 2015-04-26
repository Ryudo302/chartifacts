package br.com.colbert.chartifacts.infraestrutura.comparator;

public enum TipoOrdenacao {

	CRESCENTE((byte) 1), DECRESCENTE((byte) -1);

	private byte indice;

	TipoOrdenacao(byte indice) {
		this.indice = indice;
	}

	public int getIndice() {
		return indice;
	}
}
