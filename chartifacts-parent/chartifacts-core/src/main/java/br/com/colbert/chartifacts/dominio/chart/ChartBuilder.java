package br.com.colbert.chartifacts.dominio.chart;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.*;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.historico.CalculadoraPontos;
import br.com.colbert.chartifacts.dominio.musica.Cancao;
import br.com.colbert.chartifacts.infraestrutura.tempo.IntervaloDeDatas;
import br.com.colbert.chartifacts.infraestrutura.util.Holder;

/**
 * {@link Builder} de instâncias de {@link Chart}.
 * 
 * @author ThiagoColbert
 * @since 24 de fev de 2016
 */
@ApplicationScoped
public class ChartBuilder implements Serializable {

	private static final long serialVersionUID = 4894083072736909580L;

	/**
	 * Classe que representa um {@link Chart} que ainda não possui todas as informações definidas.
	 * 
	 * @author ThiagoColbert
	 * @since 1 de mai de 2016
	 */
	public class ChartIncompleto implements Builder<Chart> {

		private final Chart chart;

		ChartIncompleto(int numero) {
			chart = new Chart(numero);
		}

		/**
		 * Define o período da parada musical.
		 * 
		 * @param periodo
		 * @return <code>this</code>, para chamadas encadeadas de método
		 * @throws NullPointerException
		 *             caso seja informado <code>null</code>
		 */
		public ChartIncompleto comPeriodo(IntervaloDeDatas periodo) {
			chart.setPeriodo(Objects.requireNonNull(periodo, "periodo"));
			logger.debug("Período: {}", periodo);
			return this;
		}

		/**
		 * Define qual parada musical vem logo antes desta.
		 * 
		 * @param anterior
		 *            (pode ser <code>null</code>)
		 * @return <code>this</code>, para chamadas encadeadas de método
		 */
		public ChartIncompleto anterior(Chart anterior) {
			chart.setAnterior(anterior);
			logger.debug("Anterior: {}", anterior);
			return this;
		}

		/**
		 * Define qual parada musical vem logo após esta.
		 * 
		 * @param proximo
		 *            (pode ser <code>null</code>)
		 * @return <code>this</code>, para chamadas encadeadas de método
		 */
		public ChartIncompleto proximo(Chart proximo) {
			chart.setProximo(proximo);
			logger.debug("Próximo: {}", proximo);
			return this;
		}

		/**
		 * Define a lista de canções da parada musical.
		 * 
		 * @param cancoes
		 * @param calculadoraPontos
		 *            calculadora utilizada para obter a pontuação das canções
		 * @return <code>this</code>, para chamadas encadeadas de método
		 * @throws NullPointerException
		 *             caso seja informado <code>null</code>
		 */
		public ChartIncompleto comCancoes(List<Cancao> cancoes, CalculadoraPontos calculadoraPontos) {
			Objects.requireNonNull(cancoes, "cancoes");
			Objects.requireNonNull(calculadoraPontos, "calculadoraPontos");

			final Holder<Integer> numeroPosicao = new Holder<>();
			numeroPosicao.setValue(0);

			chart.setCancoes(cancoes.stream()
					.map(cancao -> CancaoChartBuilder
							.novo(PosicaoChart.valueOf(numeroPosicao.setValue(numeroPosicao.getValue() + 1).getValue()), cancao)
							.atualizarEstatisticasUtilizando(calculadoraPontos).build())
					.collect(Collectors.toList()));

			logger.debug("Canções: {}", chart.getCancoes());

			return this;
		}

		/**
		 * Define a lista de canções da parada musical.
		 * 
		 * @param cancoes
		 *            as canções, já com informações de posição, estatísticas, etc
		 * @return <code>this</code>, para chamadas encadeadas de método
		 * @throws NullPointerException
		 *             caso seja informado <code>null</code>
		 */
		public ChartIncompleto comCancoes(List<CancaoChart> cancoes) {
			Objects.requireNonNull(cancoes, "cancoes");
			chart.setCancoes(cancoes);

			logger.debug("Canções: {}", cancoes);

			return this;
		}

		/**
		 * @throws IllegalStateException
		 *             caso os valores obrigatórios para criação de {@link Chart} ainda não tenham sido definidos.
		 */
		@Override
		public Chart build() {
			Validate.validState(chart.getPeriodo() != null, "Período não informado");
			Validate.validState(chart.getCancoes() != null, "Canções não informadas");
			return chart;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}

	@Inject
	private transient Logger logger;

	/**
	 * Inicia a criação de um {@link Chart} informando o seu número.
	 * 
	 * @param numero
	 *            número da parada a ser criada
	 * @return <code>this</code>, para chamadas encadeadas de método
	 * @throws IllegalArgumentException
	 *             caso seja informado um número inválido (menor ou igual a zero)
	 */
	public ChartIncompleto numero(int numero) {
		Validate.isTrue(numero > 0, "Número inválido - deve ser maior que zero");
		logger.debug("Número: {}", numero);
		return new ChartIncompleto(numero);
	}
}
