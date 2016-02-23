package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas.SemDataFinal;

/**
 * Permite a obtenção de instâncias de {@link IntervaloDeDatas} a partir dos dados presentes em uma {@link String}.
 *
 * @author Thiago Colbert
 * @since 21/02/2016
 */
@ApplicationScoped
public class IntervaloDeDatasStringParser implements Serializable {

	private static final long serialVersionUID = 7828111894518661714L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient StringParsersConfig parserConfig;

	private DateTimeFormatter dateFormatter;

	@PostConstruct
	protected void init() {
		dateFormatter = DateTimeFormatter.ofPattern(parserConfig.formatoDatas());
	}

	/**
	 * Cria uma nova {@link IntervaloDeDatas} a partir da {@link String} informada.
	 *
	 * @param texto
	 *            a ser analizado
	 * @return a instância de intervalo criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia
	 */
	public IntervaloDeDatas parse(String texto) {
		Validate.notBlank(texto);
		logger.trace("Analisando: {}", texto);
		logger.trace("Utilizando configurações: {}", parserConfig);

		LocalDate[] datas = parseDatas(texto);
		logger.trace("Datas identificadas: {}", (Object) datas);

		SemDataFinal intervalo = IntervaloDeDatas.novo().de(datas[0]);
		if (datas[1] != null) {
			return intervalo.ate(datas[1]);
		} else {
			return intervalo.semFim();
		}
	}

	private LocalDate[] parseDatas(String linha) {
		Matcher matcher = parserConfig.periodoIntervaloPattern().matcher(linha);
		if (!matcher.find()) {
			throw new IllegalArgumentException(
					"Padrão para intervalo de datas ('" + parserConfig.periodoIntervaloPattern() + "') não encontrado: " + linha);
		}

		int gruposIdentificados = matcher.groupCount();

		LocalDate dataInicial = LocalDate.parse(matcher.group(1), dateFormatter);
		LocalDate dataFinal = gruposIdentificados > 1
				? StringUtils.isNotBlank(matcher.group(2)) ? LocalDate.parse(matcher.group(2), dateFormatter) : null : null;

		return ArrayUtils.toArray(dataInicial, dataFinal);
	}

	public StringParsersConfig getParserConfig() {
		return parserConfig;
	}

	public DateTimeFormatter getDateFormatter() {
		return dateFormatter;
	}
}
