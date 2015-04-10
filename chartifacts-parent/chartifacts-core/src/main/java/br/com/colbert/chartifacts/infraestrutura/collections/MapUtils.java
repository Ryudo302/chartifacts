package br.com.colbert.chartifacts.infraestrutura.collections;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

/**
 * Classe utilitária para operações envolvendo {@link Map}.
 *
 * @author Thiago Colbert
 * @since 12/03/2015
 */
public final class MapUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapUtils.class);

	private MapUtils() {

	}

	/**
	 * Verifica se um mapa está vazio ou é <code>null</code>.
	 *
	 * @param map
	 *            o mapa a ser verificado
	 * @return <code>true</code> caso o mapa esteja vazio ou seja <code>null</code>, <code>false</code> caso contrário
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return org.apache.commons.collections4.MapUtils.isEmpty(map);
	}

	/**
	 * Ordena um mapa a partir de seus valores utilizando o <em>comparator</em> informado.
	 *
	 * @param map
	 *            mapa a ser ordenado
	 * @param comparator
	 *            utilizado na ordenação
	 * @return o mapa ordenado
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Comparator<V> comparator) {
		LOGGER.debug("Ordenando mapa pelos valores utilizando comparator {}: {}", comparator, map);

		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> stream = map.entrySet().stream();
		stream.sorted(Comparator.comparing(mapEntry -> mapEntry, (entry1, entry2) -> {
			int resultado = compareByValue(entry1, entry2, comparator);
			return resultado == 0 ? compareByKey(entry1, entry2) : resultado;
		})).forEach(mapEntry -> result.put(mapEntry.getKey(), mapEntry.getValue()));

		LOGGER.debug("Mapa ordenado: {}", result);
		return result;
	}

	private static <K, V extends Comparable<? super V>> int compareByValue(Entry<K, V> entry1, Entry<K, V> entry2, Comparator<V> comparator) {
		return comparator.compare(entry1.getValue(), entry2.getValue());
	}

	@SuppressWarnings("unchecked")
	private static <K, V extends Comparable<? super V>> int compareByKey(Entry<K, V> entry1, Entry<K, V> entry2) {
		K key1 = entry1.getKey();
		if (key1 instanceof Comparable) {
			return ((Comparable<K>) key1).compareTo(entry2.getKey());
		} else {
			throw new IllegalArgumentException("Não implementa Comparable: " + key1.getClass());
		}
	}

	/**
	 * <p>
	 * Obtém um mapa a partir do mapa informado que contenha todos os elementos, exceto aqueles cujo valor seja:
	 * <ul>
	 * <li><strong>Número:</strong> <code>0</code></li>
	 * <li><strong>String:</strong> <code>null</code> ou vazio</li>
	 * <li><strong>Objeto:</strong> <code>null</code></li>
	 * </ul>
	 * </p>
	 *
	 * @param map
	 *            mapa a ser utilizado
	 * @return o mapa filtrado
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> removeZeroesValues(Map<K, V> map) {
		return map.entrySet().stream().filter(entry -> notZeroValue(entry)).collect(Collectors.toMap(new Function<Entry<K, V>, K>() {
			@Override
			public K apply(Entry<K, V> entry) {
				return entry.getKey();
			}
		}, new Function<Entry<K, V>, V>() {
			@Override
			public V apply(Entry<K, V> entry) {
				return entry.getValue();
			}
		}));
	}

	private static <K, V extends Comparable<? super V>> boolean notZeroValue(Entry<K, V> entry) {
		V value = entry.getValue();
		if (value instanceof Number) {
			return value != null && ((Number) value).longValue() != 0L;
		} else if (value instanceof String) {
			return StringUtils.isNotBlank((CharSequence) value);
		} else {
			return value != null;
		}
	}
}
