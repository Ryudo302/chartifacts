package br.com.colbert.chartifacts.infraestrutura.mvp;

/**
 * Uma <em>View</em> que pode ter seu tamanho alterado dinamicamente em tempo de execução.
 * 
 * @author Thiago Miranda
 * @since 12 de fev de 2016
 */
public interface ViewFlexivel extends View {

	/**
	 * Adiciona um {@link ViewFlexivelListener} à lista de ouvintes da instância.
	 * 
	 * @param listener
	 *            o ouvinte a ser adicionado
	 */
	void addViewFlexivelListener(ViewFlexivelListener listener);
}
