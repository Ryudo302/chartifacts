package br.com.colbert.chartifacts.ui;

import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;

import com.jgoodies.forms.layout.*;
import java.awt.Font;

/**
 * Janela principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 10/04/2015
 */
@Singleton
public class MainWindow implements Serializable {

	private static final long serialVersionUID = 1998102092578023349L;

	private final JFrame frame;

	private final JFormattedTextField textFieldArquivo;

	@Inject
	private ArquivoFormatter arquivoFormatter;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		frame = new JFrame("Chartifacts");
		frame.setBounds(100, 100, 510, 333);
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
		menuAjuda.add(menuItemSobre);
		frame.getContentPane().setLayout(
				new FormLayout(
						new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
								ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
								FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel labelArquivo = new JLabel("Arquivo:");
		labelArquivo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.getContentPane().add(labelArquivo, "2, 2, right, default");

		textFieldArquivo = new JFormattedTextField();
		textFieldArquivo.setToolTipText("Arquivo de histórico a ser utilizado.");

		textFieldArquivo.setEditable(false);
		frame.getContentPane().add(textFieldArquivo, "4, 2, fill, default");

		JButton botaoProcurarArquivo = new JButton("Procurar...");
		botaoProcurarArquivo.setToolTipText("Selecionar o arquivo de histórico.");
		botaoProcurarArquivo.addActionListener(event -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
			fileChooser.setMultiSelectionEnabled(false);
			int opcao = fileChooser.showOpenDialog(MainWindow.this.frame);
			if (opcao == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				textFieldArquivo.setValue(file);
			}
		});
		frame.getContentPane().add(botaoProcurarArquivo, "6, 2");
	}

	@PostConstruct
	protected void init() {
		textFieldArquivo.setFormatterFactory(new DefaultFormatterFactory(arquivoFormatter));
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
}
