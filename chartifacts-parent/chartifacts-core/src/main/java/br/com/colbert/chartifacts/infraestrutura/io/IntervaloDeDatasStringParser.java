package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas.SemDataFinal;
import br.com.colbert.chartifacts.infraestrutura.util.Holder;

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
	 * Cria uma nova {@link IntervaloDeDatas} buscando um padrão de datas nas {@link String}s informadas.
	 *
	 * @param lines
	 *            a serem analisadas
	 * @return a instância de intervalo criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public IntervaloDeDatas parse(List<String> lines) {
		logger.trace("Analisando: {}", lines);
		logger.trace("Utilizando configurações: {}", parserConfig);

		LocalDate[] datas = parseDatas(Objects.requireNonNull(lines, "lines"));
		logger.trace("Datas identificadas: {}", (Object) datas);

		SemDataFinal intervalo = IntervaloDeDatas.novo().de(datas[0]);
		if (datas[1] != null) {
			return intervalo.ate(datas[1]);
		} else {
			return intervalo.semFim();
		}
	}

	/**
	 * Cria uma nova {@link IntervaloDeDatas} buscando um padrão de datas nas {@link String}s informadas.
	 *
	 * @param lines
	 *            a serem analisadas
	 * @return a instância de intervalo criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @see #parse(List)
	 */
	public IntervaloDeDatas parse(String... lines) {
		return parse(Arrays.asList(Objects.requireNonNull(lines, "lines")));
	}

	private LocalDate[] parseDatas(List<String> lines) {
		Holder<LocalDate[]> datasHolder = new Holder<>();

		Matcher matcher = lines.stream().map(line -> parserConfig.periodoIntervaloPattern().matcher(line)).filter(Matcher::find).findFirst().get();
		int gruposIdentificados = matcher.groupCount();

		LocalDate dataInicial = LocalDate.parse(matcher.group(1), dateFormatter);
		LocalDate dataFinal = gruposIdentificados > 1
				? StringUtils.isNotBlank(matcher.group(2)) ? LocalDate.parse(matcher.group(2), dateFormatter) : null : null;

		datasHolder.setValue(ArrayUtils.toArray(dataInicial, dataFinal));

		LocalDate[] datas = datasHolder.getValue();
		if (datas == null) {
			throw new IllegalArgumentException(
					"Padrão para intervalo de datas ('" + parserConfig.periodoIntervaloPattern() + "') não encontrado: " + lines);
		} else {
			return datas;
		}
	}

	public StringParsersConfig getParserConfig() {
		return parserConfig;
	}

	public DateTimeFormatter getDateFormatter() {
		return dateFormatter;
	}
}
