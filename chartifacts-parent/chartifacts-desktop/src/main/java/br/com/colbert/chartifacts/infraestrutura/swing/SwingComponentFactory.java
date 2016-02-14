package br.com.colbert.chartifacts.infraestrutura.swing;

import java.awt.*;
import java.io.Serializable;

import javax.swing.*;

/**
 * Fábrica de componentes Swing padrão.
 * 
 * @author ThiagoColbert
 * @since 14 de fev de 2016
 */
public final class SwingComponentFactory implements Serializable {

	private static final long serialVersionUID = 4970183963439700690L;

	private static final Font COMMON_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 11);
	private static final Font HIGHLIGHTS_LABEL_FONT = new Font("Tahoma", Font.BOLD, 11);
	private static final Font PRIMARY_BUTTON_FONT = new Font("Tahoma", Font.BOLD, 11);
	private static final Font SECONDARY_BUTTON_FONT = COMMON_LABEL_FONT;
	private static final Font COMMON_TEXT_FIELD_FONT = COMMON_LABEL_FONT;

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source text "Label"
	 */
	public static JLabel createCommonJLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(COMMON_LABEL_FONT);
		return label;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source text "Label"
	 */
	public static JLabel createHighlightsJLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(HIGHLIGHTS_LABEL_FONT);
		return label;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source text "Button"
	 * @wbp.factory.parameter.source toolTipText "Tooltip"
	 */
	public static JButton createPrimaryJButton(String text, String toolTipText) {
		JButton button = new JButton(text);
		button.setFont(PRIMARY_BUTTON_FONT);
		button.setToolTipText(toolTipText);
		return button;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source text "Button"
	 * @wbp.factory.parameter.source toolTipText "Tooltip"
	 */
	public static JButton createSecondaryJButton(String text, String toolTipText) {
		JButton button = new JButton(text);
		button.setFont(SECONDARY_BUTTON_FONT);
		button.setToolTipText(toolTipText);
		return button;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source columns 50
	 */
	public static JFormattedTextField createCommonJFormattedTextField(int columns) {
		JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.setFont(COMMON_TEXT_FIELD_FONT);
		formattedTextField.setColumns(columns);
		return formattedTextField;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source text "Button"
	 */
	public static JToggleButton createSecondaryJToggleButton(String text) {
		JToggleButton toggleButton = new JToggleButton(text);
		toggleButton.setFont(SECONDARY_BUTTON_FONT);
		return toggleButton;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source toolTipText "Tooltip"
	 * @wbp.factory.parameter.source columns 10
	 */
	public static JTextField createCommonJTextField(String toolTipText, int columns) {
		JTextField textField = new JTextField();
		textField.setFont(COMMON_TEXT_FIELD_FONT);
		textField.setToolTipText(toolTipText);
		textField.setColumns(columns);
		return textField;
	}

	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source model new javax.swing.SpinnerNumberModel(new java.lang.Integer(0), new java.lang.Integer(0), null, new
	 *                               java.lang.Integer(1))
	 * @wbp.factory.parameter.source toolTipText "Tooltip"
	 */
	public static JSpinner createCommonJSpinner(SpinnerModel model, String toolTipText) {
		JSpinner spinner = new JSpinner();
		spinner.setFont(COMMON_TEXT_FIELD_FONT);
		spinner.setModel(model);
		spinner.setToolTipText(toolTipText);
		spinner.setPreferredSize(new Dimension(50, 20));
		return spinner;
	}
}