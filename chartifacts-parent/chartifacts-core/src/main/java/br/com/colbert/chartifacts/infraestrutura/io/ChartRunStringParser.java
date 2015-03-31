package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chartrun.*;

/**
 * Permite a obtenção de instâncias de {@link ChartRun} a partir dos dados presentes em uma {@link String}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@ApplicationScoped
public class ChartRunStringParser implements Serializable {

	private static final long serialVersionUID = 3856917756887213009L;

	@Inject
	private Logger logger;
	@Inject
	private StringParsersConfig parserConfig;

	/**
	 * Cria um novo {@link ChartRun} a partir da {@link String} informada.
	 *
	 * @param texto
	 *            a ser analizado
	 * @return a instância de <em>chart-run</em> criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia
	 */
	public ChartRun parse(String texto) {
		Validate.notBlank(texto);
		logger.trace("Analisando: {}", texto);
		logger.trace("Utilizando configurações: {}", parserConfig);

		List<ElementoChartRun> elementos = new ArrayList<>();

		Stream<String> chartRunString = Stream.of(texto.split(parserConfig.separadorPosicoesChartRun()));
		chartRunString.forEach(elementoString -> {
			if (StringUtils.isNumeric(elementoString)) {
				elementos.add(ElementoChartRun.valueOf(Integer.parseInt(elementoString)));
			} else {
				elementos.add(ElementoChartRun.AUSENCIA);
			}
		});

		logger.trace("Elementos identificados: {}", elementos);
		return new ChartRun(elementos);
	}
}
