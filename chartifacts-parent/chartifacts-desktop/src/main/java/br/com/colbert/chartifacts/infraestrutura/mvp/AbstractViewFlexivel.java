package br.com.colbert.chartifacts.infraestrutura.mvp;

import java.io.Serializable;

import javax.swing.event.EventListenerList;

/**
 * Implementação-base para todas as <em>View</em>s que implementem {@link ViewFlexivel}.
 * 
 * @author Thiago Miranda
 * @since 12 de fev de 2016
 */
public abstract class AbstractViewFlexivel implements ViewFlexivel, Serializable {

	private static final long serialVersionUID = -1366529262822264164L;

	private final EventListenerList listenerList;

	/**
	 * 
	 */
	public AbstractViewFlexivel() {
		listenerList = new EventListenerList();
	}

	@Override
	public void addViewFlexivelListener(ViewFlexivelListener listener) {
		listenerList.add(ViewFlexivelListener.class, listener);
	}

	/**
	 * Lança um evento do tipo {@link ViewFlexivelEvent}.
	 * 
	 * @param aumentouTamanho
	 *            <code>true</code> caso a <em>View</em> agora esteja maior, <code>false</code> caso contrário
	 */
	protected void fireViewFlexivelEvent(boolean aumentouTamanho) {
		Object[] listeners = listenerList.getListenerList();
		ViewFlexivelEvent viewFlexivelEvent = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ViewFlexivelListener.class) {
				// Lazily create the event:
				if (viewFlexivelEvent == null) {
					viewFlexivelEvent = new ViewFlexivelEvent(AbstractViewFlexivel.this);
				}

				if (aumentouTamanho) {
					((ViewFlexivelListener) listeners[i + 1]).viewExpandida(viewFlexivelEvent);
				} else {
					((ViewFlexivelListener) listeners[i + 1]).viewReduzida(viewFlexivelEvent);
				}
			}
		}
	}
}
