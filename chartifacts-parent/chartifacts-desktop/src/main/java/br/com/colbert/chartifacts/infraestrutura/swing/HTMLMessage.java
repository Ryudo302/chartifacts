package br.com.colbert.chartifacts.infraestrutura.swing;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

/**
 * Componente que permite exibir um conteúdo em HTML.
 * 
 * @author Jean-Marc Astesana
 * @author ThiagoColbert
 * @since 30 de out de 2015
 * 
 * @see http://stackoverflow.com/a/33446134/3228529
 */
public class HTMLMessage extends JEditorPane {

	private static final long serialVersionUID = 509600211741317896L;

	private static final String MEDIA_TYPE_HTML = "text/html";

	/**
	 * Cria uma nova instância definindo o conteúdo em HTML.
	 * 
	 * @param htmlBody
	 *            o conteúdo HTML a ser exibido
	 */
	public HTMLMessage(String htmlBody) {
		super(MEDIA_TYPE_HTML, "<html><body style=\"" + getStyle() + "\">" + htmlBody + "</body></html>");

		addHyperlinkListener(event -> {
			if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
				try {
					Desktop.getDesktop().browse(getUri(event));
				} catch (IOException | UnsupportedOperationException | IllegalArgumentException exception) {
					// TODO Alterar para tipo específico de exceção
					throw new RuntimeException("Erro ao abrir link: " + event.getURL(), exception);
				}
			}
		});

		setEditable(false);
		setBorder(null);
	}

	private URI getUri(HyperlinkEvent event) {
		return Paths.get(event.getDescription()).toUri();
	}

	private static StringBuilder getStyle() {
		JLabel label = new JLabel();
		return toCSS(label.getFont(), label.getBackground());
	}

	private static StringBuilder toCSS(Font font, Color color) {
		StringBuilder style = new StringBuilder("font-family:" + font.getFamily() + ";");
		style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
		style.append("font-size:" + font.getSize() + "pt;");
		style.append("background-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");");
		return style;
	}
}
