package br.com.colbert.chartifacts.dominio.relatorios.generator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.*;
import javax.inject.Inject;

/**
 * <em>Factory</em> utilizada para obter instâncias de {@link RelatorioGenerator}.
 * 
 * @author Thiago Miranda
 * @since 5 de fev de 2016
 */
@ApplicationScoped
public class RelatorioGeneratorFactory implements Serializable {

	private static final long serialVersionUID = -5749962001802260440L;

	@Inject
	@Any
	private transient Instance<RelatorioGenerator<?, ?>> relatorioGenerators;

	/**
	 * Obtém uma instância de {@link RelatorioGenerator} que corresponda às configurações informadas.
	 * 
	 * @param config
	 *            informações do gerador de relatório desejado
	 * @return a instância correspondente às informações (pode estar vazio)
	 * @throws NullPointerException
	 *             caso seja informado <code>null</code>
	 */
	public Optional<RelatorioGenerator<?, ?>> get(final RelatorioGeneratorConfig config) {
		Objects.requireNonNull(config, "config");

		Spliterator<RelatorioGenerator<?, ?>> spliterator = relatorioGenerators.spliterator();
		Stream<RelatorioGenerator<?, ?>> compartibleGenerators = StreamSupport.stream(() -> spliterator, spliterator.characteristics(), false)
				.filter(currentGenerator -> {
					return config.matches(currentGenerator.getClass().getAnnotation(RelatorioGeneratorFlow.class));
				});

		return compartibleGenerators.findFirst();
	}
}
