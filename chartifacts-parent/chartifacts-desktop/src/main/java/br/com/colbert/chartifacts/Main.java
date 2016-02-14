package br.com.colbert.chartifacts;

import java.awt.EventQueue;
import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.*;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.aplicacao.MainPresenter;
import br.com.colbert.chartifacts.console.ConsoleRunner;
import br.com.colbert.chartifacts.dominio.chart.ParserException;

/**
 * Classe principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class Main {

	private static Args arguments;

	public static void main(String[] args) {
		Main.arguments = new Args(args);
		StartMain.main(args);
	}

	@Inject
	private transient Logger logger;

	@Inject
	private MainPresenter mainPresenter;

	@Inject
	private ConsoleRunner consoleRunner;

	/**
	 * Método invocado assim que o contexto CDI é inicializado.
	 *
	 * @param event
	 *            o evento
	 * @throws ParserException
	 * @throws IOException
	 */
	protected void contextoInicializado(@Observes ContainerInitialized event) throws ParserException, IOException {
		if (arguments.isConsoleMode()) {
			logger.info("Executando em modo console");
			consoleRunner.run(arguments);
			System.exit(0);
		} else {
			EventQueue.invokeLater(() -> {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
					logger.error("Erro ao definir Look & Feel", exception);
				}

				logger.info("Executando em modo GUI");
				mainPresenter.start();
			});
		}
	}
}
