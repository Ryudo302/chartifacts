package br.com.colbert.chartifacts.ui;

import java.io.File;
import java.nio.file.*;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosFacade;
import br.com.colbert.chartifacts.infraestrutura.io.HistoricoParadaFileParser;
import br.com.colbert.chartifacts.infraestrutura.swing.AbstractWorker;

/**
 * {@link SwingWorker} responsável por gerar os relatórios da parada a partir de um arquivo de histórico e gravá-los em um arquivo
 * de saída.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
public class GeracaoRelatoriosWorker extends AbstractWorker<Path, Void> {

	@Inject
	private Logger logger;

	@Inject
	private HistoricoParadaFileParser historicoParadaFileParser;
	@Inject
	private RelatoriosFacade relatoriosFacade;

	private File arquivoEntrada;
	private File arquivoSaida;
	private int quantidadePosicoes;

	@Override
	protected Path doInBackground() throws Exception {
		logger.debug("Validando estado");
		validarEstado();
		logger.debug("Analizando arquivo e processando histórico da parada");
		HistoricoParada historicoParada = historicoParadaFileParser.parse(arquivoEntrada, quantidadePosicoes);
		logger.debug("Gerando relatórios e gravando em arquivo");
		return Files.write(arquivoSaida.toPath(), relatoriosFacade.exportarTodosRelatoriosEmTxt(historicoParada).getBytes());
	}

	private void validarEstado() {
		StringBuilder errosBuilder = new StringBuilder();
		if (arquivoEntrada == null) {
			errosBuilder.append("Arquivo de entrada não definido").append(StringUtils.LF);
		}
		if (arquivoSaida == null) {
			errosBuilder.append("Arquivo de saída não definido").append(StringUtils.LF);
		}
		if (quantidadePosicoes <= 0) {
			errosBuilder.append("Quantidade de posições não definida").append(StringUtils.LF);
		}
		if (errosBuilder.length() > 0) {
			throw new IllegalStateException(errosBuilder.toString());
		}
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException exception) {
			logger.error("Erro ao gerar relatórios.", exception);
			fireError(exception);
		}
	}

	public void setArquivoEntrada(File arquivoEntrada) {
		this.arquivoEntrada = arquivoEntrada;
	}

	public void setArquivoSaida(File arquivoSaida) {
		this.arquivoSaida = arquivoSaida;
	}

	public void setQuantidadePosicoes(int quantidadePosicoes) {
		this.quantidadePosicoes = quantidadePosicoes;
	}
}
