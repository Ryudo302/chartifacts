package br.com.colbert.chartifacts.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.layout.*;

import br.com.colbert.chartifacts.infraestrutura.io.ParserProperties;
import br.com.colbert.chartifacts.infraestrutura.mvp.AbstractViewFlexivel;
import br.com.colbert.chartifacts.infraestrutura.properties.Property;
import br.com.colbert.chartifacts.infraestrutura.swing.*;

/**
 * Painel contendo informações de um {@link StringParsersConfig}.
 *
 * @author Thiago Colbert
 * @since 20/04/2015
 */
@Singleton
public class PadroesArquivoEntradaViewPart extends AbstractViewFlexivel {

	private static final long serialVersionUID = -154918168549652861L;

	private static class RegexDataFormatter extends AbstractFormatter {

		private static final long serialVersionUID = 1417087608365137567L;

		@Override
		public Object stringToValue(String text) throws ParseException {
			try {
				// testa se é um padrão de datas válido
				new SimpleDateFormat(text);
				return text;
			} catch (IllegalArgumentException exception) {
				throw new ParseException(text, 0);
			}
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			return value != null ? value.toString() : StringUtils.EMPTY;
		}
	}

	private final JPanel conteudoPanel;

	private final JFormattedTextField nomeArtistaField;
	private final JFormattedTextField separadoresArtistasField;
	private final JFormattedTextField separadorArtistaCancaoField;
	private final JFormattedTextField tituloCancaoField;
	private final JFormattedTextField separadorTitulosAlternativosField;
	private final JTextField separadorPosicoesChartRunField;
	private final JFormattedTextField intervaloDatasField;
	private final JFormattedTextField padraoDatasField;

	@Inject
	private transient PatternFormatter patternFormatter;

	@Inject
	@Property(ParserProperties.NOME_ARTISTA_KEY)
	private transient Pattern nomeArtistaPattern;
	@Inject
	@Property(ParserProperties.SEPARADORES_ARTISTAS_KEY)
	private transient Pattern separadoresArtistasPattern;
	@Inject
	@Property(ParserProperties.SEPARADOR_ARTISTAS_E_CANCAO_KEY)
	private transient Pattern separadorArtistaCancaoPattern;
	@Inject
	@Property(ParserProperties.TITULO_CANCAO_KEY)
	private transient Pattern tituloCancaoPattern;
	@Inject
	@Property(ParserProperties.SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY)
	private transient Pattern titulosAlternativosCancaoSeparadorPattern;
	@Inject
	@Property(ParserProperties.SEPARADOR_POSICOES_CHARTRUN_KEY)
	private transient String separadorPosicoesChartRun;
	@Inject
	@Property(ParserProperties.PERIODO_INTERVALO_KEY)
	private transient Pattern periodoIntervaloPattern;
	@Inject
	@Property(ParserProperties.FORMATO_DATAS_KEY)
	private transient String formatoDatas;

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
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		conteudoPanel.add(contentPanel);

		JLabel nomeArtistaLabel = SwingComponentFactory.createCommonJLabel("Nome de Artista:");
		contentPanel.add(nomeArtistaLabel, "2, 2, right, center");

		nomeArtistaField = SwingComponentFactory.createCommonJFormattedTextField("Regex utilizada para identificar os nomes dos artistas", 10);
		contentPanel.add(nomeArtistaField, "4, 2, fill, default");

		JLabel separadoresArtistasLabel = SwingComponentFactory.createCommonJLabel("Separadores de Artistas:");
		contentPanel.add(separadoresArtistasLabel, "2, 4, right, default");

		separadoresArtistasField = SwingComponentFactory
				.createCommonJFormattedTextField("Regex utilizada para identificar segmentos de texto que separem um artista do outro", 10);
		contentPanel.add(separadoresArtistasField, "4, 4, fill, default");

		JLabel separadorArtistaCancaoLabel = SwingComponentFactory.createCommonJLabel("Separador Artista/Canção:");
		contentPanel.add(separadorArtistaCancaoLabel, "2, 6, right, default");

		separadorArtistaCancaoField = SwingComponentFactory.createCommonJFormattedTextField(
				"Regex utilizada para identificar segmentos de texto que separem os nomes de artistas do título da canção", 10);
		contentPanel.add(separadorArtistaCancaoField, "4, 6, fill, default");

