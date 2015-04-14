package br.com.colbert.chartifacts.ui;

import java.awt.Font;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.*;
import javax.swing.border.TitledBorder;

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
	@InformacoesBuild(numeroBuild = false)
	private String versaoApp;
	@Inject
	@InformacoesBuild(numeroVersao = false)
	private String numeroBuildApp;
	@Inject
	@NomeAutorApp
	private String nomeAutorApp;

	@Inject
	private ArquivoFormatter arquivoFormatter;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		frame = new JFrame("Chartifacts");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
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
		menuItemSobre.addActionListener(event -> {
			JOptionPane.showMessageDialog(frame, "Chartifacts" + "\n\n" + "Versão: " + versaoApp + StringUtils.LF + "Build: " + numeroBuildApp
					+ StringUtils.LF + "Desenvolvido por: " + nomeAutorApp, "Sobre Chartifacts", JOptionPane.INFORMATION_MESSAGE);
		});
		menuAjuda.add(menuItemSobre);
		frame.getContentPane().setLayout(
				new FormLayout(
						new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
								ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
								FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC,
								RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, }));

		JLabel labelArquivo = new JLabel("Arquivo:");
		labelArquivo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.getContentPane().add(labelArquivo, "2, 2, right, center");

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
			int opcao = fileChooser.showOpenDialog(frame);
			if (opcao == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				textFieldArquivo.setValue(file);
			}
		});
		frame.getContentPane().add(botaoProcurarArquivo, "6, 2");

		JPanel padroesArquivoPanel = new JPanel();
		padroesArquivoPanel.setBorder(new TitledBorder(null, "Padr\u00F5es do Arquivo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(padroesArquivoPanel, "2, 4, 5, 1, fill, fill");

		JPanel relatoriosPanel = new JPanel();
		relatoriosPanel.setBorder(new TitledBorder(null, "Relat\u00F3rios", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(relatoriosPanel, "2, 6, 5, 1, fill, fill");
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
