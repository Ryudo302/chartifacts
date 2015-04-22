package br.com.colbert.chartifacts.aplicacao;

import java.io.*;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;
import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.infraestrutura.swing.WorkerDoneListener;
import br.com.colbert.chartifacts.ui.*;

/**
 * <em>Presenter</em> da tela principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 21/04/2015
 */
public class MainPresenter implements Serializable {

	private static final long serialVersionUID = -2190040873391147767L;

	@Inject
	private MainWindow mainWindow;

	@Inject
	private Instance<GeracaoRelatoriosWorker> geradorRelatorios;
	@Inject
	private InformacoesAplicacao informacoesAplicacao;

	@Inject
	private Logger logger;
	@Inject
	private AppController appController;

	@PostConstruct
	protected void doBindings() {
		appController.bindPresenter(mainWindow, this);
	}

	public void start() {
		mainWindow.show();
	}

	public void escolherArquivoEntrada() {
		JFileChooser fileChooser = criarFileChooser();
		int opcao = fileChooser.showOpenDialog(mainWindow.getFrame());
		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			mainWindow.setArquivoEntrada(file);
		}
	}

	public void escolherArquivoSaida() {
		JFileChooser fileChooser = criarFileChooser();
		int opcao = fileChooser.showSaveDialog(mainWindow.getFrame());
		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			mainWindow.setArquivoSaida(file);
		}
	}

	private JFileChooser criarFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
		fileChooser.setMultiSelectionEnabled(false);
		return fileChooser;
	}

	public void gerarRelatorios() {
		File arquivoEntrada = mainWindow.getArquivoEntrada();
		File arquivoSaida = mainWindow.getArquivoSaida();

		if (arquivoEntrada == null) {
			JOptionPane.showMessageDialog(mainWindow.getFrame(), "Informe o arquivo a ser analizado!", "Informar arquivo",
					JOptionPane.WARNING_MESSAGE);
		} else if (arquivoSaida == null) {
			JOptionPane.showMessageDialog(mainWindow.getFrame(), "Informe o arquivo de saída!", "Informar arquivo", JOptionPane.WARNING_MESSAGE);
		} else {
			GeracaoRelatoriosWorker worker = geradorRelatorios.get();
			worker.setArquivoEntrada(arquivoEntrada);
			worker.setArquivoSaida(arquivoSaida);
			worker.setQuantidadePosicoes(mainWindow.getQuantidadePosicoes());

			worker.addWorkerDoneListener(new WorkerDoneListener() {
				@Override
				public void doneWithSuccess(SwingWorker<?, ?> worker) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(mainWindow.getFrame(), "Relatórios gerados com sucesso" + ":\n\n"
							+ arquivoSaida, "Sucesso", JOptionPane.INFORMATION_MESSAGE));
				}

				@Override
				public void doneWithError(SwingWorker<?, ?> worker, Throwable error) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(mainWindow.getFrame(), "Erro ao gravar arquivo de relatórios"
							+ ":\n\n" + error.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE));
				}
			});

			worker.execute();
		}
	}

	public void sobre() {
		JOptionPane.showMessageDialog(mainWindow.getFrame(),
				informacoesAplicacao.getNome() + "\n\n" + "Versão: " + informacoesAplicacao.getVersao() + StringUtils.LF + "Build: "
						+ informacoesAplicacao.getNumeroBuild() + StringUtils.LF + "Desenvolvido por: " + informacoesAplicacao.getAutor(),
				"Sobre Chartifacts", JOptionPane.INFORMATION_MESSAGE);
	}
}
