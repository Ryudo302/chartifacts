package br.com.colbert.chartifacts.dominio.relatorios;

import java.util.*;

import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;
import br.com.colbert.chartifacts.dominio.relatorios.generator.RelatorioGenerator;

/**
 * Configurações para utilização do {@link RelatoriosFacade}.
 *
 * @author Thiago Colbert
 * @since 19/03/2015
 */
public interface RelatoriosConfiguration {

	/**
	 * O limite de tamanho dos relatórios exportados.
	 *
	 * @return o limite de tamanho
	 */
	Optional<Integer> limiteTamanho();

	/**
	 * Define a largura da primeira coluna dos relatórios exportados.
	 *
	 * @return a largura da primeira coluna
	 */
	int larguraPrimeiraColuna();

	/**
	 * {@link String} utilizada para indicar a separação entre um relatório e outro.
	 *
	 * @return o separador de relatórios
	 */
	String separador();

	/**
	 * Obtém as posições definidas para a geração de um relatório.
	 *
	 * @return as posições definidas
	 */
	<T extends RelatorioGenerator<?, ?>> Collection<ElementoChartRun> posicoesRelatorio(T relatorioGenerator);

	/**
	 * Obtém o título definido para um determinado relatório.
	 *
	 * @param relatorioGenerator
	 *            o gerador de relatório
	 * @param args
	 *            argumentos que venham a ser utilizados para formatar o título do relatório
	 * @return o título do relatório
	 */
	<T extends RelatorioGenerator<?, ?>> String getTituloRelatorio(T relatorioGenerator, Object... args);
}
