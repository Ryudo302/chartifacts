package br.com.colbert.chartifacts.ui;

import java.awt.FlowLayout;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.infraestrutura.io.StringParsersConfig;

/**
 * Painel contendo informações de um {@link StringParsersConfig}.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
@Singleton
public class PadroesArquivoEntradaPanel extends JPanel {

	private static final long serialVersionUID = -154918168549652861L;

	private final JTextField nomeArtistaField;
	private final JTextField separadoresArtistasField;
	private final JTextField separadorArtistaCancaoField;
	private final JTextField tituloCancaoField;
	private final JTextField separadorTitulosAlternativosField;
	private final JTextField separadorPosicoesChartRunField;

	@Inject
	private StringParsersConfig parsersConfig;

	/**
	 * Create the panel.
	 */
	public PadroesArquivoEntradaPanel() {
		setBorder(new TitledBorder(null, "Padr\u00F5es do Arquivo", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel togglePanel = new JPanel();
		JPanel contentPanel = new JPanel();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		FlowLayout flowLayout = (FlowLayout) togglePanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.add(togglePanel);

		JToggleButton togglePadroesArquivoButton = new JToggleButton("Ocultar");
		togglePadroesArquivoButton.addActionListener(event -> {
			togglePadroesArquivoButton.setText(togglePadroesArquivoButton.isSelected() ? "Ocultar" : "Mostrar");
			contentPanel.setVisible(togglePadroesArquivoButton.isSelected());
		});
		togglePadroesArquivoButton.setSelected(true);
		togglePanel.add(togglePadroesArquivoButton);

		contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		this.add(contentPanel);

		JLabel nomeArtistaLabel = new JLabel("Nome de Artista:");
		contentPanel.add(nomeArtistaLabel, "2, 2, right, default");

		nomeArtistaField = new JTextField();
		nomeArtistaField.setEditable(false);
		nomeArtistaField.setToolTipText("Regex utilizada para identificar os nomes dos artistas");
		contentPanel.add(nomeArtistaField, "4, 2, fill, default");
		nomeArtistaField.setColumns(10);

		JLabel separadoresArtistasLabel = new JLabel("Separadores de Artistas:");
		contentPanel.add(separadoresArtistasLabel, "2, 4, right, default");

		separadoresArtistasField = new JTextField();
		separadoresArtistasField.setEditable(false);
		separadoresArtistasField.setToolTipText("Regex utilizada para identificar segmentos de texto que separem um artista do outro");
		separadoresArtistasField.setColumns(10);
		contentPanel.add(separadoresArtistasField, "4, 4, fill, default");

		JLabel separadorArtistaCancaoLabel = new JLabel("Separador Artista/Canção:");
		contentPanel.add(separadorArtistaCancaoLabel, "2, 6, right, default");

		separadorArtistaCancaoField = new JTextField();
		separadorArtistaCancaoField.setEditable(false);
		separadorArtistaCancaoField
				.setToolTipText("Regex utilizada para identificar segmentos de texto que separem os nomes de artistas do título da canção");
		separadorArtistaCancaoField.setColumns(10);
		contentPanel.add(separadorArtistaCancaoField, "4, 6, fill, default");

		JLabel tituloCancaoLabel = new JLabel("Título de Canção:");
		contentPanel.add(tituloCancaoLabel, "2, 8, right, default");

		tituloCancaoField = new JTextField();
		tituloCancaoField.setEditable(false);
		tituloCancaoField.setToolTipText("Regex utilizada para identificar o título da canção");
		tituloCancaoField.setColumns(10);
		contentPanel.add(tituloCancaoField, "4, 8, fill, default");

		JLabel separadorTitulosAlternativosLabel = new JLabel("Separador de Títulos Alternativos:");
		contentPanel.add(separadorTitulosAlternativosLabel, "2, 10, right, default");

		separadorTitulosAlternativosField = new JTextField();
		separadorTitulosAlternativosField.setEditable(false);
		separadorTitulosAlternativosField.setToolTipText("Regex utilizada para identificar títulos alternativos da canção");
		separadorTitulosAlternativosField.setColumns(10);
		contentPanel.add(separadorTitulosAlternativosField, "4, 10, fill, default");

		JLabel separadorPosicoesChartRunLabel = new JLabel("Separador de Posições de Chart-run:");
		contentPanel.add(separadorPosicoesChartRunLabel, "2, 12, right, default");

		separadorPosicoesChartRunField = new JTextField();
		separadorPosicoesChartRunField.setEditable(false);
		separadorPosicoesChartRunField.setToolTipText("Expressão utilizada para separar as posições dos chart-runs");
		separadorPosicoesChartRunField.setColumns(10);
		contentPanel.add(separadorPosicoesChartRunField, "4, 12, fill, default");
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
}
