package br.com.colbert.chartifacts.negocio.chartrun;

/**
 * Os tipos de variação de posição que podem ocorrer dentro de um <em>chart-run</em>.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
public enum TipoVariacaoPosicao {

	/**
	 * A canção apareceu pela primeira vez dentro da parada.
	 */
	ESTREIA(2),

	/**
	 * A canção entrou na parada, porém já havia entrado anteriormente.
	 */
	RETORNO(3),

	/**
	 * A canção saiu da parada.
	 */
	SAIDA(-2),

	/**
	 * A canção diminuiu sua posição dentro da parada.
	 */
	SUBIDA(1),

	/**
	 * A canção aumentou sua posição dentro da parada.
	 */
	QUEDA(-1);

	private int delta;

	TipoVariacaoPosicao(int delta) {
		this.delta = delta;
	}

	public int delta() {
		return delta;
	}
}
