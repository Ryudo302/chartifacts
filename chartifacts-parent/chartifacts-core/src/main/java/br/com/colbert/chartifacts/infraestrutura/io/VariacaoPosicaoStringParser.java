package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.regex.Matcher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.negocio.chartrun.*;

/**
 * Permite a obtenção de instâncias de {@link VariacaoPosicao} a partir dos dados presentes em uma {@link String}.
 * 
 * @author Thiago Colbert
 * @since 8 de mai de 2016
 */
@ApplicationScoped
public class VariacaoPosicaoStringParser implements Serializable {

	private static final String SIMBOLO_SUBIDA = "+";
	private static final String SIMBOLO_QUEDA = "-";
	private static final String SIMBOLO_PERMANENCIA = "=";
	private static final String SIMBOLO_ESTREIA = " NEW ";
	private static final String SIMBOLO_RETORNO = " RET ";

	private static final long serialVersionUID = 4436169754663142716L;

	@Inject
	private transient Logger logger;
	@Inject
	private transient StringParsersConfig parserConfig;

	/**
	 * 
	 * @param texto
	 * @param posicao
	 * @return a instância de variação de posição criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia
	 */
	public VariacaoPosicao parseVariacaoPosicao(String texto, PosicaoChart posicao) {
		Validate.notBlank(texto, "texto");

		logger.trace("Analisando: {}", texto);
		logger.trace("Utilizando configurações: {}", parserConfig);

		Matcher variacaoPosicaoMatcher = parserConfig.variacaoPosicaoPattern().matcher(texto.replaceAll("\\[/?[a-z]+\\]", StringUtils.EMPTY));
		if (variacaoPosicaoMatcher.matches()) {
			String group = variacaoPosicaoMatcher.group(1);
			String tipoVariacaoPosicaoString = group.replaceAll("\\d", StringUtils.EMPTY);

			logger.trace("Tipo de variação identificado: {}", tipoVariacaoPosicaoString);
			switch (tipoVariacaoPosicaoString) {
			case SIMBOLO_SUBIDA:
				return VariacaoPosicaoBuilder.subida().de(posicao.sum(-Integer.parseInt(String.valueOf(group.charAt(1))))).para(posicao).build();
			case SIMBOLO_QUEDA:
				return VariacaoPosicaoBuilder.queda().de(posicao.sum(Integer.parseInt(String.valueOf(group.charAt(1))))).para(posicao).build();
			case SIMBOLO_PERMANENCIA:
				return VariacaoPosicaoBuilder.permanencia().em(posicao.sum(Integer.parseInt(String.valueOf(group.charAt(1))))).build();
			case SIMBOLO_ESTREIA:
				return VariacaoPosicaoBuilder.estreia().em(posicao).build();
			case SIMBOLO_RETORNO:
				return VariacaoPosicaoBuilder.retorno().em(posicao).build();
			default:
				throw new IllegalArgumentException("Tipo de variação de posição desconhecido: " + tipoVariacaoPosicaoString);
			}
		} else {
			throw new IllegalArgumentException(
					"Padrão para variação de posição ('" + parserConfig.variacaoPosicaoPattern() + "') não encontrado: " + texto);
		}
	}
}
