package br.com.colbert.chartifacts.negocio.relatorios;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jglue.cdiunit.AdditionalPackages;

import br.com.colbert.chartifacts.dominio.chart.ChartRun;
import br.com.colbert.chartifacts.dominio.historico.*;
import br.com.colbert.chartifacts.infraestrutura.io.HistoricoParadaFileParser;
import br.com.colbert.chartifacts.negocio.chartrun.ChartRunAnalyzer;
import br.com.colbert.chartifacts.negocio.relatorios.generator.impl.AbstractRelatorioGenerator;
import br.com.colbert.chartifacts.tests.support.AbstractTestCase;

/**
 * Classe-base para todos os testes de classes do pacote de relatórios.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
@AdditionalPackages({ ChartRun.class, AbstractRelatorioGenerator.class, ChartRunAnalyzer.class })
public abstract class AbstractRelatorioTest extends AbstractTestCase {

	private static final String NOME_ARQUIVO_HISTORICO = "historico.txt";
	private static final int QUANTIDADE_POSICOES_PARADA = 20;

	@Inject
	protected HistoricoParadaFileParser parser;

	protected HistoricoParada parseHistoricoParada() throws ParserException {
		return parser.parse(loadRecurso(NOME_ARQUIVO_HISTORICO), QUANTIDADE_POSICOES_PARADA);
	}

	private File loadRecurso(String caminho) {
		return FileUtils.toFile(Thread.currentThread().getContextClassLoader().getResource(caminho));
	}
}
