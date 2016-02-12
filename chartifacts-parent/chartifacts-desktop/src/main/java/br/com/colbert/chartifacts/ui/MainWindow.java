package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.swing.*;

import org.apache.commons.lang3.StringUtils;
import org.mvp4j.annotation.Action;
import org.mvp4j.annotation.MVP;

import br.com.colbert.chartifacts.aplicacao.MainPresenter;

/**
 * Janela principal da aplicação.
 *
 * @author Thiago Colbert
 * @since 10/04/2015
 */
@Singleton
@MVP(modelClass = Void.class, presenterClass = MainPresenter.class)
public class MainWindow implements Serializable {

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
		
		/*frame.getContentPane().setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC, }));*/
	}

	@PostConstruct
	protected void init() {
		frame.pack();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new MainWindow().show();
	}

	public void setContent(Container container) {
		String containerName = StringUtils.defaultIfBlank(container.getName(), container.getClass().getName() + '%' + container.hashCode());
		Container contentPane = frame.getContentPane();
		CardLayout layout = (CardLayout) contentPane.getLayout();
		layout.addLayoutComponent(container, containerName);
		layout.show(contentPane, containerName);
		contentPane.revalidate();
		frame.pack();
		
		// TODO Só funciona do modo abaixo
		/*frame.setContentPane(container);
		frame.revalidate();
		frame.pack();*/
	}

	public void show() {
		frame.setVisible(true);
	}

	public void close() {
		frame.setVisible(false);
	}

	public JFrame getFrame() {
		return frame;
	}
}
