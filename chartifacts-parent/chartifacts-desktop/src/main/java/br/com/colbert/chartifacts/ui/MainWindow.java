package br.com.colbert.chartifacts.ui;

import java.awt.Font;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosConfiguration;
import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.infraestrutura.io.StringParsersConfig;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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
	private InformacoesAplicacao informacoesAplicacao;
	@Inject
	private ArquivoFormatter arquivoFormatter;

	@Inject
	private StringParsersConfig parsersConfig;
	@Inject
	private RelatoriosConfiguration relatoriosConfig;
	private final JTextField nomeArtistaField;
	private final JTextField separadoresArtistasField;
	private final JTextField separadorArtistaCancaoField;
	private final JTextField separadorTituloCancaoField;
	private final JTextField separadorTitulosAlternativosField;
	private final JTextField separadorPosicoesChartRunField;

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

		JPanel padroesArquivoPanelToggle = new JPanel();
		JPanel padroesArquivoPanelContent = new JPanel();
		padroesArquivoPanel.setLayout(new BoxLayout(padroesArquivoPanel, BoxLayout.Y_AXIS));

		FlowLayout flowLayout = (FlowLayout) padroesArquivoPanelToggle.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		padroesArquivoPanel.add(padroesArquivoPanelToggle);

		JToggleButton togglePadroesArquivoButton = new JToggleButton("Ocultar");
		togglePadroesArquivoButton.addActionListener(event -> {
			togglePadroesArquivoButton.setText(togglePadroesArquivoButton.isSelected() ? "Ocultar" : "Mostrar");
			padroesArquivoPanelContent.setVisible(togglePadroesArquivoButton.isSelected());
		});
		togglePadroesArquivoButton.setSelected(true);
		padroesArquivoPanelToggle.add(togglePadroesArquivoButton);

		padroesArquivoPanelContent.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		padroesArquivoPanel.add(padroesArquivoPanelContent);

		JLabel nomeArtistaLabel = new JLabel("Nome de Artista:");
		padroesArquivoPanelContent.add(nomeArtistaLabel, "2, 2, right, default");

		nomeArtistaField = new JTextField();
		padroesArquivoPanelContent.add(nomeArtistaField, "4, 2, fill, default");
		nomeArtistaField.setColumns(10);

		JLabel separadoresArtistasLabel = new JLabel("Separadores de Artistas:");
		padroesArquivoPanelContent.add(separadoresArtistasLabel, "2, 4, right, default");

		separadoresArtistasField = new JTextField();
		separadoresArtistasField.setColumns(10);
		padroesArquivoPanelContent.add(separadoresArtistasField, "4, 4, fill, default");

		JLabel separadorArtistaCancaoLabel = new JLabel("Separador Artista/Canção:");
		padroesArquivoPanelContent.add(separadorArtistaCancaoLabel, "2, 6, right, default");

		separadorArtistaCancaoField = new JTextField();
		separadorArtistaCancaoField.setColumns(10);
		padroesArquivoPanelContent.add(separadorArtistaCancaoField, "4, 6, fill, default");

		JLabel tituloCancaoLabel = new JLabel("Título de Canção:");
		padroesArquivoPanelContent.add(tituloCancaoLabel, "2, 8, right, default");

		separadorTituloCancaoField = new JTextField();
		separadorTituloCancaoField.setColumns(10);
		padroesArquivoPanelContent.add(separadorTituloCancaoField, "4, 8, fill, default");

		JLabel separadorTitulosAlternativosLabel = new JLabel("Separador de Títulos Alternativos:");
		padroesArquivoPanelContent.add(separadorTitulosAlternativosLabel, "2, 10, right, default");

		separadorTitulosAlternativosField = new JTextField();
		separadorTitulosAlternativosField.setColumns(10);
		padroesArquivoPanelContent.add(separadorTitulosAlternativosField, "4, 10, fill, default");

		JLabel separadorPosicoesChartRunLabel = new JLabel("Separador de Posições de Chart-run:");
		padroesArquivoPanelContent.add(separadorPosicoesChartRunLabel, "2, 12, right, default");

		separadorPosicoesChartRunField = new JTextField();
		separadorPosicoesChartRunField.setColumns(10);
		padroesArquivoPanelContent.add(separadorPosicoesChartRunField, "4, 12, fill, default");

		JPanel relatoriosPanel = new JPanel();
		relatoriosPanel.setBorder(new TitledBorder(null, "Relat\u00F3rios", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(relatoriosPanel, "2, 6, 5, 1, fill, fill");

		JPanel relatoriosPanelToggle = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) relatoriosPanelToggle.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		JPanel relatoriosPanelContent = new JPanel();
		relatoriosPanel.setLayout(new BoxLayout(relatoriosPanel, BoxLayout.Y_AXIS));

		relatoriosPanel.add(relatoriosPanelToggle);

		JToggleButton toggleRelatoriosButton = new JToggleButton("Ocultar");
		toggleRelatoriosButton.addActionListener(event -> {
			toggleRelatoriosButton.setText(toggleRelatoriosButton.isSelected() ? "Ocultar" : "Mostrar");
			relatoriosPanelContent.setVisible(toggleRelatoriosButton.isSelected());
		});
		toggleRelatoriosButton.setSelected(true);
		relatoriosPanelToggle.add(toggleRelatoriosButton);

		relatoriosPanelContent.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		relatoriosPanel.add(relatoriosPanelContent);
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
