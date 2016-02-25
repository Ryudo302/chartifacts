package br.com.colbert.chartifacts.negocio.relatorios.export;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.negocio.relatorios.Relatorio;

/**
 * Permite a exportação dos dados de um {@link Relatorio} em um formato qualquer.
 *
 * @author Thiago Colbert
 * @since 16/03/2015
 *
 * @param <O>
 *            o tipo de objeto gerado após a exportação
 */
public interface RelatorioExporter<O> {

	/**
	 * Obtém o formato dos relatórios gerados.
	 *
	 * @return o formato dos relatórios
	 */
	FormatoRelatorio getFormato();

	/**
	 * Gera um novo <em>stream</em> de leitura contendo o relatório formatado.
	 *
	 * @param relatorio
	 *            relatório a ser utilizado
	 * @param toStringKey
	 *            objeto que converte as chaves para String (opcional)
	 * @param toStringValue
	 *            objeto que converte os valores para String (opcional)
	 * @return o <em>stream</em> de leitura gerado
	 * @throws NullPointerException
	 *             caso o relatório seja <code>null</code>
	 * @see #export(Relatorio)
	 */
	<T extends Entidade, V extends Comparable<V>> O export(Relatorio<T, V> relatorio, ToFormatedStringConverter<T> toStringKey,
			ToFormatedStringConverter<V> toStringValue);

	/**
	 * Gera um novo <em>stream</em> de leitura contendo o relatório formatado.
	 * 
	 * @param relatorio
	 *            a ser utilizado
	 * @return o <em>stream</em> de leitura gerado
	 * @throws NullPointerException
	 *             caso o relatório seja <code>null</code>
	 * @see #export(Relatorio, ToFormatedStringConverter, ToFormatedStringConverter)
	 */
	default <T extends Entidade, V extends Comparable<V>> O export(Relatorio<T, V> relatorio) {
		return export(relatorio, null, null);
	};
}
