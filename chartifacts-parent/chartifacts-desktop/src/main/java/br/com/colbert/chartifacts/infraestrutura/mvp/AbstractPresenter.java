package br.com.colbert.chartifacts.infraestrutura.mvp;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.SwingUtilities;

import org.mvp4j.AppController;
import org.slf4j.Logger;

/**
 * Implementação-base para todas as classes que implementem {@link Presenter}.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 * 
 * @param <V>
 *            o tipo de <em>View</em> associada
 */
public abstract class AbstractPresenter<V extends View> implements Presenter<V> {

	private static final long serialVersionUID = -3649613414986016998L;

	@Inject
	protected transient Logger logger;

	@Inject
	private transient AppController appController;
	@Inject
	private transient MensagensService mensagensService;

	/**
	 * A <em>view</em> atualmente associada à esta instância.
	 */
	protected V view;

	/**
	 * Cria uma nova instância informando qual a <em>view</em> associada a ele.
	 * 
	 * @param view
	 */
	public AbstractPresenter(V view) {
		this.view = view;
	}

	/**
	 * Efetua a associação dos métodos do <em>presenter</em> com os componentes visuais da <em>view</em>.
	 */
	@PostConstruct
	protected void doBindings() {
		appController.bindPresenter(view, this);
	}

	/**
	 * Exibe uma mensagem informativa na tela.
	 * 
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @param titulo
	 *            título da mensagem
	 * @see MensagensService#mostrarMensagemInformativa(View, Object, String)
	 */
	protected void mostrarMensagemInformativa(Object mensagem, String titulo) {
		SwingUtilities.invokeLater(() -> mensagensService.mostrarMensagemInformativa(view, mensagem, titulo));
	}

	/**
	 * Exibe uma mensagem de alerta na tela.
	 * 
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @param titulo
	 *            título da mensagem
	 * @see MensagensService#mostrarMensagemAlerta(View, Object, String)
	 */
	protected void mostrarMensagemAlerta(Object mensagem, String titulo) {
		SwingUtilities.invokeLater(() -> mensagensService.mostrarMensagemAlerta(view, mensagem, titulo));
	}

	/**
	 * Exibe uma mensagem de erro na tela.
	 * 
	 * @param mensagem
	 *            conteúdo da mensagem
	 * @param titulo
	 *            título da mensagem
	 * @see MensagensService#mostrarMensagemErro(View, Object, String)
	 */
	protected void mostrarMensagemErro(Object mensagem, String titulo) {
		SwingUtilities.invokeLater(() -> mensagensService.mostrarMensagemErro(view, mensagem, titulo));
	}

	@Override
	public void start() {
		logger.debug("Iniciando");
		view.show();
	}

	@Override
	public V getView() {
		return view;
	}
}
