package br.com.colbert.chartifacts;

import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import br.com.colbert.chartifacts.dominio.chart.ParserException;
import br.com.colbert.chartifacts.ui.MainWindow;

/**
 * Classe principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class Main {

	public static void main(String[] args) {
		StartMain.main(args);
	}

	@Inject
	private MainWindow mainWindow;

	/**
	 * Método invocado assim que o contexto CDI é inicializado.
	 *
	 * @param event
	 *            o evento
	 * @throws ParserException
	 * @throws IOException
	 */
	protected void contextoInicializado(@Observes ContainerInitialized event) throws ParserException, IOException {
		mainWindow.show();
	}
}
