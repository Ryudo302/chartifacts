package br.com.colbert.chartifacts.aplicacao;

import java.awt.Dimension;
import java.text.MessageFormat;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.infraestrutura.io.ImagensRepository;
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

	private static final String CAMINHO_ARQUIVO_ICONE_APLICACAO = "images/chart.png";

	@Inject
	private transient InformacoesAplicacao informacoesAplicacao;
	@Inject
	private transient RelatoriosPresenter relatoriosPresenter;
	@Inject
	private transient ImagensRepository imagensRepository;

	@Inject
	public MainPresenter(MainWindow view) {
		super(view);
	}

	@PostConstruct
	protected void init() {
		view.setIcone(imagensRepository.recuperarImagem(CAMINHO_ARQUIVO_ICONE_APLICACAO, Optional.empty()).get());
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
				MessageFormat.format("{0}\n\nVersão: {1}\nBuild: {2}\nDesenvolvido por: {3}", informacoesAplicacao.getNome().toUpperCase(),
						informacoesAplicacao.getVersao(), informacoesAplicacao.getNumeroBuild(), informacoesAplicacao.getAutor()),
				MessageFormat.format("Sobre {0}", informacoesAplicacao.getNome()),
				imagensRepository.recuperarIcone(CAMINHO_ARQUIVO_ICONE_APLICACAO, Optional.of(new Dimension(55, 50))));
	}
}
