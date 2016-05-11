package br.com.colbert.chartifacts;

import java.awt.EventQueue;
import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.*;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.aplicacao.MainPresenter;
import br.com.colbert.chartifacts.console.ConsoleRunner;
import br.com.colbert.chartifacts.infraestrutura.properties.PropertiesFilesResolver;
import br.com.colbert.chartifacts.negocio.parser.ParserException;

/**
 * Classe principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class Main {

	private static Args arguments;

	@Inject
	private transient Logger logger;

	@Inject
	private MainPresenter mainPresenter;

	@Inject
	private ConsoleRunner consoleRunner;

	/**
	 * Método <em>main</em>.
	 * 
	 * @param args
	 *            parâmetros para a aplicação
	 */
	public static void main(String[] args) {
		System.setProperty(PropertiesFilesResolver.SYSTEM_PROPERTY_ARQUIVOS, "parser.properties,relatorios.properties,application.properties");

		try {
			Main.arguments = new Args(args);
			StartMain.main(args);
		} catch (Throwable throwable) {
			System.err.println("Erro ao iniciar aplicação");
			throwable.printStackTrace();
			if (!SystemUtils.isJavaAwtHeadless()) {
				JOptionPane.showMessageDialog(null, "Erro ao iniciar aplicação: " + ExceptionUtils.getRootCauseMessage(throwable), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Método invocado assim que o contexto CDI é inicializado.
	 *
	 * @param event
	 *            o evento
	 * @throws ParserException
	 * @throws IOException
	 */
	protected void contextoInicializado(/*@Observes*/ ContainerInitialized event) throws ParserException, IOException {
		if (arguments.isConsoleMode()) {
			logger.info("Executando em modo console");
			Thread.setDefaultUncaughtExceptionHandler((thread, error) -> logarErro(thread, error));
			consoleRunner.run(arguments);
			System.exit(0);
		} else {
			logger.info("Executando em modo GUI");
			Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
				logarErro(thread, error);
				JOptionPane.showMessageDialog(null, "Ocorreu um erro não tratado pela aplicação: " + error.getLocalizedMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			});

			EventQueue.invokeLater(() -> {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
					logger.error("Erro ao definir Look & Feel", exception);
				}

				mainPresenter.start();
			});
		}
	}

	private void logarErro(Thread thread, Throwable error) {
		logger.error("Erro na execução da thread {}", thread.getName(), error);
	}
}
