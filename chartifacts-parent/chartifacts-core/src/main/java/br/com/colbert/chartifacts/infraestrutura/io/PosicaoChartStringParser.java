package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.regex.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.infraestrutura.properties.Property;

/**
 * Permite a obtenção de instâncias de {@link PosicaoChart} a partir dos dados presentes em uma {@link String}.
 *
 * @author Thiago Colbert
 * @since 09/05/2016
 */
@ApplicationScoped
public class PosicaoChartStringParser implements Serializable {

	private static final long serialVersionUID = 2359280632565272636L;

	@Inject
	private transient Logger logger;

	@Inject
	@Property(ParserProperties.NUMERO_POSICAO_KEY)
	private transient Pattern posicaoPattern;

	/**
	 * Cria uma nova {@link PosicaoChart} a partir da {@link String} informada.
	 * 
	 * @param texto
	 * @return a instância de variação de posição criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia
	 */
	public PosicaoChart parse(String texto) {
		Validate.notBlank(texto, "texto");

		logger.trace("Analisando: {}", texto);
		Matcher matcher = posicaoPattern.matcher(texto);
		if (matcher.matches()) {
			return PosicaoChart.valueOf(matcher.group(1));
		} else {
			throw new IllegalArgumentException("Padrão para número de posição ('" + posicaoPattern + "') não encontrado: " + texto);
		}
	}
}
