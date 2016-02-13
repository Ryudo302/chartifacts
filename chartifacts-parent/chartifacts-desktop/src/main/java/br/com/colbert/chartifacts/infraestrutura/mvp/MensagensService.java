package br.com.colbert.chartifacts.infraestrutura.mvp;

import java.awt.Container;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.JOptionPane;

/**
 * Serviço que permite a exibição de mensagens na tela.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 */
@ApplicationScoped
class MensagensService implements Serializable {

	private static final long serialVersionUID = 811363282648383599L;

	/**
	 * Exibe uma mensagem informativa na tela.
	 * 
	 * @param parentView
	 *            {@link View} sobre a qual a mensagem será exibida
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @param titulo
	 *            título da janela contendo a mensagem
	 */
	public void mostrarMensagemInformativa(View parentView, Object mensagem, String titulo) {
		mostrarMensagem(parentView.getAwtContainer(), mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Exibe uma mensagem de alerta na tela.
	 * 
	 * @param parentView
	 *            {@link View} sobre a qual a mensagem será exibida
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @param titulo
	 *            título da janela contendo a mensagem
	 */
	public void mostrarMensagemAlerta(View parentView, Object mensagem, String titulo) {
		mostrarMensagem(parentView.getAwtContainer(), mensagem, titulo, JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Exibe uma mensagem de erro na tela.
	 * 
	 * @param parentView
	 *            {@link View} sobre a qual a mensagem será exibida
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @param titulo
	 *            título da janela contendo a mensagem
	 */
	public void mostrarMensagemErro(View parentView, Object mensagem, String titulo) {
		mostrarMensagem(parentView.getAwtContainer(), mensagem, titulo, JOptionPane.ERROR_MESSAGE);
	}

	private void mostrarMensagem(Container container, Object mensagem, String titulo, int tipo) {
		JOptionPane.showMessageDialog(container, mensagem, titulo, tipo);
	}
}
