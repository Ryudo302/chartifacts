package br.com.colbert.chartifacts.infraestrutura.mvp;

import java.awt.Container;
import java.io.Serializable;

/**
 * Uma <em>View</em> no modelo MVP.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 */
public interface View extends Serializable {

	/**
	 * Obtém a instância de {@link Container} que representa o conteúdo da <em>view</em>.
	 * 
	 * @return o contêiner AWT
	 */
	Container getAwtContainer();

	/**
	 * Obtém o nome da <em>view</em>.
	 * 
	 * @return o nome
	 */
	default String getName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Torna a <em>view</em> oculta.
	 */
	default void close() {
		getAwtContainer().setVisible(false);
	}

	/**
	 * Torna a <em>view</em> visível.
	 */
	default void show() {
		getAwtContainer().setVisible(true);
	}
}
