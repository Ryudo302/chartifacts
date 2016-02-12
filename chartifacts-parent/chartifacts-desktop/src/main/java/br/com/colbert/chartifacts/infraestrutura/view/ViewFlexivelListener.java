package br.com.colbert.chartifacts.infraestrutura.view;

import java.util.EventListener;

/**
 * Ouvinte de eventos do tipo {@link ViewFlexivelEvent}, que indicam que uma <em>View</em> teve seu tamanho alterado dinamicamente em tempo de execução.
 * 
 * @author Thiago Miranda
 * @since 12 de fev de 2016
 * @see ViewFlexivel
 */
public interface ViewFlexivelListener extends EventListener {

	/**
	 * Lança um evento indicando que a <em>View</em> teve seu tamanho reduzido.
	 * 
	 * @param event
	 *            o evento
	 */
	void viewReduzida(ViewFlexivelEvent event);

	/**
	 * Lança um evento indicando que a <em>View</em> teve seu tamanho expandido.
	 * 
	 * @param event
	 *            o evento
	 */
	void viewExpandida(ViewFlexivelEvent event);
}
