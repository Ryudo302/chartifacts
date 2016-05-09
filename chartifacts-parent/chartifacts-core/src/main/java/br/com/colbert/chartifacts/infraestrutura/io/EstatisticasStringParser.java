package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;
import java.util.regex.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.dominio.historico.Estatisticas;
import br.com.colbert.chartifacts.infraestrutura.properties.Property;
import br.com.colbert.chartifacts.negocio.chartrun.PermanenciaPosicao;

/**
 * Permite a obtenção de instâncias de {@link Estatisticas} a partir dos dados presentes em uma {@link String}.
 *
 * @author Thiago Colbert
 * @since 09/05/2016
 */
@ApplicationScoped
public class EstatisticasStringParser implements Serializable {

	private static final long serialVersionUID = 5891814408875228742L;

	@Inject
	private transient Logger logger;

	@Inject
	@Property(ParserProperties.ESTATISTICAS_PERMANENCIA_AND_PEAK_KEY)
	private transient Pattern permanenciaAndPeakPattern;

	/**
	 * Cria uma nova {@link Estatisticas} a partir da {@link String} informada.
	 * 
	 * @param texto
	 * @return a instância de variação de posição criada
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso seja informada uma String vazia ou não seja possível identificar estatísticas na String
	 */
	public Estatisticas parse(String texto) {
		Validate.notBlank(texto, "texto");

		logger.trace("Analisando: {}", texto);

		Matcher matcher = permanenciaAndPeakPattern.matcher(texto);
		if (matcher.matches()) {
			return new Estatisticas(0.0, Integer.valueOf(matcher.group(1)), new PermanenciaPosicao(PosicaoChart.valueOf(matcher.group(2)),
					Integer.valueOf(StringUtils.defaultIfBlank(matcher.group(4), String.valueOf(1)))));
		} else {
			throw new IllegalArgumentException("Não foi possível identificar estatísticas: " + texto);
		}
	}
}
