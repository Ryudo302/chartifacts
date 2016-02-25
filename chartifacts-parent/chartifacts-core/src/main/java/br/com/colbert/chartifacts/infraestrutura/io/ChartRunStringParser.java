package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.*;

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
	 * @param limiteValorPosicao
	 *            o valor máximo que uma posição pode ter
	 * @return a instância de <em>chart-run</em> criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia ou um limite menor ou igual a zero
	 * @throws ChartRunInvalidoException
	 *             caso o texto informado possua posições cujo valor ultrapasse o limite informado
	 */
	public ChartRun parse(String texto, int limiteValorPosicao) {
		Validate.notBlank(texto, "texto");
		Validate.isTrue(limiteValorPosicao > 0, "O limite de valor das posições deve ser superior a zero");

		logger.trace("Analisando: {}", texto);
		logger.trace("Utilizando configurações: {}", parserConfig);

		List<PosicaoChart> elementos = new ArrayList<>();

		String[] chartRunString = texto.split(parserConfig.separadorPosicoesChartRun());
		for (String elementoString : chartRunString) {
			if (StringUtils.isNumeric(elementoString)) {
				int posicao = Integer.parseInt(elementoString);
				if (posicao > limiteValorPosicao) {
					throw new ChartRunInvalidoException(texto, limiteValorPosicao);
				} else {
					elementos.add(PosicaoChart.valueOf(posicao));
				}
			} else {
				elementos.add(PosicaoChart.AUSENCIA);
			}
		}

		logger.trace("Elementos identificados: {}", elementos);
		return new ChartRun(elementos);
	}
}
