package br.com.colbert.chartifacts.infraestrutura.swing;

import java.beans.*;
import java.util.EventListener;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;

import org.apache.commons.lang3.StringUtils;

/**
 * {@link EventListener} que observa as alterações de status de um {@link SwingWorker}.
 * 
 * @author Thiago Colbert
 * @since 16/01/2015
 *
 */
@FunctionalInterface
public interface WorkerStateListener extends PropertyChangeListener {

	@Override
	default void propertyChange(PropertyChangeEvent event) {
		if (StringUtils.equals(event.getPropertyName(), "state")) {
			stateChange((SwingWorker<?, ?>) event.getSource(), (StateValue) event.getOldValue(), (StateValue) event.getNewValue());
		}
	}

	/**
	 * Método invocado quando o estado do <em>worker</em> é alterado.
	 * 
	 * @param source
	 *            a instância de {@link SwingWorker} que originou o evento
	 * @param oldState
	 *            o estado anterior
	 * @param newState
	 *            o novo estado
	 */
	void stateChange(SwingWorker<?, ?> source, StateValue oldState, StateValue newState);
}
