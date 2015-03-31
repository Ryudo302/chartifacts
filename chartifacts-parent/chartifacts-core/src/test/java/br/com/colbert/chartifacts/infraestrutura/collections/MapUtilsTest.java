package br.com.colbert.chartifacts.infraestrutura.collections;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;
import java.util.Map.Entry;

import org.junit.Test;

/**
 * Testes unitários da classe {@link MapUtils}.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class MapUtilsTest {

	@Test
	public void testIsEmptyComNullDeveriaRetornarTrue() {
		assertThat(MapUtils.isEmpty(null), is(true));
	}

	@Test
	public void testIsEmptyComMapaVazioDeveriaRetornarTrue() {
		assertThat(MapUtils.isEmpty(Collections.emptyMap()), is(true));
	}

	@Test
	public void testIsEmptyComMapaNaoVazioDeveriaRetornarFalse() {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(1, 2);

		assertThat(MapUtils.isEmpty(map), is(false));
	}

	@Test
	public void testSortByValue() {
		HashMap<Integer, Integer> map = new HashMap<>();

		map.put(1, 4);
		map.put(4, 1);
		map.put(2, 3);
		map.put(3, 2);

		Map<Integer, Integer> sortedMap = MapUtils.sortByValue(map, (a, b) -> a.compareTo(b));
		Set<Entry<Integer, Integer>> entries = sortedMap.entrySet();
		Iterator<Entry<Integer, Integer>> iterator = entries.iterator();

		Entry<Integer, Integer> primeiro = iterator.next();
		assertThat(primeiro.getKey(), is(equalTo(4)));

		Entry<Integer, Integer> segundo = iterator.next();
		assertThat(segundo.getKey(), is(equalTo(3)));

		Entry<Integer, Integer> terceiro = iterator.next();
		assertThat(terceiro.getKey(), is(equalTo(2)));

		Entry<Integer, Integer> quarto = iterator.next();
		assertThat(quarto.getKey(), is(equalTo(1)));
	}
}
