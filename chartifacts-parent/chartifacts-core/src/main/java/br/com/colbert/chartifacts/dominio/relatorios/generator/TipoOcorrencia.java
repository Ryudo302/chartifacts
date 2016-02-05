package br.com.colbert.chartifacts.dominio.relatorios.generator;

/**
 * Tipos de ocorrências que podem ser utilizados pelos relatórios.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
public enum TipoOcorrencia {

	/**
	 * Indica uma ocorrência geral, sem especificar o que seja.
	 */
	GERAL,

	SUBIDA,

	QUEDA,

	ESTREIA,

	SAIDA,

	RETORNO,

	TEMPO;
}
