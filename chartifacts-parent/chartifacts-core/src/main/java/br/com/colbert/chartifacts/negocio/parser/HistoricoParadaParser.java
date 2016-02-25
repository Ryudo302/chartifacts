package br.com.colbert.chartifacts.negocio.parser;

import br.com.colbert.chartifacts.dominio.historico.*;

/**
 * <em>Parser</em> que permite a obtenção de instâncias de {@link HistoricoParada} a partir de uma fonte externa.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 *
 * @param <T>
 *            o tipo de fonte externa
 */
public interface HistoricoParadaParser<T> {

	/**
	 * Obtém uma instância de {@link HistoricoParada} a partir da fonte informada.
	 *
	 * @param fonte
	 *            a fonte a ser analizada
	 * @param quantidadePosicoesParada
	 *            a quantidade total de posições da parada
	 * @return a instância criada
	 * @throws ParserException
	 *             caso ocorra algum erro ao ler a fonte externa
	 */
	HistoricoParada parse(T fonte, int quantidadePosicoesParada) throws ParserException;
}
