package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.swing.*;

import org.mvp4j.annotation.Action;
import org.mvp4j.annotation.MVP;

import br.com.colbert.chartifacts.aplicacao.MainPresenter;
import br.com.colbert.chartifacts.infraestrutura.mvp.View;

/**
 * Janela principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 10/04/2015
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = MainPresenter.class)
public class MainWindow implements View {

	private static final long serialVersionUID = 1998102092578023349L;

	private final JFrame frame;

	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "sobre")
	private transient final JMenuItem menuItemSobre;
	@Action(EventAction = "actionPerformed", EventType = ActionListener.class, name = "relatorios")
	private transient final JMenuItem menuItemRelatorios;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		frame = new JFrame("Chartifacts");

		Container contentPane = frame.getContentPane();
		contentPane.setFont(new Font("Tahoma", Font.PLAIN, 12));

		frame.setBounds(100, 100, 510, 229);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		menuItemRelatorios = new JMenuItem("Relatórios");
		menuArquivo.add(menuItemRelatorios);

		menuArquivo.addSeparator();

		JMenuItem menuItemSair = new JMenuItem("Sair");
		menuItemSair.addActionListener(event -> MainWindow.this.close());
		menuArquivo.add(menuItemSair);

		JMenu menuAjuda = new JMenu("Ajuda");
		menuBar.add(menuAjuda);

		menuItemSobre = new JMenuItem("Sobre");
		menuAjuda.add(menuItemSobre);

		contentPane.setLayout(new CardLayout(1, 1));
		contentPane.setPreferredSize(new Dimension(400, 300));
	}

	@PostConstruct
	protected void init() {
		frame.pack();
	}

	@Override
	public Container getAwtContainer() {
		return frame;
	}

	/**
	 * 
	 * @param view
	 */
	public void setContentView(View view) {
		// TODO Não funciona
		/*String viewName = view.getName();
		Container contentPane = frame.getContentPane();
		CardLayout layout = (CardLayout) contentPane.getLayout();
		layout.addLayoutComponent(view.getAwtContainer(), viewName);
		layout.show(contentPane, viewName);
		contentPane.revalidate();
		frame.revalidate();
		frame.pack();*/

		frame.setContentPane(view.getAwtContainer());
		frame.revalidate();
		frame.pack();
	}

	@Override
	public void close() {
		this.frame.dispose();
	}
}
