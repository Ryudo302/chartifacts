package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.awt.event.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosConfiguration;
import br.com.colbert.chartifacts.infraestrutura.mvp.AbstractViewFlexivel;
import br.com.colbert.chartifacts.infraestrutura.properties.RelatoriosPropertiesConfiguration;
import br.com.colbert.chartifacts.infraestrutura.swing.SwingComponentFactory;

/**
 * Painel que exibe informações de um {@link RelatoriosConfiguration}.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
@Singleton
public class RelatoriosConfigViewPart extends AbstractViewFlexivel {

	private static final long serialVersionUID = -7418030376508842522L;

	@Inject
	private transient RelatoriosPropertiesConfiguration relatoriosConfiguration;

	private final JPanel conteudoPanel;

	private final JSpinner limiteSpinner;
	private final JTextField separadorTextField;
	private final JSpinner larguraPrimeiraColunaSpinner;

	/**
	 * Create the panel.
	 */
	public RelatoriosConfigViewPart() {
		conteudoPanel = new JPanel();
		conteudoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Relat\u00F3rios", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), new Color(0, 0, 0)));

		JPanel togglePanel = new JPanel();
		FlowLayout layout = (FlowLayout) togglePanel.getLayout();
		layout.setAlignment(FlowLayout.LEFT);
		JPanel contentPanel = new JPanel();
		conteudoPanel.setLayout(new BoxLayout(conteudoPanel, BoxLayout.Y_AXIS));

		conteudoPanel.add(togglePanel);

		JToggleButton toggleRelatoriosButton = SwingComponentFactory.createSecondaryJToggleButton("Ocultar");
		toggleRelatoriosButton.addActionListener(event -> {
			toggleRelatoriosButton.setText(toggleRelatoriosButton.isSelected() ? "Ocultar" : "Mostrar");
			contentPanel.setVisible(toggleRelatoriosButton.isSelected());
		});
		toggleRelatoriosButton.setSelected(true);
		togglePanel.add(toggleRelatoriosButton);

		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		conteudoPanel.add(contentPanel);

		JLabel limiteLabel = SwingComponentFactory.createCommonJLabel("Limite por relatório:");
		contentPanel.add(limiteLabel, "2, 2, right, center");

		limiteSpinner = SwingComponentFactory.createCommonJSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)),
				"Quantidade máxima de itens a serem listados em cada relatório");
		contentPanel.add(limiteSpinner, "4, 2, left, center");

		JLabel larguraPrimeiraColunaLabel = SwingComponentFactory.createCommonJLabel("Largura da primeira coluna:");
		contentPanel.add(larguraPrimeiraColunaLabel, "2, 4, right, center");

		larguraPrimeiraColunaSpinner = SwingComponentFactory.createCommonJSpinner(
				new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)),
				"Largura (em caracteres) da primeira coluna de cada relatório");
		contentPanel.add(larguraPrimeiraColunaSpinner, "4, 4, left, center");

		JLabel separadorLabel = SwingComponentFactory.createCommonJLabel("Separador:");
		contentPanel.add(separadorLabel, "2, 6, right, center");

		separadorTextField = SwingComponentFactory.createCommonJTextField("Texto utilizado para separar cada relatório dentro do arquivo gerado", 10);
		contentPanel.add(separadorTextField, "4, 6, fill, center");

		contentPanel.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentHidden(ComponentEvent event) {
				fireViewFlexivelEvent(false);
			}

			@Override
			public void componentShown(ComponentEvent event) {
				fireViewFlexivelEvent(true);
			}
		});
	}

	@PostConstruct
	protected void modelToView() {
		relatoriosConfiguration.limiteTamanho().ifPresent(limite -> limiteSpinner.setValue(limite));
		larguraPrimeiraColunaSpinner.setValue(relatoriosConfiguration.larguraPrimeiraColuna());
		separadorTextField.setText(relatoriosConfiguration.separador());
	}

	private void viewToModel() {
		relatoriosConfiguration.setLimiteTamanho((int) limiteSpinner.getValue());
		relatoriosConfiguration.setLarguraPrimeiraColuna((int) larguraPrimeiraColunaSpinner.getValue());
		relatoriosConfiguration.setSeparador(separadorTextField.getText());
	}

	@Override
	public Container getAwtContainer() {
		return conteudoPanel;
	}

	public RelatoriosConfiguration getRelatoriosConfiguration() {
		viewToModel();
		return relatoriosConfiguration;
	}
}
