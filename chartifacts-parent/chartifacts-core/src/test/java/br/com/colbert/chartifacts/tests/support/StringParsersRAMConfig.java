package br.com.colbert.chartifacts.tests.support;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import br.com.colbert.chartifacts.infraestrutura.io.StringParsersConfig;

/**
 * Implementação de {@link StringParsersConfig} que obtém as informações a partir de constantes definidas em memória.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class StringParsersRAMConfig implements StringParsersConfig {

	private static final Pattern NOME_ARTISTA_PATTERN = Pattern.compile("(.+) - \"");
	private static final Pattern SEPARADORES_ARTISTAS_PATTERN = Pattern
			.compile("feat\\.|&|,|\\[feat\\.|presents|\\(|Duet With|intro\\.|vs\\.| or |part\\.");
	private static final Pattern SEPARADOR_ARTISTAS_E_CANCAO_PATTERN = Pattern.compile(" - ");

	private static final Pattern TITULO_CANCAO_PATTERN = Pattern.compile("\"(.+)\"");
	private static final Pattern SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_PATTERN = Pattern.compile("\" / \"");

	private static final String SEPARADOR_POSICOES_CHARTRUN = StringUtils.SPACE;

	@Override
	public Pattern nomeArtistaPattern() {
		return NOME_ARTISTA_PATTERN;
	}

	@Override
	public Pattern separadoresArtistasPattern() {
		return SEPARADORES_ARTISTAS_PATTERN;
	}

	@Override
	public Pattern separadorArtistaCancaoPattern() {
		return SEPARADOR_ARTISTAS_E_CANCAO_PATTERN;
	}

	@Override
	public Pattern tituloCancaoPattern() {
		return TITULO_CANCAO_PATTERN;
	}

	@Override
	public Pattern titulosAlternativosCancaoSeparadorPattern() {
		return SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_PATTERN;
	}

	@Override
	public String separadorPosicoesChartRun() {
		return SEPARADOR_POSICOES_CHARTRUN;
	}
}
