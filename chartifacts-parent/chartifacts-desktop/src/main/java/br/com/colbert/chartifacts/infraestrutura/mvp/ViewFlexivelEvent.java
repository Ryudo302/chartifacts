package br.com.colbert.chartifacts.infraestrutura.mvp;

import java.util.EventObject;

/**
 * Evento que indica que uma <em>View</em> teve seu tamanho alterado dinamicamente em tempo de execução.
 * 
 * @author Thiago Miranda
 * @since 12 de fev de 2016
 */
public class ViewFlexivelEvent extends EventObject {

	private static final long serialVersionUID = 3679937779163724700L;

	/**
	 * Cria uma nova instância informando qual <em>View</em> teve seu tamanho alterado.
	 * 
	 * @param source
	 */
	public ViewFlexivelEvent(ViewFlexivel source) {
		super(source);
	}

	/**
	 * Método utilitário que retorna a <em>View</em> associada ao evento.
	 * 
	 * @return a <em>View</em> associada ao evento
	 */
	public ViewFlexivel getView() {
		return (ViewFlexivel) getSource();
	}
}
