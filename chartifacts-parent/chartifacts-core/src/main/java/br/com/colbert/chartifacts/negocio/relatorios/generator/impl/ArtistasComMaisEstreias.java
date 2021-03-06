package br.com.colbert.chartifacts.negocio.relatorios.generator.impl;

import java.util.*;
import java.util.stream.Collectors;

import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.dominio.musica.*;
import br.com.colbert.chartifacts.negocio.relatorios.Relatorio;
import br.com.colbert.chartifacts.negocio.relatorios.generator.*;

/**
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.ARTISTA, tipoVariacao = TipoVariacao.MAIOR, tipoOcorrencia = TipoOcorrencia.ESTREIA)
public class ArtistasComMaisEstreias extends AbstractRelatorioGenerator<Artista, Integer> {

	private static final long serialVersionUID = -6428702912696633539L;

	@Override
	protected Optional<Relatorio<Artista, Integer>> gerarRelatorio(HistoricoParada historico) {
		Map<Artista, Integer> itens = new HashMap<>();
		historico.getItens().stream().map(ItemHistoricoParada::getCancao).map(Cancao::getArtistas).map(List::stream)
				.forEach(artistasStream -> artistasStream.collect(Collectors.toList()).forEach(artista -> atualizarOcorrencias(artista, itens)));
		return criarRelatorio(itens);
	}

	private void atualizarOcorrencias(Artista artista, Map<Artista, Integer> itens) {
		Integer ocorrencias = itens.get(artista);

		if (ocorrencias == null) {
			ocorrencias = 1;
		} else {
			ocorrencias++;
		}

		itens.put(artista, ocorrencias);
	}
}
