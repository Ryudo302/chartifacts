package br.com.colbert.chartifacts.infraestrutura.swing;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.slf4j.Logger;

/**
 * Listener responsável por exibir e esconder a {@link LoadingDialog} quando uma tarefa é executada em <em>background</em>.
 *
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@Dependent
public class WorkerWaitListener implements WorkerStateListener {

	@Inject
	private transient Logger logger;

	@Inject
	@LoadingView
	private transient LoadingDialog loadingView;

	private boolean exibindo;

	@Override
	public void stateChange(SwingWorker<?, ?> source, StateValue oldState, StateValue newState) {
		logger.trace("Mudança de estado do worker {} de {} para {}", source, oldState, newState);
		switch (newState) {
		case STARTED:
			showView();
			break;
		case DONE:
			hideView();
			break;
		default:
			break;
		}
	}

	private void hideView() {
		if (exibindo) {
			logger.trace("Ocultando tela");
			exibindo = false;
			loadingView.close();
		}
	}

	private void showView() {
		if (!exibindo) {
			logger.trace("Exibindo tela");
			exibindo = true;
			loadingView.show();
		}
	}
}
