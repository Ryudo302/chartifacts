package br.com.colbert.chartifacts.infraestrutura.properties;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.infraestrutura.io.StringParsersConfig;

/**
 * Implementação de {@link StringParsersConfig} que obtém as informações de um arquivo <em>properties</em>.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class StringParsersPropertiesConfig implements StringParsersConfig {

	private static final String NOME_ARQUIVO = "parser.properties";

	private static final String NOME_ARTISTA_KEY = "artista.nome";
	private static final String SEPARADORES_ARTISTAS_KEY = "artista.separadores";
	private static final String SEPARADOR_ARTISTAS_E_CANCAO_KEY = "artista.cancao.separador";
	private static final String TITULO_CANCAO_KEY = "cancao.titulo";
	private static final String SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY = "cancao.titulosAlternativos.separador";
	private static final String SEPARADOR_POSICOES_CHARTRUN_KEY = "chartrun.posicoes.separador";
	private static final String PERIODO_INTERVALO_KEY = "periodo.intervalo";
	private static final String FORMATO_DATAS_KEY = "datas.formato";


	@Inject
	private transient Logger logger;

	private Properties properties;

	/**
	 * Carrega todas as propriedades a partir do arquivo.
	 * 
	 * @throws IOException
	 *             caso ocorra algum erro de I/O
	 */
	@PostConstruct
	protected void init() throws IOException {
		properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(NOME_ARQUIVO));
		logger.debug("Propriedades carregadas");
	}

	@Override
	public Pattern nomeArtistaPattern() {
		return Pattern.compile(getString(NOME_ARTISTA_KEY));
	}

	public void setNomeArtistaPattern(Pattern pattern) {
		setPatternProperty(NOME_ARTISTA_KEY, pattern);
	}

	@Override
	public Pattern separadoresArtistasPattern() {
		return Pattern.compile(getString(SEPARADORES_ARTISTAS_KEY));
	}

	public void setSeparadoresArtistasPattern(Pattern pattern) {
		setPatternProperty(SEPARADORES_ARTISTAS_KEY, pattern);
	}

	@Override
	public Pattern separadorArtistaCancaoPattern() {
		return Pattern.compile(getString(SEPARADOR_ARTISTAS_E_CANCAO_KEY));
	}

	public void setSeparadorArtistaCancaoPattern(Pattern pattern) {
		setPatternProperty(SEPARADOR_ARTISTAS_E_CANCAO_KEY, pattern);
	}

	@Override
	public Pattern tituloCancaoPattern() {
		return Pattern.compile(getString(TITULO_CANCAO_KEY));
	}

	public void setTituloCancaoPattern(Pattern pattern) {
		setPatternProperty(TITULO_CANCAO_KEY, pattern);
	}

	@Override
	public Pattern titulosAlternativosCancaoSeparadorPattern() {
		return Pattern.compile(getString(SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY));
	}

	public void setTitulosAlternativosCancaoSeparadorPattern(Pattern pattern) {
		setPatternProperty(SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY, pattern);
	}

	@Override
	public String separadorPosicoesChartRun() {
		return getString(SEPARADOR_POSICOES_CHARTRUN_KEY);
	}

	public void setSeparadorPosicoesChartRun(String separador) {
		properties.setProperty(SEPARADOR_POSICOES_CHARTRUN_KEY, separador);
	}
	
	@Override
	public Pattern periodoIntervaloPattern() {
		return Pattern.compile(getString(PERIODO_INTERVALO_KEY));
	}
	
	public void setPeriodoIntervaloPattern(Pattern pattern) {
		setPatternProperty(PERIODO_INTERVALO_KEY, pattern);
	}

	@Override
	public String formatoDatas() {
		return properties.getProperty(FORMATO_DATAS_KEY);
	}

	public void setFormatoDatas(String formatoDatas) {
		properties.setProperty(FORMATO_DATAS_KEY, formatoDatas);
	}

	private void setPatternProperty(String key, Pattern pattern) {
		properties.setProperty(key, pattern != null ? pattern.pattern() : null);
	}

	private String getString(String key) {
		logger.trace("Obtendo propriedade: {}", key);
		return StringUtils.defaultIfBlank(properties.getProperty(key), '!' + key + '!');
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("properties", properties).toString();
	}
}
