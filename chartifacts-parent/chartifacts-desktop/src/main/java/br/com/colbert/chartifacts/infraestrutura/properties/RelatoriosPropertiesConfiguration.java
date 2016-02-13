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
import br.com.colbert.chartifacts.dominio.relatorios.RelatoriosConfiguration;
import br.com.colbert.chartifacts.dominio.relatorios.generator.RelatorioGenerator;

/**
 * Implementação de {@link RelatoriosConfiguration} que utiliza um arquivo <em>properties</em>.
 *
 * @author Thiago Colbert
 * @since 20/03/2015
 */
public class RelatoriosPropertiesConfiguration implements RelatoriosConfiguration {

	private static final String NOME_ARQUIVO = "relatorios.properties";

	@Inject
	private transient Logger logger;

	private Properties properties;

	@PostConstruct
	protected void init() throws IOException {
		properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(NOME_ARQUIVO));
		logger.debug("Propriedades carregadas");
	}

	@Override
	public Optional<Integer> limiteTamanho() {
		return Optional.ofNullable(Integer.valueOf(getString("relatorios.limiteTamanho")));
	}

	@Override
	public int larguraPrimeiraColuna() {
		return Integer.valueOf(getString("relatorios.larguraPrimeiraColuna"));
	}

	@Override
	public String separador() {
		return getString("relatorios.separador");
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
}
