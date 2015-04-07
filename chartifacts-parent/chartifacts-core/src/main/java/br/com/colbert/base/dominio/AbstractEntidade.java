package br.com.colbert.base.dominio;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;

/**
 * Classe-base para todas as implementações de entidades de domínio da aplicação.
 *
 * @author Thiago Colbert
 * @since 11/03/2015
 */
public abstract class AbstractEntidade implements Entidade, Serializable {

	private static final long serialVersionUID = -6837156660941198873L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
