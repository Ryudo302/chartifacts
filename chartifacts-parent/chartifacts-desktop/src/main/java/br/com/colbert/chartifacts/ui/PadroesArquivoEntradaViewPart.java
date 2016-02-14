package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.awt.event.*;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.infraestrutura.io.StringParsersConfig;
import br.com.colbert.chartifacts.infraestrutura.mvp.AbstractViewFlexivel;

/**
 * Painel contendo informações de um {@link StringParsersConfig}.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
@Singleton
public class PadroesArquivoEntradaViewPart extends AbstractViewFlexivel {

	private static final long serialVersionUID = -154918168549652861L;

	private final JPanel conteudoPanel;

	private final JTextField nomeArtistaField;
	private final JTextField separadoresArtistasField;
	private final JTextField separadorArtistaCancaoField;
	private final JTextField tituloCancaoField;
	private final JTextField separadorTitulosAlternativosField;
	private final JTextField separadorPosicoesChartRunField;

	@Inject
	private transient StringParsersConfig parsersConfig;

	/**
	 * Create the conteudoPanel.
	 */
	public PadroesArquivoEntradaViewPart() {
		conteudoPanel = new JPanel();
		conteudoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Padr\u00F5es do Arquivo", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), new Color(0, 0, 0)));

		JPanel togglePanel = new JPanel();
		JPanel contentPanel = new JPanel();
		conteudoPanel.setLayout(new BoxLayout(conteudoPanel, BoxLayout.Y_AXIS));

		FlowLayout flowLayout = (FlowLayout) togglePanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		conteudoPanel.add(togglePanel);

		JToggleButton togglePadroesArquivoButton = SwingComponentFactory.createSecondaryJToggleButton("Ocultar");
		togglePadroesArquivoButton.addActionListener(event -> {
			togglePadroesArquivoButton.setText(togglePadroesArquivoButton.isSelected() ? "Ocultar" : "Mostrar");
			contentPanel.setVisible(togglePadroesArquivoButton.isSelected());
		});
		togglePadroesArquivoButton.setSelected(true);
		togglePanel.add(togglePadroesArquivoButton);

		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		conteudoPanel.add(contentPanel);

		JLabel nomeArtistaLabel = SwingComponentFactory.createCommonJLabel("Nome de Artista:");
		contentPanel.add(nomeArtistaLabel, "2, 2, right, center");

		nomeArtistaField = SwingComponentFactory.createCommonJTextField("Regex utilizada para identificar os nomes dos artistas", 10);
		nomeArtistaField.setEditable(false);
		contentPanel.add(nomeArtistaField, "4, 2, fill, default");

		JLabel separadoresArtistasLabel = SwingComponentFactory.createCommonJLabel("Separadores de Artistas:");
		contentPanel.add(separadoresArtistasLabel, "2, 4, right, default");

		separadoresArtistasField = SwingComponentFactory
				.createCommonJTextField("Regex utilizada para identificar segmentos de texto que separem um artista do outro", 10);
		separadoresArtistasField.setEditable(false);
		contentPanel.add(separadoresArtistasField, "4, 4, fill, default");

		JLabel separadorArtistaCancaoLabel = SwingComponentFactory.createCommonJLabel("Separador Artista/Canção:");
		contentPanel.add(separadorArtistaCancaoLabel, "2, 6, right, default");

		separadorArtistaCancaoField = SwingComponentFactory.createCommonJTextField(
				"Regex utilizada para identificar segmentos de texto que separem os nomes de artistas do título da canção", 10);
		separadorArtistaCancaoField.setEditable(false);
		contentPanel.add(separadorArtistaCancaoField, "4, 6, fill, default");

		JLabel tituloCancaoLabel = SwingComponentFactory.createCommonJLabel("Título de Canção:");
		contentPanel.add(tituloCancaoLabel, "2, 8, right, default");

		tituloCancaoField = SwingComponentFactory.createCommonJTextField("Regex utilizada para identificar o título da canção", 10);
		tituloCancaoField.setEditable(false);
		contentPanel.add(tituloCancaoField, "4, 8, fill, default");

		JLabel separadorTitulosAlternativosLabel = SwingComponentFactory.createCommonJLabel("Separador de Títulos Alternativos:");
		contentPanel.add(separadorTitulosAlternativosLabel, "2, 10, right, default");

		separadorTitulosAlternativosField = SwingComponentFactory
				.createCommonJTextField("Regex utilizada para identificar títulos alternativos da canção", 10);
		separadorTitulosAlternativosField.setEditable(false);
		contentPanel.add(separadorTitulosAlternativosField, "4, 10, fill, default");

		JLabel separadorPosicoesChartRunLabel = SwingComponentFactory.createCommonJLabel("Separador de Posições de Chart-run:");
		contentPanel.add(separadorPosicoesChartRunLabel, "2, 12, right, default");

		separadorPosicoesChartRunField = SwingComponentFactory.createCommonJTextField("Expressão utilizada para separar as posições dos chart-runs",
				10);
		separadorPosicoesChartRunField.setEditable(false);
		contentPanel.add(separadorPosicoesChartRunField, "4, 12, fill, center");

		contentPanel.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentHidden(ComponentEvent event) {
				fireViewFlexivelEvent(false);
			}

			@Override
			public void componentShown(ComponentEvent event) {
				fireViewFlexivelEvent(true);
			}
		});
	}

	@PostConstruct
	protected void init() {
		nomeArtistaField.setText(parsersConfig.nomeArtistaPattern().pattern());
		separadoresArtistasField.setText(parsersConfig.separadoresArtistasPattern().pattern());
		separadorArtistaCancaoField.setText(parsersConfig.separadorArtistaCancaoPattern().pattern());
		tituloCancaoField.setText(parsersConfig.tituloCancaoPattern().pattern());
		separadorTitulosAlternativosField.setText(parsersConfig.titulosAlternativosCancaoSeparadorPattern().pattern());
		separadorPosicoesChartRunField.setText(parsersConfig.separadorPosicoesChartRun());
	}

	@Override
	public Container getAwtContainer() {
		return conteudoPanel;
	}
}
