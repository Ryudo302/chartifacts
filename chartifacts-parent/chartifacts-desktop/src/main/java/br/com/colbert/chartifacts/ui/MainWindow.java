package br.com.colbert.chartifacts.ui;

import java.awt.Font;
import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;

/**
 * Janela principal da aplicação.
 *
 * TODO Criar Presenter
 *
 * @author Thiago Colbert
 * @since 10/04/2015
 */
@Singleton
public class MainWindow implements Serializable {

	private static final long serialVersionUID = 1998102092578023349L;

	private final JFrame frame;

	@Inject
	private Logger logger;

	@Inject
	private Instance<GeracaoRelatoriosWorker> geradorRelatorios;

	@Inject
	private InformacoesAplicacao informacoesAplicacao;
	@Inject
	private ArquivoFormatter arquivoFormatter;

	@Inject
	private PadroesArquivoEntradaPanel padroesArquivoPanel;
	@Inject
	private RelatoriosConfigPanel relatoriosConfigPanel;

	private final JFormattedTextField arquivoEntradaField;
	private final JFormattedTextField arquivoSaidaField;
	private final JSpinner quantidadePosicoesSpinner;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		frame = new JFrame("Chartifacts");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.setBounds(100, 100, 510, 483);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		JMenuItem menuItemSair = new JMenuItem("Sair");
		menuItemSair.addActionListener(event -> MainWindow.this.close());
		menuArquivo.add(menuItemSair);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);

		JMenuItem menuItemSobre = new JMenuItem("Sobre");
		menuItemSobre.addActionListener(event -> {
			JOptionPane.showMessageDialog(frame, "Chartifacts" + "\n\n" + "Versão: " + informacoesAplicacao.getVersao() + StringUtils.LF + "Build: "
					+ informacoesAplicacao.getNumeroBuild() + StringUtils.LF + "Desenvolvido por: " + informacoesAplicacao.getAutor(),
					"Sobre Chartifacts", JOptionPane.INFORMATION_MESSAGE);
		});
		menuAjuda.add(menuItemSobre);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, }));

		JPanel arquivosPanel = new JPanel();
		arquivosPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC, }));

		frame.getContentPane().add(arquivosPanel, "2, 2, 7, 1, fill, default");

		JLabel arquivoEntradaLabel = new JLabel("Arquivo de Entrada:");
		arquivoEntradaLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		arquivosPanel.add(arquivoEntradaLabel, "2, 2, right, center");

		arquivoEntradaField = new JFormattedTextField();
		arquivosPanel.add(arquivoEntradaField, "4, 2");
		arquivoEntradaField.setEditable(false);

		JButton escolherArquivoEntradaButton = new JButton("Procurar...");
		arquivosPanel.add(escolherArquivoEntradaButton, "6, 2");
		escolherArquivoEntradaButton.setToolTipText("Selecionar o arquivo de histórico");
		escolherArquivoEntradaButton.addActionListener(event -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
			fileChooser.setMultiSelectionEnabled(false);
			int opcao = fileChooser.showOpenDialog(frame);
			if (opcao == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				arquivoEntradaField.setValue(file);
			}
		});

		JLabel arquivoSaidaLabel = new JLabel("Arquivo de Saída:");
		arquivoSaidaLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		arquivosPanel.add(arquivoSaidaLabel, "2, 4, right, default");

		arquivoSaidaField = new JFormattedTextField();
		arquivoSaidaField.setEditable(false);
		arquivosPanel.add(arquivoSaidaField, "4, 4, fill, default");

		JButton escolherArquivoSaidaButton = new JButton("Procurar...");
		escolherArquivoSaidaButton.setToolTipText("Selecionar o arquivo de saída");
		arquivosPanel.add(escolherArquivoSaidaButton, "6, 4");

		JLabel quantidadePosicoesLabel = new JLabel("Quantidade de Posições:");
		arquivosPanel.add(quantidadePosicoesLabel, "2, 6, right, default");

		quantidadePosicoesSpinner = new JSpinner();
		quantidadePosicoesSpinner.setToolTipText("Total de posições na parada musical");
		quantidadePosicoesSpinner.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		arquivosPanel.add(quantidadePosicoesSpinner, "4, 6");
		escolherArquivoSaidaButton.addActionListener(event -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
			fileChooser.setMultiSelectionEnabled(false);
			int opcao = fileChooser.showSaveDialog(frame);
			if (opcao == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				arquivoSaidaField.setValue(file);
			}
		});

		JPanel botoesPanel = new JPanel();
		frame.getContentPane().add(botoesPanel, "2, 8, 7, 1, fill, fill");

		JButton executarButton = new JButton("Executar");
		executarButton.addActionListener(event -> {
			gerarRelatorios();
		});
		botoesPanel.add(executarButton);
	}

	@PostConstruct
	protected void init() {
		frame.getContentPane().add(padroesArquivoPanel, "2, 4, 7, 1, fill, fill");
		frame.getContentPane().add(relatoriosConfigPanel, "2, 6, 7, 1, fill, fill");
		arquivoEntradaField.setFormatterFactory(new DefaultFormatterFactory(arquivoFormatter));

		frame.pack();
	}

	private void gerarRelatorios() {
		File arquivoEntrada = (File) arquivoEntradaField.getValue();
		File arquivoSaida = (File) arquivoSaidaField.getValue();

		if (arquivoEntrada == null) {
			JOptionPane.showMessageDialog(MainWindow.this.frame, "Informe o arquivo a ser analizado!", "Informar arquivo",
					JOptionPane.WARNING_MESSAGE);
		} else if (arquivoSaida == null) {
			JOptionPane.showMessageDialog(MainWindow.this.frame, "Informe o arquivo de saída!", "Informar arquivo", JOptionPane.WARNING_MESSAGE);
		} else {
			try {
				GeracaoRelatoriosWorker worker = geradorRelatorios.get();
				worker.setArquivoEntrada(arquivoEntrada);
				worker.setArquivoSaida(arquivoSaida);
				worker.setQuantidadePosicoes((int) quantidadePosicoesSpinner.getValue());
				worker.execute();
				Path arquivoRelatorios = worker.get();
				JOptionPane.showMessageDialog(MainWindow.this.frame, "Relatórios gerados com sucesso:\n\n" + arquivoRelatorios, "Sucesso",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (InterruptedException | ExecutionException exception) {
				logger.error("Erro ao gravar arquivo de relatórios", exception);
				JOptionPane.showMessageDialog(MainWindow.this.frame,
						"Erro ao gravar arquivo de relatórios" + ":\n\n" + exception.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new MainWindow().show();
	}

	public void show() {
		frame.setVisible(true);
	}

	public void close() {
		frame.setVisible(false);
	}

	public JFrame getFrame() {
		return frame;
	}
}
