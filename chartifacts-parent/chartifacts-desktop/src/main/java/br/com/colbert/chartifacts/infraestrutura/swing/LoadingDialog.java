package br.com.colbert.chartifacts.infraestrutura.swing;

import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.io.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.swing.*;

import br.com.colbert.chartifacts.ui.MainWindow;

/**
 * Tela exibida quando é aguardada a execução de uma tarefa em <em>background</em>.
 *
 * @author Thiago Colbert
 * @since 09/01/2015
 */
@ApplicationScoped
@LoadingView
public class LoadingDialog implements Serializable {

	private static final long serialVersionUID = 8248885596292025385L;

	private JDialog dialog;

	@Inject
	private MainWindow mainWindow;

	public static void main(String[] args) throws IOException {
		new LoadingDialog().init();
	}

	@PostConstruct
	protected void init() throws IOException {
		dialog = new JDialog(mainWindow != null ? mainWindow.getFrame() : null, true);
		dialog.setUndecorated(true);
		dialog.setPreferredSize(new Dimension(200, 230));
		dialog.setResizable(false);
		dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dialog.setLocationRelativeTo(mainWindow != null ? mainWindow.getFrame() : null);

		dialog.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialog.setVisible(false);
				}
			}
		});

		JLabel imagemLabel = new JLabel();
		imagemLabel.setIcon(Beans.isDesignTime() ? null : new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/carregando.gif")));
		dialog.getContentPane().add(imagemLabel, BorderLayout.CENTER);

		dialog.pack();
	}

	public void close() {
		dialog.setVisible(false);
	}

	public void show() {
		dialog.setVisible(true);
	}

	@Produces
	@LoadingView
	public Window getWindow() {
		return dialog;
	}
}
