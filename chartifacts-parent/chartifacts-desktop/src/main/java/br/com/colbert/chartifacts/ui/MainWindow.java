package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.io.*;
import java.nio.file.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.dominio.chart.*;
import br.com.colbert.chartifacts.dominio.relatorios.*;
import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.infraestrutura.io.*;

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
	private final JFormattedTextField arquivoField;

	@Inject
	private InformacoesAplicacao informacoesAplicacao;
	@Inject
	private ArquivoFormatter arquivoFormatter;
	@Inject
	private HistoricoParadaFileParser historicoParadaFileParser;

	@Inject
	private StringParsersConfig parsersConfig;
	@Inject
	private RelatoriosConfiguration relatoriosConfig;

	@Inject
	private RelatoriosFacade relatoriosFacade;

	private final JTextField nomeArtistaField;
	private final JTextField separadoresArtistasField;
	private final JTextField separadorArtistaCancaoField;
	private final JTextField tituloCancaoField;
	private final JTextField separadorTitulosAlternativosField;
	private final JTextField separadorPosicoesChartRunField;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		frame = new JFrame("Chartifacts");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.setBounds(100, 100, 510, 470);
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
								RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
								FormSpecs.RELATED_GAP_ROWSPEC, }));

		JLabel arquivoLabel = new JLabel("Arquivo:");
		arquivoLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.getContentPane().add(arquivoLabel, "2, 2, right, center");

		arquivoField = new JFormattedTextField();
		arquivoField.setToolTipText("Arquivo de histórico a ser utilizado.");

		arquivoField.setEditable(false);
		frame.getContentPane().add(arquivoField, "4, 2, fill, default");

		JButton botaoProcurarArquivo = new JButton("Procurar...");
		botaoProcurarArquivo.setToolTipText("Selecionar o arquivo de histórico.");
		botaoProcurarArquivo.addActionListener(event -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
			fileChooser.setMultiSelectionEnabled(false);
			int opcao = fileChooser.showOpenDialog(frame);
			if (opcao == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				arquivoField.setValue(file);
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
		nomeArtistaField.setEditable(false);
		nomeArtistaField.setToolTipText("Regex utilizada para identificar os nomes dos artistas");
		padroesArquivoPanelContent.add(nomeArtistaField, "4, 2, fill, default");
		nomeArtistaField.setColumns(10);

		JLabel separadoresArtistasLabel = new JLabel("Separadores de Artistas:");
		padroesArquivoPanelContent.add(separadoresArtistasLabel, "2, 4, right, default");

		separadoresArtistasField = new JTextField();
		separadoresArtistasField.setEditable(false);
		separadoresArtistasField.setToolTipText("Regex utilizada para identificar segmentos de texto que separem um artista do outro");
		separadoresArtistasField.setColumns(10);
		padroesArquivoPanelContent.add(separadoresArtistasField, "4, 4, fill, default");

		JLabel separadorArtistaCancaoLabel = new JLabel("Separador Artista/Canção:");
		padroesArquivoPanelContent.add(separadorArtistaCancaoLabel, "2, 6, right, default");

		separadorArtistaCancaoField = new JTextField();
		separadorArtistaCancaoField.setEditable(false);
		separadorArtistaCancaoField
				.setToolTipText("Regex utilizada para identificar segmentos de texto que separem os nomes de artistas do título da canção");
		separadorArtistaCancaoField.setColumns(10);
		padroesArquivoPanelContent.add(separadorArtistaCancaoField, "4, 6, fill, default");

		JLabel tituloCancaoLabel = new JLabel("Título de Canção:");
		padroesArquivoPanelContent.add(tituloCancaoLabel, "2, 8, right, default");

		tituloCancaoField = new JTextField();
		tituloCancaoField.setEditable(false);
		tituloCancaoField.setToolTipText("Regex utilizada para identificar o título da canção");
		tituloCancaoField.setColumns(10);
		padroesArquivoPanelContent.add(tituloCancaoField, "4, 8, fill, default");

		JLabel separadorTitulosAlternativosLabel = new JLabel("Separador de Títulos Alternativos:");
		padroesArquivoPanelContent.add(separadorTitulosAlternativosLabel, "2, 10, right, default");

		separadorTitulosAlternativosField = new JTextField();
		separadorTitulosAlternativosField.setEditable(false);
		separadorTitulosAlternativosField.setToolTipText("Regex utilizada para identificar títulos alternativos da canção");
		separadorTitulosAlternativosField.setColumns(10);
		padroesArquivoPanelContent.add(separadorTitulosAlternativosField, "4, 10, fill, default");

		JLabel separadorPosicoesChartRunLabel = new JLabel("Separador de Posições de Chart-run:");
		padroesArquivoPanelContent.add(separadorPosicoesChartRunLabel, "2, 12, right, default");

		separadorPosicoesChartRunField = new JTextField();
		separadorPosicoesChartRunField.setEditable(false);
		separadorPosicoesChartRunField.setToolTipText("Expressão utilizada para separar as posições dos chart-runs");
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

		JPanel botoesPanel = new JPanel();
		frame.getContentPane().add(botoesPanel, "2, 8, 5, 1, fill, fill");

		JButton executarButton = new JButton("Executar");
		executarButton.addActionListener(event -> {
			gerarRelatorios();
		});
		botoesPanel.add(executarButton);
	}

	private void gerarRelatorios() {
		File arquivo = (File) arquivoField.getValue();
		if (arquivo == null) {
			JOptionPane
					.showMessageDialog(MainWindow.this.frame, "Informe o arquivo a ser analizado", "Informar arquivo", JOptionPane.WARNING_MESSAGE);
		} else {
			try {
				HistoricoParada historicoParada = historicoParadaFileParser.parse(arquivo, 20);
				// TODO Receber arquivo de saída da tela
				Path arquivoRelatorios = Files
						.write(Paths.get(".", "relatorio.txt"), relatoriosFacade.exportarTodosEmTxt(historicoParada).getBytes());
				JOptionPane.showMessageDialog(MainWindow.this.frame, "Relatórios gerados com sucesso:\n\n" + arquivoRelatorios, "Sucesso",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (ParserException exception) {
				JOptionPane.showMessageDialog(MainWindow.this.frame, "Erro ao analizar arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
			} catch (IOException exception) {
				JOptionPane.showMessageDialog(MainWindow.this.frame, "Erro ao gravar arquivo de relatórios", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@PostConstruct
	protected void init() {
		arquivoField.setFormatterFactory(new DefaultFormatterFactory(arquivoFormatter));
		nomeArtistaField.setText(parsersConfig.nomeArtistaPattern().pattern());
		separadoresArtistasField.setText(parsersConfig.separadoresArtistasPattern().pattern());
		separadorArtistaCancaoField.setText(parsersConfig.separadorArtistaCancaoPattern().pattern());
		tituloCancaoField.setText(parsersConfig.tituloCancaoPattern().pattern());
		separadorTitulosAlternativosField.setText(parsersConfig.titulosAlternativosCancaoSeparadorPattern().pattern());
		separadorPosicoesChartRunField.setText(parsersConfig.separadorPosicoesChartRun());
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
