package br.com.colbert.chartifacts.negocio.parser;

/**
 * <em>Parser</em> que permite a obtenção de instâncias de uma classe a partir de uma fonte de dados externa.
 * 
 * @author ThiagoColbert
 * @since 27 de fev de 2016
 *
 * @param <S>
 *            tipo de fonte de dados
 * @param <D>
 *            tipo de instância criada
 */
public interface Parser<S, D> {

	/**
	 * Obtém uma instância a partir da fonte informada.
	 * 
	 * @param fonte
	 *            a fonte de dados a ser analisada
	 * @param quantidadePosicoesParada
	 *            a quantidade total de posições da parada
	 * @return a instância criada
	 * @throws ParserException
	 *             caso ocorra algum erro ao ler a fonte externa
	 */
	D parse(S fonte, int quantidadePosicoesParada) throws ParserException;
}
