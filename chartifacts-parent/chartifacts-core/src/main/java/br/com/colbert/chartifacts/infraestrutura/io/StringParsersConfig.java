package br.com.colbert.chartifacts.infraestrutura.io;

import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;

/**
 * Configurações utilizadas pelos <em>parser</em>s de Strings.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@ApplicationScoped
public interface StringParsersConfig {

	/**
	 * {@link Pattern} utilizado para identificar nomes de artistas.
	 *
	 * @return o padrão para nomes de artistas
	 */
	Pattern nomeArtistaPattern();

	/**
	 * {@link Pattern} utilizado para identificar separadores de nomes de artistas.
	 *
	 * @return o padrão de separadores de nomes de artistas
	 */
	Pattern separadoresArtistasPattern();

	/**
	 * {@link Pattern} utilizado para identificar separadores de artistas e título de canção.
	 *
	 * @return o padrão de separadores de artistas e título de canção
	 */
	Pattern separadorArtistaCancaoPattern();

	/**
	 * {@link Pattern} utilizado para identificar títulos de canções.
	 *
	 * @return o padrão de títulos de canções
	 */
	Pattern tituloCancaoPattern();

	/**
	 * {@link Pattern} utilizado para identificar separadores de títulos de canção.
	 *
	 * @return o padrão de separadores de títulos
	 */
	Pattern titulosAlternativosCancaoSeparadorPattern();

	/**
	 * {@link Pattern} utilizado para identificar intervalo de datas de um período.
	 * 
	 * @return o padrão de intervalo de datas
	 */
	Pattern periodoIntervaloPattern();

	/**
	 * Formato utilizado para datas.
	 * 
	 * @return o formato das datas
	 */
	String formatoDatas();

	/**
	 * {@link String} utilizada como separador das posições de um <em>chart-run</em>.
	 *
	 * @return o separador de posições
	 */
	String separadorPosicoesChartRun();
}
