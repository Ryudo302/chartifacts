package br.com.colbert.chartifacts.infraestrutura.swing;

import java.text.ParseException;
import java.util.regex.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * {@link AbstractFormatter} responsável pela conversão entre visão e modelo de um {@link Pattern}.
 * 
 * @author Thiago Miranda
 * @since 17 de fev de 2016
 */
@ApplicationScoped
public class PatternFormatter extends JFormattedTextField.AbstractFormatter {

	private static final long serialVersionUID = 2225625210651672147L;

	@Inject
	private transient Logger logger;

	@Override
	public String valueToString(Object value) throws ParseException {
		logger.trace("To String: {}", value);

		if (value == null) {
			return null;
		} else if (value instanceof Pattern) {
			return ((Pattern) value).pattern();
		} else {
			return value.toString();
		}
	}

	@Override
	public Pattern stringToValue(String text) throws ParseException {
		logger.trace("To File: {}", text);

		if (StringUtils.isBlank(text)) {
			return null;
		} else {
			return toPattern(text);
		}
	}

	private Pattern toPattern(String text) throws ParseException {
		try {
			return Pattern.compile(text);
		} catch (PatternSyntaxException exception) {
			throw new ParseException("Regex inválida: " + text, exception.getIndex());
		}
	}
}
