package br.com.colbert.chartifacts.infraestrutura.io;

import java.io.Serializable;

/**
 * Classe contendo os nomes das propriedades utilizadas pelos <em>parser</em>s.
 * 
 * @author Thiago Colbert
 * @since 8 de mai de 2016
 */
public final class ParserProperties implements Serializable {

	private static final long serialVersionUID = 8635410354116025576L;

	public static final String NUMERO_POSICAO_KEY = "posicao.numero";

	public static final String NOME_ARTISTA_KEY = "artista.nome";
	public static final String SEPARADORES_ARTISTAS_KEY = "artista.separadores";
	public static final String SEPARADOR_ARTISTAS_E_CANCAO_KEY = "artista.cancao.separador";
	
	public static final String TITULO_CANCAO_KEY = "cancao.titulo";
	public static final String SEPARADOR_TITULOS_ALTERNATIVOS_CANCAO_KEY = "cancao.titulosAlternativos.separador";
	
	public static final String VARIACAO_POSICAO_KEY = "estatisticas.variacaoPosicao";
	public static final String ESTATISTICAS_PERMANENCIA_AND_PEAK_KEY = "estatisticas.permanenciaAndPeak";

	public static final String SEPARADOR_POSICOES_CHARTRUN_KEY = "chartrun.posicoes.separador";
	
	public static final String PERIODO_INTERVALO_KEY = "periodo.intervalo";
	
	public static final String FORMATO_DATAS_KEY = "datas.formato";



	private ParserProperties() {

	}
}
