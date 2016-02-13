package br.com.colbert.chartifacts.aplicacao;

import java.text.MessageFormat;

import javax.inject.Inject;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.infraestrutura.mvp.AbstractPresenter;
import br.com.colbert.chartifacts.ui.MainWindow;

/**
 * <em>Presenter</em> da tela principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 21/04/2015
 */
public class MainPresenter extends AbstractPresenter<MainWindow> {

	private static final long serialVersionUID = -2190040873391147767L;

	@Inject
	private transient InformacoesAplicacao informacoesAplicacao;
	@Inject
	private transient RelatoriosPresenter relatoriosPresenter;

	@Inject
	public MainPresenter(MainWindow view) {
		super(view);
	}

	/**
	 * Exibe a tela de gerenciamento de relatórios.
	 */
	public void relatorios() {
		logger.info("Relatórios");
		relatoriosPresenter.start();
		view.setContentView(relatoriosPresenter.getView());
	}

	/**
	 * Exibe informações sobre a aplicação.
	 */
	public void sobre() {
		logger.info("Sobre");
		mostrarMensagemInformativa(
				MessageFormat.format("{0}\n\nVersão: {1}\nBuild: {2}\nDesenvolvido por: {3}", informacoesAplicacao.getNome(),
						informacoesAplicacao.getVersao(), informacoesAplicacao.getNumeroBuild(), informacoesAplicacao.getAutor()),
				MessageFormat.format("Sobre {0}", informacoesAplicacao.getNome()));
	}
}
