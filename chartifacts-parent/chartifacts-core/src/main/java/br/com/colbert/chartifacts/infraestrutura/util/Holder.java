package br.com.colbert.chartifacts.infraestrutura.util;

import org.apache.commons.lang3.builder.*;

/**
 * Classe que permite guardar e alterar um valor qualquer.
 * 
 * @author ThiagoColbert
 * @since 25 de fev de 2016
 */
public class Holder<T> {

	private T value;

	public T getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return <code>this</code>, para chamadas encadeadas de método
	 */
	public Holder<T> setValue(T value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
