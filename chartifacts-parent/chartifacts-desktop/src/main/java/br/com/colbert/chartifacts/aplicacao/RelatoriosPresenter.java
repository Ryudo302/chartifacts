package br.com.colbert.chartifacts.aplicacao;

import java.io.File;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.colbert.chartifacts.infraestrutura.mvp.AbstractPresenter;
import br.com.colbert.chartifacts.infraestrutura.swing.WorkerDoneListener;
import br.com.colbert.chartifacts.ui.*;

/**
 * <em>Presenter</em> da tela de geração de relatórios.
 *
 * @author Thiago Colbert
 * @since 28/04/2015
 */
public class RelatoriosPresenter extends AbstractPresenter<RelatoriosView> {

	private static final long serialVersionUID = 8034832520010295862L;

	private static final String ARQUIVO_HTML_RELATORIO_GERADO_SUCESSO = "relatorios-gerados-sucesso.html";

	@Inject
	private transient Instance<GeracaoRelatoriosWorker> geradorRelatorios;

	private File ultimoArquivoSelecionado;

	@Inject
	public RelatoriosPresenter(RelatoriosView view) {
		super(view);
	}

	/**
	 * 
	 */
	public void escolherArquivoEntrada() {
		JFileChooser fileChooser = criarFileChooser();
		int opcao = fileChooser.showOpenDialog(view.getAwtContainer());
		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			view.setArquivoEntrada(file);
			ultimoArquivoSelecionado = file;
		}
	}

	/**
	 * 
	 */
	public void escolherArquivoSaida() {
		JFileChooser fileChooser = criarFileChooser();
		int opcao = fileChooser.showSaveDialog(view.getAwtContainer());
		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			view.setArquivoSaida(file);
			ultimoArquivoSelecionado = file;
		}
	}

	private JFileChooser criarFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo Texto (*.txt)", "txt"));
		fileChooser.setMultiSelectionEnabled(false);

		if (ultimoArquivoSelecionado != null) {
			fileChooser.setCurrentDirectory(ultimoArquivoSelecionado);
		}

		return fileChooser;
	}

	/**
	 * 
	 */
	public void gerarRelatorios() {
		File arquivoEntrada = view.getArquivoEntrada();
		File arquivoSaida = view.getArquivoSaida();

		if (arquivoEntrada == null) {
			mostrarMensagemAlerta("Informe o arquivo a ser analisado!", "Informar arquivo");
		} else if (arquivoSaida == null) {
			mostrarMensagemAlerta("Informe o arquivo de saída!", "Informar arquivo");
		} else {
			GeracaoRelatoriosWorker worker = geradorRelatorios.get();
			worker.setArquivoEntrada(arquivoEntrada);
			worker.setArquivoSaida(arquivoSaida);
			worker.setQuantidadePosicoes(view.getQuantidadePosicoes());
			worker.setRelatoriosConfig(view.getRelatoriosConfiguration());

			worker.addWorkerDoneListener(new WorkerDoneListener() {

				@Override
				public void doneWithSuccess(SwingWorker<?, ?> worker) {
					mostrarMensagemInformativa(carregarMensagemHtml(ARQUIVO_HTML_RELATORIO_GERADO_SUCESSO, arquivoSaida, arquivoSaida), "Sucesso");
				}

				@Override
				public void doneWithError(SwingWorker<?, ?> worker, Throwable error) {
					mostrarMensagemErro("Erro ao gravar arquivo de relatórios:\n\n" + error.getLocalizedMessage(), "Erro");
				}
			});

			worker.execute();
		}
	}
}
