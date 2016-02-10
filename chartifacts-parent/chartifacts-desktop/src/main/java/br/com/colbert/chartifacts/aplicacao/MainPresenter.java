package br.com.colbert.chartifacts.aplicacao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.mvp4j.AppController;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.ui.MainWindow;

/**
 * <em>Presenter</em> da tela principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 21/04/2015
 */
public class MainPresenter implements Serializable {

	private static final long serialVersionUID = -2190040873391147767L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient AppController appController;
	@Inject
	private transient InformacoesAplicacao informacoesAplicacao;

	@Inject
	private transient MainWindow mainWindow;
	
	@Inject
	private transient RelatoriosPresenter relatoriosPresenter;

	@PostConstruct
	protected void doBindings() {
		appController.bindPresenter(mainWindow, this);
	}

	public void start() {
		mainWindow.show();
	}

	public void relatorios() {
		relatoriosPresenter.start();
		mainWindow.setContent(relatoriosPresenter.getViewPanel());
	}

	public void sobre() {
		JOptionPane.showMessageDialog(mainWindow.getFrame(),
				informacoesAplicacao.getNome() + "\n\n" + "Versão: " + informacoesAplicacao.getVersao() + StringUtils.LF + "Build: "
						+ informacoesAplicacao.getNumeroBuild() + StringUtils.LF + "Desenvolvido por: " + informacoesAplicacao.getAutor(),
				"Sobre Chartifacts", JOptionPane.INFORMATION_MESSAGE);
	}
}
