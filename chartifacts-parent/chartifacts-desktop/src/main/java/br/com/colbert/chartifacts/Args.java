package br.com.colbert.chartifacts;

import java.io.Serializable;
import java.net.URI;
import java.nio.file.*;

import org.apache.commons.lang3.Validate;

/**
 * Argumentos que podem ser passados para a aplicação durante a execução.
 * 
 * @author ThiagoColbert
 * @since 3 de fev de 2016
 */
public class Args implements Serializable {

	private static final long serialVersionUID = 7063004231451359772L;

	private static final String CONSOLE_MODE_ARG = "-c";
	private static final String EXPORTAR_RELATORIOS_ARG = "-r";
	private static final String EXPORTAR_ALL_TIME_CHART_ARG = "-a";
	private static final String CAMINHO_ARQUIVO_HISTORICO_ARG = "-f";
	private static final String CAMINHO_ARQUIVO_SAIDA_RELATORIOS_ARG = "-or";
	private static final String CAMINHO_ARQUIVO_SAIDA_ALL_TIME_CHART_ARG = "-oa";
	private static final String QUANTIDADE_POSICOES_PARADA_ARG = "-q";

	private boolean consoleMode;
	private boolean exportarRelatorios;
	private boolean exportarAllTimeChart;

	private URI caminhoArquivoHistorico;
	private URI caminhoArquivoSaidaRelatorios;
	private URI caminhoArquivoSaidaAllTimeChart;

	private Integer quantidadePosicoesParada;

	/**
	 * Cria uma nova instância com o array de Strings representando os
	 * argumentos informados.
	 * 
	 * @param args
	 */
	public Args(String[] args) {
		parseArgs(args).validateState();
	}

	private void validateState() {
		if (consoleMode) {
			Validate.notNull(caminhoArquivoHistorico, "Parâmetro obrigatório: " + CAMINHO_ARQUIVO_HISTORICO_ARG);
			Validate.notNull(quantidadePosicoesParada, "Parâmetro obrigatório: " + QUANTIDADE_POSICOES_PARADA_ARG);

			Validate.isTrue(!exportarAllTimeChart || (exportarAllTimeChart && caminhoArquivoSaidaAllTimeChart != null),
					"Parâmetro não informado: " + CAMINHO_ARQUIVO_SAIDA_ALL_TIME_CHART_ARG);
			Validate.isTrue(!exportarRelatorios || (exportarRelatorios && caminhoArquivoSaidaRelatorios != null),
					"Parâmetro não informado: " + CAMINHO_ARQUIVO_SAIDA_RELATORIOS_ARG);
		}
	}

	private Args parseArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String currentArg = args[i];
			switch (currentArg) {
			case CONSOLE_MODE_ARG:
				consoleMode = true;
				break;
			case EXPORTAR_RELATORIOS_ARG:
				exportarRelatorios = true;
				break;
			case EXPORTAR_ALL_TIME_CHART_ARG:
				exportarAllTimeChart = true;
				break;
			case CAMINHO_ARQUIVO_HISTORICO_ARG:
				caminhoArquivoHistorico = toURI(args[i + 1], true);
				break;
			case CAMINHO_ARQUIVO_SAIDA_RELATORIOS_ARG:
				caminhoArquivoSaidaRelatorios = toURI(args[i + 1], false);
				break;
			case CAMINHO_ARQUIVO_SAIDA_ALL_TIME_CHART_ARG:
				caminhoArquivoSaidaAllTimeChart = toURI(args[i + 1], false);
				break;
			case QUANTIDADE_POSICOES_PARADA_ARG:
				quantidadePosicoesParada = Integer.valueOf(args[i + 1]);
			}
		}

		return this;
	}

	private URI toURI(String value, boolean deveExistir) {
		Path path = Paths.get(value);
		Validate.isTrue(!deveExistir || (deveExistir && path.toFile().exists()), "O arquivo não existe: " + value);
		return path.toUri();
	}

	public boolean isConsoleMode() {
		return consoleMode;
	}

	public URI getCaminhoArquivoHistorico() {
		return caminhoArquivoHistorico;
	}

	public int getQuantidadePosicoesParada() {
		return quantidadePosicoesParada;
	}

	public boolean isExportarRelatorios() {
		return exportarRelatorios;
	}

	public boolean isExportarAllTimeChart() {
		return exportarAllTimeChart;
	}

	public URI getCaminhoArquivoSaidaRelatorios() {
		return caminhoArquivoSaidaRelatorios;
	}

	public URI getCaminhoArquivoSaidaAllTimeChart() {
		return caminhoArquivoSaidaAllTimeChart;
	}
}
