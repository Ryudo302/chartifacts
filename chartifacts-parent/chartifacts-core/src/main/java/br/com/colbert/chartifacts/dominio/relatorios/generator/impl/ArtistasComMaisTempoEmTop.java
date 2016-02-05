package br.com.colbert.chartifacts.dominio.relatorios.generator.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.HistoricoParada;
import br.com.colbert.chartifacts.dominio.chartrun.ElementoChartRun;
import br.com.colbert.chartifacts.dominio.musica.Artista;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.generator.*;

/**
 * Identifica os artistas com maior tempo acumulado dentro de um top.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@RelatorioGeneratorFlow(tipoEntidade = TipoEntidade.ARTISTA, tipoVariacao = TipoVariacao.MAIOR, tipoOcorrencia = TipoOcorrencia.TEMPO, tipoLocal = TipoLocal.TOP)
public class ArtistasComMaisTempoEmTop extends AbstractRelatorioGenerator<Artista, Integer> {

	private static final long serialVersionUID = 2091891652660676343L;

	@Inject
	private transient Logger logger;

	private ElementoChartRun posicao;

	@Override
	protected Optional<Relatorio<Artista, Integer>> gerarRelatorio(HistoricoParada historico) {
		if (posicao == null) {
			throw new IllegalStateException("A posição de corte não foi definida");
		}

		logger.debug("Top {}", posicao);
		Map<Artista, Integer> itens = new HashMap<>();
		historico.getItens().stream()
				.forEach(itemHistorico -> atualizarPermanencia(
						itemHistorico.getCancao().getArtistas(), itemHistorico.getChartRun().getElementos().stream()
								.filter(elemento -> elemento.isPresenca() && elemento.compareTo(posicao) <= 0).collect(Collectors.counting()).intValue(),
				itens));
		return criarRelatorio(itens);
	}

	private void atualizarPermanencia(List<Artista> artistas, int permanenciaNoTopCancao, Map<Artista, Integer> itens) {
		artistas.forEach(artista -> {
			Integer permanenciaNoTopArtista = itens.getOrDefault(artista, 0);
			permanenciaNoTopArtista += permanenciaNoTopCancao;
			itens.put(artista, permanenciaNoTopArtista);
		});
	}

	/**
	 * Define a posição de corte do top.
	 *
	 * @param posicao
	 *            a posição de corte
	 */
	public void setPosicao(ElementoChartRun posicao) {
		Validate.isTrue(posicao.isPresenca(), "A posição não pode ser uma ausência");
		this.posicao = posicao;
	}
}
