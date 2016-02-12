package br.com.colbert.chartifacts.ui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

import org.mvp4j.annotation.Action;
import org.mvp4j.annotation.MVP;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.aplicacao.RelatoriosPresenter;
import br.com.colbert.chartifacts.infraestrutura.view.*;

/**
 * Tela que permite a geração de relatórios da parada musical.
 *
 * @author Thiago Colbert
 * @since 27/04/2015
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = RelatoriosPresenter.class)
public class RelatoriosView implements Serializable {

	private static final long serialVersionUID = -4531644924905149366L;

	private static final class FramePackListener implements ViewFlexivelListener {

		private final JPanel component;

		public FramePackListener(JPanel component) {
			this.component = component;
		}

		@Override
		public void viewReduzida(ViewFlexivelEvent event) {
			SwingUtilities.windowForComponent(component).pack();
		}

		@Override
		public void viewExpandida(ViewFlexivelEvent event) {
			SwingUtilities.windowForComponent(component).pack();
		}
	}

	private final JPanel conteudoPanel;

	@Inject
	private PadroesArquivoEntradaView padroesArquivoView;
	@Inject
	private RelatoriosConfigView relatoriosConfigView;
	@Inject
	private ArquivoFormatter arquivoFormatter;

	private final JFormattedTextField arquivoEntradaField;
	private final JFormattedTextField arquivoSaidaField;
	private final JSpinner quantidadePosicoesSpinner;

	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "gerarRelatorios")
	private final JButton executarButton;
	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "escolherArquivoEntrada")
	private final JButton escolherArquivoEntradaButton;
	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "escolherArquivoSaida")
	private final JButton escolherArquivoSaidaButton;

	/**
	 * Create the panel.
	 */
	public RelatoriosView() {
		conteudoPanel = new JPanel();
		conteudoPanel.setLayout(new BoxLayout(conteudoPanel, BoxLayout.Y_AXIS));

		JPanel topoPanel = new JPanel();
		topoPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, }));

		arquivoEntradaField = new JFormattedTextField();
		arquivoEntradaField.setColumns(50);
		topoPanel.add(arquivoEntradaField, "4, 2, fill, center");
		arquivoEntradaField.setEditable(false);

		escolherArquivoEntradaButton = new JButton("Procurar...");
		topoPanel.add(escolherArquivoEntradaButton, "6, 2, center, center");
		escolherArquivoEntradaButton.setToolTipText("Selecionar o arquivo de histórico");

		JLabel arquivoSaidaLabel = new JLabel("Arquivo de Saída:");
		arquivoSaidaLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		topoPanel.add(arquivoSaidaLabel, "2, 4, right, center");

		arquivoSaidaField = new JFormattedTextField();
		arquivoSaidaField.setColumns(50);
		arquivoSaidaField.setEditable(false);
		topoPanel.add(arquivoSaidaField, "4, 4, fill, center");

		escolherArquivoSaidaButton = new JButton("Procurar...");
		escolherArquivoSaidaButton.setToolTipText("Selecionar o arquivo de saída");
		topoPanel.add(escolherArquivoSaidaButton, "6, 4, center, center");

		JLabel quantidadePosicoesLabel = new JLabel("Quantidade de Posições:");
		quantidadePosicoesLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		topoPanel.add(quantidadePosicoesLabel, "2, 6, right, center");

		quantidadePosicoesSpinner = new JSpinner();
		quantidadePosicoesSpinner.setToolTipText("Total de posições na parada musical");
		quantidadePosicoesSpinner.setModel(new SpinnerNumberModel(new Integer(2), new Integer(2), null, new Integer(1)));
		topoPanel.add(quantidadePosicoesSpinner, "4, 6, fill, center");

		JPanel botoesPanel = new JPanel();
		topoPanel.add(botoesPanel, "2, 8, 5, 1, fill, center");

		executarButton = new JButton("Executar");
		botoesPanel.add(executarButton);

		JLabel arquivoEntradaLabel = new JLabel("Arquivo de Entrada:");
		arquivoEntradaLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		topoPanel.add(arquivoEntradaLabel, "2, 2, right, center");

		conteudoPanel.add(topoPanel);
	}

	@PostConstruct
	protected void init() {
		conteudoPanel.add(padroesArquivoView.getPanel());
		conteudoPanel.add(relatoriosConfigView.getPanel());

		padroesArquivoView.addViewFlexivelListener(new FramePackListener(padroesArquivoView.getPanel()));
		relatoriosConfigView.addViewFlexivelListener(new FramePackListener(relatoriosConfigView.getPanel()));

		arquivoEntradaField.setFormatterFactory(new DefaultFormatterFactory(arquivoFormatter));
	}

	public static void main(String[] args) {
		new RelatoriosView();
	}

	public JPanel getPanel() {
		return conteudoPanel;
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
