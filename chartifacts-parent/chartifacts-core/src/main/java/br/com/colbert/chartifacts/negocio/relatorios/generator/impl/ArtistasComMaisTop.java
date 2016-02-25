package br.com.colbert.chartifacts.negocio.relatorios.generator.impl;

import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.PosicaoChart;
import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.negocio.relatorios.Relatorio;
import br.com.colbert.chartifacts.negocio.relatorios.generator.*;

/**
 * Identifica os artistas com maior número de canções que atingiram um determinado top.
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.ARTISTA, tipoVariacao = TipoVariacao.MAIOR, tipoLocal = TipoLocal.TOP)
public class ArtistasComMaisTop extends AbstractRelatorioGenerator<Artista, Integer> {

	private static final long serialVersionUID = 2091891652660676343L;

	@Inject
	private Logger logger;

	private PosicaoChart posicao;

	@Override
	protected Optional<Relatorio<Artista, Integer>> gerarRelatorio(HistoricoParada historico) {
		if (posicao == null) {
			throw new IllegalStateException("A posição de corte não foi definida");
		}

		logger.debug("Artistas com mais top {}", posicao);

		Map<Artista, Integer> itens = new HashMap<>();
		historico.getItens().stream().filter(item -> item.getEstatisticas().getMelhorPosicao().getPosicao().compareTo(posicao) <= 0)
				.map(ItemHistoricoParada::getCancao).forEach(cancao -> atualizarOcorrencias(cancao, itens));
		return criarRelatorio(itens);
	}

	private void atualizarOcorrencias(Cancao cancao, Map<Artista, Integer> itens) {
		cancao.getArtistas().forEach(artista -> {
			Integer ocorrencias = itens.getOrDefault(artista, 0);
			ocorrencias++;
			itens.put(artista, ocorrencias);
		});
	}

	/**
	 * Define a posição de corte do top.
	 *
	 * @param posicao
	 *            a posição de corte
	 */
	public void setPosicao(PosicaoChart posicao) {
		Validate.isTrue(posicao.isPresenca(), "A posição não pode ser uma ausência");
		this.posicao = posicao;
	}
}
