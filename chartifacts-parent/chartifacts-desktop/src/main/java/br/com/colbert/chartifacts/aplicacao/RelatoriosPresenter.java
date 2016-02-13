package br.com.colbert.chartifacts.aplicacao;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.infraestrutura.mvp.*;
import br.com.colbert.chartifacts.infraestrutura.swing.WorkerDoneListener;
import br.com.colbert.chartifacts.ui.*;

/**
 * <em>Presenter</em> da tela de geração de relatórios.
 *
 * @author Thiago Colbert
 * @since 28/04/2015
 */
public class RelatoriosPresenter implements Presenter {

	private static final long serialVersionUID = 8034832520010295862L;

	@Inject
	private transient RelatoriosView view;

	@Inject
	private transient Instance<GeracaoRelatoriosWorker> geradorRelatorios;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;

	@PostConstruct
	protected void doBindings() {
		appController.bindPresenter(view, this);
	}

	@Override
	public void start() {
	}

	@Override
	public View getView() {
		return view;
	}

	public void escolherArquivoEntrada() {
		JFileChooser fileChooser = criarFileChooser();
		int opcao = fileChooser.showOpenDialog(view.getAwtContainer());
		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			view.setArquivoEntrada(file);
		}
	}

	public void escolherArquivoSaida() {
		JFileChooser fileChooser = criarFileChooser();
		int opcao = fileChooser.showSaveDialog(view.getAwtContainer());
		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			view.setArquivoSaida(file);
		}
	}

	private JFileChooser criarFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
		fileChooser.setMultiSelectionEnabled(false);
		return fileChooser;
	}

	public void gerarRelatorios() {
		File arquivoEntrada = view.getArquivoEntrada();
		File arquivoSaida = view.getArquivoSaida();

		if (arquivoEntrada == null) {
			JOptionPane.showMessageDialog(view.getAwtContainer(), "Informe o arquivo a ser analizado!", "Informar arquivo", JOptionPane.WARNING_MESSAGE);
		} else if (arquivoSaida == null) {
			JOptionPane.showMessageDialog(view.getAwtContainer(), "Informe o arquivo de saída!", "Informar arquivo", JOptionPane.WARNING_MESSAGE);
		} else {
			GeracaoRelatoriosWorker worker = geradorRelatorios.get();
			worker.setArquivoEntrada(arquivoEntrada);
			worker.setArquivoSaida(arquivoSaida);
			worker.setQuantidadePosicoes(view.getQuantidadePosicoes());

			worker.addWorkerDoneListener(new WorkerDoneListener() {
				@Override
				public void doneWithSuccess(SwingWorker<?, ?> worker) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view.getAwtContainer(),
							"Relatórios gerados com sucesso" + ":\n\n" + arquivoSaida, "Sucesso", JOptionPane.INFORMATION_MESSAGE));
				}

				@Override
				public void doneWithError(SwingWorker<?, ?> worker, Throwable error) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view.getAwtContainer(),
							"Erro ao gravar arquivo de relatórios" + ":\n\n" + error.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE));
				}
			});

			worker.execute();
		}
	}
}
