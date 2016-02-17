package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

import org.mvp4j.annotation.Action;
import org.mvp4j.annotation.MVP;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.aplicacao.RelatoriosPresenter;
import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosConfiguration;
import br.com.colbert.chartifacts.infraestrutura.io.ImagensRepository;
import br.com.colbert.chartifacts.infraestrutura.mvp.*;
import br.com.colbert.chartifacts.infraestrutura.swing.*;

/**
 * Tela que permite a geração de relatórios da parada musical.
 *
 * @author Thiago Colbert
 * @since 27/04/2015
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = RelatoriosPresenter.class)
public class RelatoriosView implements View, Serializable {

	private static final long serialVersionUID = -4531644924905149366L;

	private static final class FramePackListener implements ViewFlexivelListener {

		private final Component component;

		public FramePackListener(Component component) {
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
	private transient PadroesArquivoEntradaViewPart padroesArquivoView;
	@Inject
	private transient RelatoriosConfigViewPart relatoriosConfigView;
	@Inject
	private transient ArquivoFormatter arquivoFormatter;
	@Inject
	private transient ImagensRepository imagensRepository;

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

		arquivoEntradaField = SwingComponentFactory.createCommonJFormattedTextField("Arquivo de histórico a ser analisado", 50);
		arquivoEntradaField.setEditable(false);
		topoPanel.add(arquivoEntradaField, "4, 2, fill, center");

		escolherArquivoEntradaButton = SwingComponentFactory.createSecondaryJButton("Procurar...", "Selecionar o arquivo de histórico");
		topoPanel.add(escolherArquivoEntradaButton, "6, 2, center, center");

		JLabel arquivoSaidaLabel = SwingComponentFactory.createHighlightsJLabel("Arquivo de Saída:");
		topoPanel.add(arquivoSaidaLabel, "2, 4, right, center");

		arquivoSaidaField = SwingComponentFactory.createCommonJFormattedTextField("Arquivo de saída dos relatórios gerados", 50);
		arquivoSaidaField.setEditable(false);
		topoPanel.add(arquivoSaidaField, "4, 4, fill, center");

		escolherArquivoSaidaButton = SwingComponentFactory.createSecondaryJButton("Procurar...", "Selecionar o arquivo de saída");
		topoPanel.add(escolherArquivoSaidaButton, "6, 4, center, center");

		JLabel quantidadePosicoesLabel = SwingComponentFactory.createHighlightsJLabel("Quantidade de Posições:");
		topoPanel.add(quantidadePosicoesLabel, "2, 6, right, center");

		quantidadePosicoesSpinner = SwingComponentFactory.createCommonJSpinner(new SpinnerNumberModel(new Integer(10), new Integer(0), null, new Integer(1)),
				"Total de posições na parada musical");
		topoPanel.add(quantidadePosicoesSpinner, "4, 6, left, center");

		JPanel botoesPanel = new JPanel();
		topoPanel.add(botoesPanel, "2, 8, 5, 1, fill, center");

		executarButton = SwingComponentFactory.createPrimaryJButton("Executar", "Gera todos os relatórios");
		botoesPanel.add(executarButton);

		JLabel arquivoEntradaLabel = SwingComponentFactory.createHighlightsJLabel("Arquivo de Entrada:");
		topoPanel.add(arquivoEntradaLabel, "2, 2, right, center");

		conteudoPanel.add(topoPanel);
	}

	@PostConstruct
	protected void init() {
		Container padroesArquivoContainer = padroesArquivoView.getAwtContainer();
		Container relatoriosConfigContainer = relatoriosConfigView.getAwtContainer();

		conteudoPanel.add(padroesArquivoContainer);
		conteudoPanel.add(relatoriosConfigContainer);

		padroesArquivoView.addViewFlexivelListener(new FramePackListener(padroesArquivoContainer));
		relatoriosConfigView.addViewFlexivelListener(new FramePackListener(relatoriosConfigContainer));

		arquivoEntradaField.setFormatterFactory(new DefaultFormatterFactory(arquivoFormatter));

		executarButton.setIcon(imagensRepository.recuperarIcone("images/executar.png", Optional.of(new Dimension(20, 20))).get());
	}

	@Override
	public Container getAwtContainer() {
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

	public RelatoriosConfiguration getRelatoriosConfiguration() {
		return relatoriosConfigView.getRelatoriosConfiguration();
	}
}
