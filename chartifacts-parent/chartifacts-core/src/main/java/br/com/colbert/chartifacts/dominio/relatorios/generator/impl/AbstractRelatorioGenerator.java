package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.builder.*;
import org.slf4j.Logger;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.generator.RelatorioGenerator;
import br.com.colbert.chartifacts.infraestrutura.collections.MapUtils;

/**
 * Classe-base para todas as implementações de geradores de relatórios.
 *
 * @author Thiago Colbert
 * @since 14/03/2015
 *
 * @param <T>
 *            o tipo de objeto listado nos relatórios gerados
 * @param <V>
 *            o tipo de valor listado nos relatórios gerados
 */
@ApplicationScoped
public abstract class AbstractRelatorioGenerator<T extends Entidade, V extends Comparable<V>> implements RelatorioGenerator<T, V>, Serializable {

	private static final long serialVersionUID = -2777378202837103067L;

	@Inject
	private transient Logger logger;

	private Integer limiteTamanho;
	private boolean retirarZeros;

	@Override
	public Optional<Relatorio<T, V>> gerar(HistoricoParada historico) {
		Optional<Relatorio<T, V>> relatorioOptional = gerarRelatorio(historico);
		relatorioOptional.ifPresent(relatorio -> {
			logger.debug("Limitando o tamanho do relatório em {} (null indica nenhum limite)", limiteTamanho);
			relatorio.limitarTamanho(limiteTamanho);
		});
		return relatorioOptional;
	}

	protected abstract Optional<Relatorio<T, V>> gerarRelatorio(HistoricoParada historico);

	/**
	 * Método utilitário para criar um relatório a partir de um mapa de itens. Caso o mapa esteja vazio, será retornado vazio.
	 *
	 * @param itens
	 *            os itens do relatório
	 * @return o relatório contendo os itens informados ou vazio caso não sejam informados itens
	 */
	protected Optional<Relatorio<T, V>> criarRelatorio(Map<T, V> itens) {
		return MapUtils.isEmpty(itens) ? Optional.empty() : Optional.of(new Relatorio<>(retirarZeros ? MapUtils.removeZeroesValues(itens) : itens, (
				valor1, valor2) -> valor2.compareTo(valor1)));
	}

	public Integer getLimiteTamanho() {
		return limiteTamanho;
	}

	public void setLimiteTamanho(Integer limiteTamanho) {
		this.limiteTamanho = limiteTamanho;
	}

	public boolean isRetirarZeros() {
		return retirarZeros;
	}

	/**
	 * Define se elementos com valor zero devem ser omitidos do relatório. Como "zero" entenda-se os valores <code>0</code> e
	 * <code>null</code>.
	 *
	 * @param retirarZeros
	 */
	public void setRetirarZeros(boolean retirarZeros) {
		this.retirarZeros = retirarZeros;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
