package br.com.colbert.chartifacts.infraestrutura.export;

import java.io.*;
import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.colbert.base.dominio.Entidade;
import br.com.colbert.chartifacts.negocio.relatorios.Relatorio;
import br.com.colbert.chartifacts.negocio.relatorios.export.*;

/**
 * Implementação de {@link RelatorioExporter} que gera uma planilha contendo os dados do relatório.
 *
 * @author Thiago Colbert
 * @since 18/03/2015
 */
public class RelatorioXLSXExporter implements RelatorioExporter<InputStream>, Serializable {

	private static final long serialVersionUID = -7279485724590743674L;

	@Inject
	private Logger logger;

	private File template;

	@Override
	public FormatoRelatorio getFormato() {
		return FormatoRelatorio.XLSX;
	}

	@Override
	public <T extends Entidade, V extends Comparable<V>> InputStream export(Relatorio<T, V> relatorio, ToFormatedStringConverter<T> toStringKey,
			ToFormatedStringConverter<V> toStringValue) {
		Objects.requireNonNull(relatorio, "Relatório");

		logger.debug("Relatório: {}", relatorio);
		logger.debug("Conversor de chaves: {}", toStringKey);
		logger.debug("Conversor de valores: {}", toStringValue);

		return null;
	}

	public RelatorioXLSXExporter setTemplate(File template) {
		this.template = template;
		return this;
	}
}
