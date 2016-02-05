package br.com.colbert.chartifacts.dominio.relatorios.generator;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Informações sobre um {@link RelatorioGenerator}. É utilizado pela {@link RelatorioGeneratorFactory} para identificar a instância apropriada do gerador.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
public class RelatorioGeneratorConfig implements Serializable {

	private static final long serialVersionUID = -8971056487504721112L;

	private TipoEntidade tipoEntidade;
	private TipoVariacao tipoVariacao;
	private TipoOcorrencia tipoOcorrencia;
	private TipoLocal tipoLocal;

	private RelatorioGeneratorConfig() {
		tipoEntidade = null;
		tipoVariacao = null;
		tipoOcorrencia = TipoOcorrencia.GERAL;
		tipoLocal = TipoLocal.PARADA;
	}

	/**
	 * Relatório de artistas.
	 * 
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public static RelatorioGeneratorConfig artista() {
		RelatorioGeneratorConfig config = new RelatorioGeneratorConfig();
		config.tipoEntidade = TipoEntidade.ARTISTA;
		return config;
	}

	/**
	 * Relatório de canções.
	 * 
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public static RelatorioGeneratorConfig cancao() {
		RelatorioGeneratorConfig config = new RelatorioGeneratorConfig();
		config.tipoEntidade = TipoEntidade.CANCAO;
		return config;
	}

	public TipoEntidade getTipoEntidade() {
		return tipoEntidade;
	}

	public TipoVariacao getTipoVariacao() {
		return tipoVariacao;
	}

	/**
	 * Define a variação utilizada no relatório.
	 * 
	 * @param tipoVariacao
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public RelatorioGeneratorConfig com(TipoVariacao tipoVariacao) {
		this.tipoVariacao = tipoVariacao;
		return this;
	}

	public TipoOcorrencia getTipoOcorrencia() {
		return tipoOcorrencia;
	}

	/**
	 * Define qual ocorrência é utilizada no relatório.
	 * 
	 * @param tipoOcorrencia
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public RelatorioGeneratorConfig ocorrencia(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
		return this;
	}

	public TipoLocal getTipoLocal() {
		return tipoLocal;
	}

	/**
	 * Define qual local da parada é utilizado no relatório.
	 * 
	 * @param tipoLocal
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public RelatorioGeneratorConfig em(TipoLocal tipoLocal) {
		this.tipoLocal = tipoLocal;
		return this;
	}

	/**
	 * Verifica se os valores definidos na instância equivalem aos valores da anotação informada. Este método é <em>null-safe</em>.
	 * 
	 * @param annotation
	 *            a anotação a ser analisada
	 * @return <code>true</code>/<code>false</code>
	 */
	public boolean matches(RelatorioGeneratorFlow annotation) {
		return annotation != null && annotation.tipoEntidade() == tipoEntidade && annotation.tipoVariacao() == tipoVariacao
				&& ArrayUtils.contains(annotation.tipoOcorrencia(), tipoOcorrencia) && annotation.tipoLocal() == tipoLocal;
	}
}
