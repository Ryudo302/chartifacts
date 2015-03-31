package br.com.colbert.chartifacts.infraestrutura.export;

import java.io.Serializable;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.dominio.relatorios.Relatorio;
import br.com.colbert.chartifacts.dominio.relatorios.export.*;

/**
 * Implementação de {@link RelatorioExporter} que gera uma {@link String} contendo os dados do relatório.
 *
 * @author Thiago Colbert
 * @since 16/03/2015
 */
public class RelatorioTextExporter implements RelatorioExporter<String>, Serializable {

	private static final long serialVersionUID = -7505314036169243246L;

	@Inject
	private Logger logger;

	private int larguraPrimeiraColuna;

	@Override
	public FormatoRelatorio getFormato() {
		return FormatoRelatorio.TXT;
	}

	/**
	 * @throws NullPointerException
	 *             caso o relatório seja <code>null</code>
	 */
	@Override
	public <T extends Entidade, V extends Comparable<V>> String export(Relatorio<T, V> relatorio, ToFormatedStringConverter<T> toStringKey,
			ToFormatedStringConverter<V> toStringValue) {
		Objects.requireNonNull(relatorio, "Relatório");

		logger.debug("Relatório: {}", relatorio);
		logger.debug("Conversor de chaves: {}", toStringKey);
		logger.debug("Conversor de valores: {}", toStringValue);

		StringBuilder builder = new StringBuilder();
		relatorio.getItens().forEach(
				(key, value) -> builder.append(StringUtils.rightPad(toString(key, toStringKey), larguraPrimeiraColuna))
						.append(toString(value, toStringValue)).append(StringUtils.LF));
		return builder.toString();
	}

	private <T> String toString(T key, ToFormatedStringConverter<T> toStringConverter) {
		return toStringConverter != null ? toStringConverter.convert(key) : key.toString();
	}

	public RelatorioTextExporter setLarguraPrimeiraColuna(int larguraPrimeiraColuna) {
		this.larguraPrimeiraColuna = larguraPrimeiraColuna;
		return this;
	}
}
