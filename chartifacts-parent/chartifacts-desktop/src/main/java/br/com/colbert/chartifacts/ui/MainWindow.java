package br.com.colbert.chartifacts.ui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

import org.mvp4j.annotation.*;
import org.mvp4j.annotation.Action;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.aplicacao.MainPresenter;

/**
 * Janela principal da aplicação.
 *
 * TODO Criar Presenter
 *
 * @author Thiago Colbert
 * @since 10/04/2015
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = MainPresenter.class)
public class MainWindow implements Serializable {

	private static final long serialVersionUID = 1998102092578023349L;

	private final JFrame frame;

	@Inject
	private PadroesArquivoEntradaPanel padroesArquivoPanel;
	@Inject
	private RelatoriosConfigPanel relatoriosConfigPanel;
	@Inject
	private ArquivoFormatter arquivoFormatter;

	private final JFormattedTextField arquivoEntradaField;
	private final JFormattedTextField arquivoSaidaField;
	private final JSpinner quantidadePosicoesSpinner;

	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "sobre")
	private final JMenuItem menuItemSobre;
	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "gerarRelatorios")
	private final JButton executarButton;
	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "escolherArquivoEntrada")
	private final JButton escolherArquivoEntradaButton;
	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "escolherArquivoSaida")
	private final JButton escolherArquivoSaidaButton;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		frame = new JFrame("Chartifacts");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		frame.setBounds(100, 100, 510, 229);
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

		menuItemSobre = new JMenuItem("Sobre");
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
		arquivoEntradaLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		arquivosPanel.add(arquivoEntradaLabel, "2, 2, right, center");

		arquivoEntradaField = new JFormattedTextField();
		arquivoEntradaField.setColumns(50);
		arquivosPanel.add(arquivoEntradaField, "4, 2");
		arquivoEntradaField.setEditable(false);

		escolherArquivoEntradaButton = new JButton("Procurar...");
		arquivosPanel.add(escolherArquivoEntradaButton, "6, 2");
		escolherArquivoEntradaButton.setToolTipText("Selecionar o arquivo de histórico");

		JLabel arquivoSaidaLabel = new JLabel("Arquivo de Saída:");
		arquivoSaidaLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		arquivosPanel.add(arquivoSaidaLabel, "2, 4, right, default");

		arquivoSaidaField = new JFormattedTextField();
		arquivoSaidaField.setColumns(50);
		arquivoSaidaField.setEditable(false);
		arquivosPanel.add(arquivoSaidaField, "4, 4, fill, default");

		escolherArquivoSaidaButton = new JButton("Procurar...");
		escolherArquivoSaidaButton.setToolTipText("Selecionar o arquivo de saída");
		arquivosPanel.add(escolherArquivoSaidaButton, "6, 4");

		JLabel quantidadePosicoesLabel = new JLabel("Quantidade de Posições:");
		quantidadePosicoesLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		arquivosPanel.add(quantidadePosicoesLabel, "2, 6, right, default");

		quantidadePosicoesSpinner = new JSpinner();
		quantidadePosicoesSpinner.setToolTipText("Total de posições na parada musical");
		quantidadePosicoesSpinner.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		arquivosPanel.add(quantidadePosicoesSpinner, "4, 6");

		JPanel botoesPanel = new JPanel();
		frame.getContentPane().add(botoesPanel, "2, 8, 7, 1, fill, fill");

		executarButton = new JButton("Executar");
		botoesPanel.add(executarButton);
	}

	@PostConstruct
	protected void init() {
		frame.getContentPane().add(padroesArquivoPanel, "2, 4, 7, 1, fill, fill");
		frame.getContentPane().add(relatoriosConfigPanel, "2, 6, 7, 1, fill, fill");
		arquivoEntradaField.setFormatterFactory(new DefaultFormatterFactory(arquivoFormatter));

		frame.pack();
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

	public File getArquivoEntrada() {
		return (File) arquivoEntradaField.getValue();
	}

	public void setArquivoEntrada(File arquivo) {
		arquivoEntradaField.setValue(arquivo);
	}

	public File getArquivoSaida() {
		return (File) arquivoSaidaField.getValue();
	}

	public void setArquivoSaida(File arquivo) {
		arquivoSaidaField.setValue(arquivo);
	}

	public int getQuantidadePosicoes() {
		return (int) quantidadePosicoesSpinner.getValue();
	}
}
