package br.com.colbert.chartifacts.aplicacao;

import java.awt.Dimension;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import br.com.colbert.chartifacts.infraestrutura.aplicacao.InformacoesAplicacao;
import br.com.colbert.chartifacts.infraestrutura.io.*;
import br.com.colbert.chartifacts.infraestrutura.mvp.AbstractPresenter;
import br.com.colbert.chartifacts.infraestrutura.swing.HTMLMessage;
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
	private static final String ARQUIVO_HTML_SOBRE = "sobre.html";

	@Inject
	private transient InformacoesAplicacao informacoesAplicacao;
	@Inject
	private transient RelatoriosPresenter relatoriosPresenter;
	@Inject
	private transient ImagensRepository imagensRepository;
	@Inject
	private transient HtmlTemplateRepository htmlTemplateRepository;

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

		try {
			String conteudoHtml = htmlTemplateRepository.carregarTemplate(ARQUIVO_HTML_SOBRE, informacoesAplicacao.getNome().toUpperCase(),
					informacoesAplicacao.getVersao(), informacoesAplicacao.getNumeroBuild(), informacoesAplicacao.getAutor()).get();
			logger.debug("Template carregado:\n{}", conteudoHtml);
			mostrarMensagemInformativa(new HTMLMessage(conteudoHtml), MessageFormat.format("Sobre {0}", informacoesAplicacao.getNome()),
					imagensRepository.recuperarIcone(CAMINHO_ARQUIVO_ICONE_APLICACAO, Optional.of(new Dimension(65, 60))));
		} catch (IOException exception) {
			logger.error("Erro ao carregar template", exception);
			mostrarMensagemErro("Erro ao carregar conteúdo da janela:\n\n" + exception.getLocalizedMessage(), informacoesAplicacao.getNome());
		}
	}
}
