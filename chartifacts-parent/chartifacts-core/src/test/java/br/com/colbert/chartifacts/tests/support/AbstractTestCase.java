package br.com.colbert.chartifacts.tests.support;

import org.jboss.weld.log.LoggerProducer;
import org.jglue.cdiunit.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * Classe-base para todos os testes da aplicação.
 *
 * @author Thiago Colbert
 * @since 08/12/2014
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({ LoggerProducer.class })
public abstract class AbstractTestCase {

	@Rule
	public TestRule printTestName() {
		return new PrintTestNameWatcher();
	}
}