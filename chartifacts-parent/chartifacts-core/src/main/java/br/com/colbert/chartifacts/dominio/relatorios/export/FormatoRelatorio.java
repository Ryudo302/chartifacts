package br.com.colbert.chartifacts.dominio.relatorios.export;

/**
 * Uma enumeração dos formatos de relatório existentes.
 *
 * @author Thiago Colbert
 * @since 17/03/2015
 */
public enum FormatoRelatorio {

	/**
	 * Arquivo de texto simples.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Text_file">Wikipédia</a>
	 */
	TXT,

	/**
	 * <em>Portable Document Format</em>.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Portable_Document_Format">Wikipédia</a>
	 */
	PDF,

	/**
	 * <em>Office Open XML</em>.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Office_Open_XML">Wikipédia</a>
	 */
	XLSX;
}
