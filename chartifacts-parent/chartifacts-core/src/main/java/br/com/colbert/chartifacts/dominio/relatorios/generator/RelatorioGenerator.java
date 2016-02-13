package br.com.colbert.chartifacts.dominio.relatorios.generator;

import java.util.Optional;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;

/**
 * Permite a geração de um relatório a partir de um {@link HistoricoParada}.
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 *
 * @param <T>
 *            o tipo de objeto listado nos relatórios gerados
 * @param <V>
 *            o tipo de valor listado nos relatórios gerados
 */
public interface RelatorioGenerator<T extends Entidade, V extends Comparable<V>> {

	/**
	 * Gera um relatório a partir do histórico informado.
	 *
	 * @param historico
	 *            o histórico de parada musical
	 * @return o relatório gerado (vazio caso não seja identificado nenhum dado)
	 */
	Optional<Relatorio<T, V>> gerar(HistoricoParada historico);

	/**
	 * Define o limite de tamanho dos relatórios gerados.
	 * 
	 * @param limiteTamanho
	 *            o limite de tamanho dos relatórios
	 */
	void setLimiteTamanho(Integer limiteTamanho);
}
