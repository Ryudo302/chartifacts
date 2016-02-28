package br.com.colbert.chartifacts.console;

import java.io.*;
import java.nio.file.*;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.Args;
import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.infraestrutura.io.HistoricoParadaFileParser;
import br.com.colbert.chartifacts.negocio.parser.ParserException;
import br.com.colbert.chartifacts.negocio.relatorios.*;

/**
 * Permite a execução da aplicação apenas via console, sem interface gráfica.
 * 
 * @author ThiagoColbert
 * @since 3 de fev de 2016
 */
public class ConsoleRunner implements Serializable {

	private static final long serialVersionUID = -8073060925359519268L;

	@Inject
	private transient Logger logger;

	@Inject
	private transient HistoricoParadaFileParser historicoParadaFileParser;
	@Inject
	private transient RelatoriosFacade relatoriosFacade;
	@Inject
	private transient RelatoriosConfiguration relatoriosConfiguration;

	/**
	 * Executa a aplicação com os parâmetros informados.
	 * 
	 * @param arguments
	 *            os parâmetros de execução
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws ParserException
	 * @throws IOException
	 */
	public void run(Args arguments) throws ParserException, IOException {
		Validate.notNull(arguments, "arguments");

		logger.info("Iniciando");

		Path arquivoHistorico = Paths.get(arguments.getCaminhoArquivoHistorico());
		int quantidadePosicoesParada = arguments.getQuantidadePosicoesParada();

		HistoricoParada historicoParada = historicoParadaFileParser.parse(arquivoHistorico.toFile(), quantidadePosicoesParada);

		if (arguments.isExportarRelatorios()) {
			Path arquivoSaidaRelatorios = Paths.get(arguments.getCaminhoArquivoSaidaRelatorios());
			String relatorios = relatoriosFacade.exportarTodosRelatoriosEmTxt(historicoParada, relatoriosConfiguration);
			Files.write(arquivoSaidaRelatorios, relatorios.getBytes());
		}

		if (arguments.isExportarAllTimeChart()) {
			Path arquivoSaidaAllTimeChart = Paths.get(arguments.getCaminhoArquivoSaidaAllTimeChart());
			String allTimeChart = relatoriosFacade.exportarAllTimeChartEmTxt(historicoParada, relatoriosConfiguration);
			Files.write(arquivoSaidaAllTimeChart, allTimeChart.getBytes());
		}

		logger.info("Finalizado");
	}
}
