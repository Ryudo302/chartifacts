package br.com.colbert.chartifacts.ui;

import java.io.File;
import java.text.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * {@link AbstractFormatter} responsável pela conversão entre visão e modelo de um arquivo.
 *
 * @author Thiago Colbert
 * @since 10/04/2015
 */
@ApplicationScoped
public class ArquivoFormatter extends JFormattedTextField.AbstractFormatter {

	private static final long serialVersionUID = 3574659792038042348L;

	@Inject
	private transient Logger logger;

	@Override
	public String valueToString(Object value) throws ParseException {
		logger.trace("To String: {}", value);

		if (value == null) {
			return null;
		} else if (value instanceof File) {
			return ((File) value).getAbsolutePath();
		} else {
			return value.toString();
		}
	}

	@Override
	public Object stringToValue(String text) throws ParseException {
		logger.trace("To File: {}", text);

		if (StringUtils.isBlank(text)) {
			return null;
		} else {
			return new File(text);
		}
	}
}
