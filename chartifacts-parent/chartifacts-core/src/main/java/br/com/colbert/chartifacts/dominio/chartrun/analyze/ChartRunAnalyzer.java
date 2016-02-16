package br.com.colbert.chartifacts.dominio.chartrun.analyze;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.Inject;

import org.jboss.weld.exceptions.IllegalArgumentException;
import org.slf4j.Logger;

import com.google.common.collect.Iterators;

import br.com.colbert.chartifacts.dominio.chartrun.*;

/**
 * Classe que permite a análise de {@link ChartRun}s para extrair informações a respeito deles.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 */
@ApplicationScoped
public class ChartRunAnalyzer implements Serializable {

	private static final long serialVersionUID = 5383863001731237061L;

	@Inject
	private Logger logger;

	@Inject
	@Any
	private Instance<MaiorVariacaoPosicaoEspecificaChartRunStrategy> estrategiasPosicaoEspecifica;
	@Inject
	@Any
	private Instance<MaiorVariacaoChartRunStrategy> estrategias;

	/**
	 * Obtém a maior ocorrência de um determinado tipo de variação dentro de um <em>chart-run</em>, desde que envolva a posição
	 * informada. Caso a posição informada seja <code>null</code>, será retornada a maior ocorrência independentemente de
	 * posições.
	 *
	 * @param chartRun
	 *            o <em>chart-run</em> analisado
	 * @param tipoVariacao
	 *            tipo de variação desejado
	 * @param posicaoEspecifica
	 *            filtra por uma posição específica (opcional)
	 * @return a maior variação do tipo informado ou vazio caso não exista
	 * @throws NullPointerException
	 *             caso qualquer dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o tipo de variação informado seja desconhecido
	 */
	public Optional<VariacaoPosicao> getMaiorVariacao(ChartRun chartRun, TipoVariacaoPosicao tipoVariacao, ElementoChartRun posicaoEspecifica) {
		logger.debug("Obtendo maior {} no chart-run: {}", tipoVariacao, chartRun);
		logger.debug("Restringindo por posição: {}", posicaoEspecifica);
		return posicaoEspecifica != null ? getMaiorVariacaoEspecifica(chartRun, tipoVariacao, posicaoEspecifica) : getMaiorVariacaoGeral(chartRun,
				tipoVariacao);
	}

	private Optional<VariacaoPosicao> getMaiorVariacaoEspecifica(ChartRun chartRun, TipoVariacaoPosicao tipoVariacao, ElementoChartRun posicao) {
		Optional<VariacaoPosicao> variacaoPosicao = Optional.empty();

		logger.debug("Iterando sobre {} estratégias", Iterators.size(estrategiasPosicaoEspecifica.iterator()));
		Iterator<MaiorVariacaoPosicaoEspecificaChartRunStrategy> iterator = estrategiasPosicaoEspecifica.iterator();
		while (iterator.hasNext()) {
			MaiorVariacaoPosicaoEspecificaChartRunStrategy estrategia = iterator.next();
			if (estrategia.getTipoVariacao() == tipoVariacao) {
				logger.debug("Utilizando estratégia: {}", estrategia);
				variacaoPosicao = estrategia.identificar(chartRun, posicao);
				logger.debug("Maior {} identificado(a): {}", tipoVariacao, variacaoPosicao);
				break;
			}
		}

		return variacaoPosicao;
	}

	private Optional<VariacaoPosicao> getMaiorVariacaoGeral(ChartRun chartRun, TipoVariacaoPosicao tipoVariacao) {
		Optional<VariacaoPosicao> variacaoPosicao = Optional.empty();

		logger.debug("Iterando sobre {} estratégias", Iterators.size(estrategias.iterator()));
		Iterator<MaiorVariacaoChartRunStrategy> iterator = estrategias.iterator();
		while (iterator.hasNext()) {
			MaiorVariacaoChartRunStrategy estrategia = iterator.next();
			if (estrategia.getTipoVariacao() == tipoVariacao) {
				logger.debug("Utilizando estratégia: {}", estrategia);
				variacaoPosicao = estrategia.identificar(chartRun);
				logger.debug("Maior {} identificado(a): {}", tipoVariacao, variacaoPosicao);
				break;
			}
		}

		return variacaoPosicao;
	}

	/**
	 * Obtém a maior ocorrência de um determinado tipo de variação dentro de um <em>chart-run</em>.
	 *
	 * @param chartRun
	 *            o <em>chart-run</em> analisado
	 * @param tipoVariacao
	 *            tipo de variação desejado
	 * @return a maior variação do tipo informado ou vazio caso não exista
	 * @throws NullPointerException
	 *             caso qualquer dos parâmetros seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o tipo de variação informado seja desconhecido
	 * @see #getMaiorVariacao(ChartRun, TipoVariacaoPosicao, ElementoChartRun)
	 */
	public Optional<VariacaoPosicao> getMaiorVariacao(ChartRun chartRun, TipoVariacaoPosicao tipoVariacao) {
		return getMaiorVariacao(chartRun, tipoVariacao, null);
	}

	/**
	 * Obtém a maior permanência em uma determinada posição no <em>chart-run</em>.
	 *
	 * @param chartRun
	 *            o <em>chart-run</em> analisado
	 * @param posicao
	 *            a posição a ser verificada
	 * @return a maior permanência na posição informada ou vazio caso a posição não exista no <em>chart-run</em>
	 * @throws NullPointerException
	 *             caso qualquer dos parâmetros seja <code>null</code>
	 */
	public Optional<PermanenciaPosicao> getMaiorPermanencia(ChartRun chartRun, ElementoChartRun posicao) {
		logger.debug("Obtendo maior permanência na posição {} no chart-run: {}", posicao, chartRun);
		int permanencia = chartRun.getElementos().stream().filter(elemento -> elemento.equals(posicao)).collect(Collectors.counting()).intValue();
		logger.debug("Permanência identificada: {}", permanencia);
		return permanencia != 0 ? Optional.of(new PermanenciaPosicao(posicao, permanencia)) : Optional.empty();
	}
}
