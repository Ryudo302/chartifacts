package br.com.colbert.chartifacts.negocio.parser;

import br.com.colbert.chartifacts.dominio.historico.HistoricoParada;

/**
 * <em>Parser</em> que permite a obtenção de instâncias de {@link HistoricoParada} a partir de uma fonte externa.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 *
 * @param <T>
 *            o tipo de fonte externa
 */
public interface HistoricoParadaParser<T> extends Parser<T, HistoricoParada> {

}
