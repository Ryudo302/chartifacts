package br.com.colbert.chartifacts.infraestrutura.comparator;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * {@link Comparator} que permite a comparação através de qualquer propriedade dos objetos, desde que implementem
 * {@link Comparable}.
 *
 * @author Thiago Colbert
 * @since 29/08/2012
 *
 * @param <T>
 *            tipo de objeto a ser comparado
 */
public class PropertyComparator<T extends Comparable<T>> implements Comparator<T> {

	private static final TipoOrdenacao TIPO_ORDENACAO_PADRAO = TipoOrdenacao.CRESCENTE;

	private String nomePropriedade;
	private TipoOrdenacao tipoOrdenacao;

	public PropertyComparator(String nomePropriedade, TipoOrdenacao tipoOrdenacao) {
		this.nomePropriedade = nomePropriedade;
		this.tipoOrdenacao = tipoOrdenacao;
	}

	public PropertyComparator(String nomePropriedade) {
		this(nomePropriedade, TIPO_ORDENACAO_PADRAO);
	}

	@Override
	public int compare(T objeto1, T objeto2) {
		Object valor1 = getValorPropriedade(nomePropriedade, objeto1);
		Object valor2 = getValorPropriedade(nomePropriedade, objeto2);

		validarPropriedadeParaComparacao(valor1);
		validarPropriedadeParaComparacao(valor2);

		return CompareToBuilder.reflectionCompare(valor1, valor2) * tipoOrdenacao.getIndice();
	}

	private void validarPropriedadeParaComparacao(Object valorPropriedade) {
		if (!(valorPropriedade instanceof Comparable)) {
			throw new IllegalStateException("A propriedade " + nomePropriedade + " não implementa a interface Comparable.");
		}
	}

	private Object getValorPropriedade(String nomePropriedade, T objeto) {
		try {
			return PropertyUtils.getProperty(objeto, nomePropriedade);
		} catch (IllegalAccessException exception) {
			throw new IllegalStateException("A propriedade " + nomePropriedade + " não é acessível", exception);
		} catch (InvocationTargetException exception) {
			throw new IllegalStateException("Ocorreu uma exceção inesperada ao recuperar o valor da propriedade " + nomePropriedade, exception);
		} catch (NoSuchMethodException exception) {
			throw new IllegalStateException("A classe " + objeto.getClass() + " não possui um método getter para a propriedade " + nomePropriedade);
		}
	}
}
