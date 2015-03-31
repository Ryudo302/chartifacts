package br.com.colbert.chartifacts;

import java.io.*;
import java.nio.file.*;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosFacade;
import br.com.colbert.chartifacts.infraestrutura.io.HistoricoParadaFileParser;

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
	private HistoricoParadaFileParser parser;
	@Inject
	private RelatoriosFacade relatoriosFacade;

	/**
	 * Método invocado assim que o contexto CDI é inicializado.
	 *
	 * @param event
	 *            o evento
	 * @throws ParserException
	 * @throws IOException
	 */
	protected void contextoInicializado(@Observes ContainerInitialized event) throws ParserException, IOException {
		File arquivo = Paths.get("C:", "Users", "ThiagoColbert", "Documents", "Paradas Musicais", "Hit Parade TL", "Geral", "Históricos.txt")
				.toFile();
		HistoricoParada historicoParada = parser.parse(arquivo, 20);
		Files.write(Paths.get(".", "relatorio.txt"), relatoriosFacade.exportarTodosEmTxt(historicoParada).getBytes());
	}
}
