package br.com.colbert.chartifacts.infraestrutura.properties;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.*;

import br.com.colbert.chartifacts.infraestrutura.io.StringParsersConfig;

/**
 * Implementação de {@link StringParsersConfig} que obtém as informações de um arquivo <em>properties</em>.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class StringParsersPropertiesConfig implements StringParsersConfig {

	private static final String NOME_ARQUIVO_PROPERTIES = "parser";

	private static final String NOME_ARTISTA_KEY = "artista.nome";
	private static final String SEPARADORES_ARTISTAS_KEY = "artista.separadores";
	private static final String SEPARADOR_ARTISTAS_E_CANCAO_KEY = "artista.cancao.separador";
	private static final String TITULO_CANCAO_KEY = "cancao.titulo";
	private static final String SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY = "cancao.titulosAlternativos.separador";
	private static final String SEPARADOR_POSICOES_CHARTRUN_KEY = "chartrun.posicoes.separador";

	private final ResourceBundle bundle = ResourceBundle.getBundle(NOME_ARQUIVO_PROPERTIES);

	@Override
	public Pattern nomeArtistaPattern() {
		return Pattern.compile(bundle.getString(NOME_ARTISTA_KEY));
	}

	@Override
	public Pattern separadoresArtistasPattern() {
		return Pattern.compile(bundle.getString(SEPARADORES_ARTISTAS_KEY));
	}

	@Override
	public Pattern separadorArtistaCancaoPattern() {
		return Pattern.compile(bundle.getString(SEPARADOR_ARTISTAS_E_CANCAO_KEY));
	}

	@Override
	public Pattern tituloCancaoPattern() {
		return Pattern.compile(bundle.getString(TITULO_CANCAO_KEY));
	}

	@Override
	public Pattern titulosAlternativosCancaoSeparadorPattern() {
		return Pattern.compile(bundle.getString(SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY));
	}

	@Override
	public String separadorPosicoesChartRun() {
		return bundle.getString(SEPARADOR_POSICOES_CHARTRUN_KEY);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bundle", toString(bundle)).toString();
	}

	private String toString(ResourceBundle bundle) {
		StringBuilder builder = new StringBuilder();
		bundle.keySet().forEach(key -> builder.append(key).append('=').append(bundle.getString(key)).append(", "));
		return builder.delete(builder.length() - 2, builder.length()).toString();
	}
}
