package br.com.colbert.chartifacts.infraestrutura.mvp;

import java.io.Serializable;

/**
 * Um <em>Presenter</em> no modelo MVP.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 */
public interface Presenter extends Serializable {

	/**
	 * Indica que o <em>presenter</em> deve ser iniciado.
	 */
	void start();

	/**
	 * Obtém a <em>view</em> atualmente associada a esta instância.
	 * 
	 * @return a <em>view</em>
	 */
	View getView();
}
