package br.com.colbert.chartifacts.negocio.relatorios.generator;

import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;

/**
 * Tipos de locais que podem ser utilizados pelos relatórios.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
public enum TipoLocal {

	/**
	 * Toda a parada musical, sem definir uma posição específica.
	 */
	PARADA,

	/**
	 * Um grupo de {@link ElementoChartRun} que estejam abaixo de um outro específico.
	 */
	TOP,

	/**
	 * Um {@link ElementoChartRun} específico.
	 */
	POSICAO;
}
