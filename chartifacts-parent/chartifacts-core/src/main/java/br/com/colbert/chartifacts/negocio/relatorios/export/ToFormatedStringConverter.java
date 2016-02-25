package br.com.colbert.chartifacts.negocio.relatorios.export;

/**
 * Define como um objeto é convertido em uma {@link String} para exibição em uma versão exportada de um relatório.
 *
 * @author Thiago Colbert
 * @since 16/03/2015
 *
 * @param <T>
 *            o tipo de objeto que é convertido
 */
@FunctionalInterface
public interface ToFormatedStringConverter<T> {

	/**
	 * Converte um objeto em uma {@link String} para exibição em um relatório exportado.
	 *
	 * @param objeto
	 *            objeto a ser convertido
	 * @return a String gerada
	 */
	String convert(T objeto);
}
