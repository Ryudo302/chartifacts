package br.com.colbert.chartifacts.negocio.relatorios.generator;

import java.util.*;
import java.util.stream.Stream;

import br.com.colbert.chartifacts.negocio.chartrun.TipoVariacaoPosicao;

/**
 * Tipos de ocorrências que podem ser utilizados pelos relatórios.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
public enum TipoOcorrencia {

	/**
	 * Indica uma ocorrência geral, sem especificar o que seja.
	 */
	GERAL(null),

	SUBIDA(TipoVariacaoPosicao.SUBIDA),

	QUEDA(TipoVariacaoPosicao.QUEDA),

	ESTREIA(TipoVariacaoPosicao.ESTREIA),

	SAIDA(TipoVariacaoPosicao.SAIDA),

	RETORNO(TipoVariacaoPosicao.RETORNO),

	TEMPO(null);

	private TipoVariacaoPosicao tipoVariacaoPosicao;

	TipoOcorrencia(TipoVariacaoPosicao tipoVariacaoPosicao) {
		this.tipoVariacaoPosicao = tipoVariacaoPosicao;
	}

	/**
	 * Obtém o {@link TipoOcorrencia} equivalente a um {@link TipoVariacaoPosicao}.
	 * 
	 * @param tipoVariacao
	 *            o tipo de variação de posição
	 * @return o tipo de ocorrência equivalente
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso não seja encontrado um tipo de ocorrência
	 */
	public static TipoOcorrencia valueOf(TipoVariacaoPosicao tipoVariacao) {
		Objects.requireNonNull(tipoVariacao, "tipoVariacao");
		Optional<TipoOcorrencia> tipoOcorrencia = Stream.of(values())
				.filter(tipoOcorrenciaAtual -> tipoOcorrenciaAtual.getTipoVariacaoPosicao() == tipoVariacao).findFirst();
		if (tipoOcorrencia.isPresent()) {
			return tipoOcorrencia.get();
		} else {
			throw new IllegalArgumentException("Tipo de variação desconhecido: " + tipoVariacao);
		}
	}

	public TipoVariacaoPosicao getTipoVariacaoPosicao() {
		return tipoVariacaoPosicao;
	}
}
