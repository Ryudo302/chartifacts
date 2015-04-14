package br.com.colbert.chartifacts.infraestrutura.aplicacao;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;

/**
 * Informações sobre a aplicação.
 *
 * @author Thiago Colbert
 * @since 14/04/2015
 */
public class InformacoesAplicacao implements Serializable {

	private static final long serialVersionUID = -3919971095541293773L;

	private final String nome;
	private final String versao;
	private final String numeroBuild;
	private final String autor;

	public InformacoesAplicacao(String nome, String versao, String numeroBuild, String autor) {
		this.nome = nome;
		this.versao = versao;
		this.numeroBuild = numeroBuild;
		this.autor = autor;
	}

	public String getNome() {
		return nome;
	}

	public String getVersao() {
		return versao;
	}

	public String getNumeroBuild() {
		return numeroBuild;
	}

	public String getAutor() {
		return autor;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}