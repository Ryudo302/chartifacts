package br.com.colbert.chartifacts.infraestrutura.properties;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;
import br.com.colbert.chartifacts.negocio.relatorios.RelatoriosConfiguration;
import br.com.colbert.chartifacts.negocio.relatorios.generator.RelatorioGenerator;

/**
 * Implementação de {@link RelatoriosConfiguration} que utiliza um arquivo <em>properties</em>.
 *
 * @author Thiago Colbert
 * @since 20/03/2015
 */
public class RelatoriosPropertiesConfiguration implements RelatoriosConfiguration {

	private static final String RELATORIOS_LIMITE_TAMANHO_KEY = "relatorios.limiteTamanho";
	private static final String RELATORIOS_LARGURA_PRIMEIRA_COLUNA_KEY = "relatorios.larguraPrimeiraColuna";
	private static final String RELATORIOS_SEPARADOR_KEY = "relatorios.separador";

	private static final String NOME_ARQUIVO = "relatorios.properties";

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
	public Optional<Integer> limiteTamanho() {
		return Optional.ofNullable(Integer.valueOf(getString(RELATORIOS_LIMITE_TAMANHO_KEY)));
	}

	public void setLimiteTamanho(int tamanho) {
		setInteger(RELATORIOS_LIMITE_TAMANHO_KEY, tamanho);
	}

	@Override
	public int larguraPrimeiraColuna() {
		return Integer.valueOf(getString(RELATORIOS_LARGURA_PRIMEIRA_COLUNA_KEY));
	}

	public void setLarguraPrimeiraColuna(int largura) {
		setInteger(RELATORIOS_LARGURA_PRIMEIRA_COLUNA_KEY, largura);
	}

	@Override
	public String separador() {
		return getString(RELATORIOS_SEPARADOR_KEY);
	}

	public void setSeparador(String separador) {
		properties.setProperty(RELATORIOS_SEPARADOR_KEY, separador);
	}

	@Override
	public <T extends RelatorioGenerator<?, ?>> Collection<ElementoChartRun> posicoesRelatorio(T relatorioGenerator) {
		List<ElementoChartRun> tops = new ArrayList<>();
		String[] topsString = getString(getClassName(relatorioGenerator) + ".posicoes").split(",");
		Stream.of(topsString).forEach(topString -> tops.add(ElementoChartRun.valueOf(Integer.valueOf(topString))));
		return tops;
	}

	private <T extends RelatorioGenerator<?, ?>> String getClassName(T relatorioGenerator) {
		String simpleName = relatorioGenerator.getClass().getSimpleName();
		return simpleName.contains("$") ? simpleName.substring(0, simpleName.indexOf('$')) : simpleName;
	}

	@Override
	public <T extends RelatorioGenerator<?, ?>> String getTituloRelatorio(T relatorioGenerator, Object... args) {
		return MessageFormat.format(getString(getClassName(relatorioGenerator) + ".titulo"), args);
	}

	private String getString(String key) {
		logger.trace("Obtendo propriedade: {}", key);
		return StringUtils.defaultIfBlank(properties.getProperty(key), '!' + key + '!');
	}

	private void setInteger(String key, int value) {
		properties.setProperty(key, String.valueOf(value));
	}
}
