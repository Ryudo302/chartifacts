package br.com.colbert.chartifacts.infraestrutura.swing;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.swing.*;

import br.com.colbert.chartifacts.infraestrutura.io.ImagensRepository;
import br.com.colbert.chartifacts.infraestrutura.mvp.View;
import br.com.colbert.chartifacts.ui.MainWindow;

/**
 * Tela exibida quando é aguardada a execução de uma tarefa em <em>background</em>.
 *
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@ApplicationScoped
@LoadingView
public class LoadingDialog implements View {

	private static final long serialVersionUID = 8248885596292025385L;

	private static final int LARGURA_PADRAO = 200;
	private static final int ALTURA_PADRAO = 230;
	private static final String ARQUIVO_IMAGEM_LOADING = "images/carregando.gif";

	private JDialog dialog;

	@Inject
	private transient MainWindow mainWindow;
	@Inject
	private transient ImagensRepository imagensRepository;

	public static void main(String[] args) throws IOException {
		new LoadingDialog().init();
	}

	@PostConstruct
	protected void init() throws IOException {
		dialog = new JDialog(mainWindow != null ? (Frame) mainWindow.getAwtContainer() : null, true);
		dialog.setUndecorated(true);
		dialog.setPreferredSize(new Dimension(LARGURA_PADRAO, ALTURA_PADRAO));
		dialog.setResizable(false);
		dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dialog.setLocationRelativeTo(mainWindow != null ? mainWindow.getAwtContainer() : null);

		dialog.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (isTeclaEsc(event)) {
					close();
				}
			}

			private boolean isTeclaEsc(KeyEvent event) {
				return event.getKeyCode() == KeyEvent.VK_ESCAPE;
			}
		});

		JLabel imagemLabel = new JLabel(imagensRepository.recuperarIcone(ARQUIVO_IMAGEM_LOADING, Optional.empty()).get());
		dialog.getContentPane().add(imagemLabel, BorderLayout.CENTER);

		dialog.pack();
	}

	@Override
	public Container getAwtContainer() {
		return getWindow();
	}

	@Produces
	@LoadingView
	public Window getWindow() {
		return dialog;
	}
}