		JLabel tituloCancaoLabel = SwingComponentFactory.createCommonJLabel("Título de Canção:");
		contentPanel.add(tituloCancaoLabel, "2, 8, right, default");

		tituloCancaoField = SwingComponentFactory.createCommonJFormattedTextField("Regex utilizada para identificar o título da canção", 10);
		contentPanel.add(tituloCancaoField, "4, 8, fill, default");

		JLabel separadorTitulosAlternativosLabel = SwingComponentFactory.createCommonJLabel("Separador de Títulos Alternativos:");
		contentPanel.add(separadorTitulosAlternativosLabel, "2, 10, right, default");

		separadorTitulosAlternativosField = SwingComponentFactory
				.createCommonJFormattedTextField("Regex utilizada para identificar títulos alternativos da canção", 10);
		contentPanel.add(separadorTitulosAlternativosField, "4, 10, fill, default");

		JLabel separadorPosicoesChartRunLabel = SwingComponentFactory.createCommonJLabel("Separador de Posições de Chart-run:");
		contentPanel.add(separadorPosicoesChartRunLabel, "2, 12, right, default");

		separadorPosicoesChartRunField = SwingComponentFactory.createCommonJTextField("Expressão utilizada para separar as posições dos chart-runs",
				10);
		contentPanel.add(separadorPosicoesChartRunField, "4, 12, fill, center");

		JLabel intervaloDatasLabel = SwingComponentFactory.createCommonJLabel("Intervalo de Datas:");
		contentPanel.add(intervaloDatasLabel, "2, 14, right, default");

		intervaloDatasField = SwingComponentFactory.createCommonJFormattedTextField("Regex utilizada para identificar um intervalo de datas", 10);
		contentPanel.add(intervaloDatasField, "4, 14, fill, center");

		JLabel padraoDatasLabel = SwingComponentFactory.createCommonJLabel("Padrão de Datas:");
		contentPanel.add(padraoDatasLabel, "2, 16, right, default");

		padraoDatasField = SwingComponentFactory.createCommonJFormattedTextField("Formato utilizado para representar datas", 10);
		padraoDatasField.setFormatterFactory(new DefaultFormatterFactory(new RegexDataFormatter()));

		contentPanel.add(padraoDatasField, "4, 16, fill, center");

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
		modelToView();

		DefaultFormatterFactory patternFormatterFactory = new DefaultFormatterFactory(patternFormatter);
		nomeArtistaField.setFormatterFactory(patternFormatterFactory);
		separadoresArtistasField.setFormatterFactory(patternFormatterFactory);
		separadorArtistaCancaoField.setFormatterFactory(patternFormatterFactory);
		tituloCancaoField.setFormatterFactory(patternFormatterFactory);
		separadorTitulosAlternativosField.setFormatterFactory(patternFormatterFactory);
		intervaloDatasField.setFormatterFactory(patternFormatterFactory);
	}

	private void modelToView() {
		nomeArtistaField.setValue(nomeArtistaPattern);
		separadoresArtistasField.setValue(separadoresArtistasPattern);
		separadorArtistaCancaoField.setValue(separadorArtistaCancaoPattern);
		tituloCancaoField.setValue(tituloCancaoPattern);
		separadorTitulosAlternativosField.setValue(titulosAlternativosCancaoSeparadorPattern);
		separadorPosicoesChartRunField.setText(separadorPosicoesChartRun);
		intervaloDatasField.setValue(periodoIntervaloPattern);
		padraoDatasField.setValue(formatoDatas);
	}

	/*private void viewToModel() {
		parsersConfig.setNomeArtistaPattern((Pattern) nomeArtistaField.getValue());
		parsersConfig.setSeparadoresArtistasPattern((Pattern) separadoresArtistasField.getValue());
		parsersConfig.setSeparadorArtistaCancaoPattern((Pattern) separadorArtistaCancaoField.getValue());
		parsersConfig.setTituloCancaoPattern((Pattern) tituloCancaoField.getValue());
		parsersConfig.setTitulosAlternativosCancaoSeparadorPattern((Pattern) separadorTitulosAlternativosField.getValue());
		parsersConfig.setSeparadorPosicoesChartRun(separadorPosicoesChartRunField.getText());
		parsersConfig.setPeriodoIntervaloPattern((Pattern) intervaloDatasField.getValue());
		parsersConfig.setFormatoDatas((String) padraoDatasField.getValue());
	}*/

	@Override
	public Container getAwtContainer() {
		return conteudoPanel;
	}
}
