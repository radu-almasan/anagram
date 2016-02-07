package com.radualmasan.anagram.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * @author almasan.radu@gmail.com
 */
public class MapBasedAnagramStrategyTest {

	/** The tested strategy. */
	private final MapBasedAnagramStrategy strategy = new MapBasedAnagramStrategy();

	@Test
	public void testWith3WordsDifferentLenghtNoPairs() throws Exception {
		final String[] words = { "from", "for", "testing" };
		Arrays.asList(words).forEach(strategy::ingest);
		final List<Set<String>> anagrams = strategy.getAnagramPairs().collect(Collectors.toList());
		assertEquals(0, anagrams.size());
	}

	@Test
	public void testWith3WordsPairOf2() {
		final String[] words = { "from", "form", "test" };
		Arrays.asList(words).forEach(strategy::ingest);
		final List<Set<String>> anagrams = strategy.getAnagramPairs().collect(Collectors.toList());
		assertEquals(1, anagrams.size());
		final Set<String> pair = anagrams.get(0);
		assertEquals(2, pair.size());
		assertThat(pair, CoreMatchers.hasItems(Arrays.copyOfRange(words, 0, 1)));
	}
}
