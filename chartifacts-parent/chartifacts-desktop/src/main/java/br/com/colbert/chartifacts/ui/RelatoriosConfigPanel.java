package br.com.colbert.chartifacts.ui;

import java.awt.FlowLayout;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosConfiguration;

/**
 * Painel que exibe informações de um {@link RelatoriosConfiguration}.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
@Singleton
public class RelatoriosConfigPanel extends JPanel {

	private static final long serialVersionUID = -7418030376508842522L;

	@Inject
	private RelatoriosConfiguration relatoriosConfiguration;

	private final JSpinner limiteSpinner;

	/**
	 * Create the panel.
	 */
	public RelatoriosConfigPanel() {
		setBorder(new TitledBorder(null, "Relat\u00F3rios", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel togglePanel = new JPanel();
		FlowLayout layout = (FlowLayout) togglePanel.getLayout();
		layout.setAlignment(FlowLayout.LEFT);
		JPanel contentPanel = new JPanel();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(togglePanel);

		JToggleButton toggleRelatoriosButton = new JToggleButton("Ocultar");
		toggleRelatoriosButton.addActionListener(event -> {
			toggleRelatoriosButton.setText(toggleRelatoriosButton.isSelected() ? "Ocultar" : "Mostrar");
			contentPanel.setVisible(toggleRelatoriosButton.isSelected());
		});
		toggleRelatoriosButton.setSelected(true);
		togglePanel.add(toggleRelatoriosButton);

		contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		this.add(contentPanel);

		JLabel limiteLabel = new JLabel("Limite por Relatório:");
		contentPanel.add(limiteLabel, "2, 2, right, default");

		limiteSpinner = new JSpinner();
		limiteSpinner.setEnabled(false);
		limiteSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		limiteSpinner.setToolTipText("Quantidade máxima de itens a serem listados em cada relatório");
		contentPanel.add(limiteSpinner, "4, 2");
	}

	@PostConstruct
	protected void init() {
		limiteSpinner.setValue(relatoriosConfiguration.limiteTamanho());
	}
}
