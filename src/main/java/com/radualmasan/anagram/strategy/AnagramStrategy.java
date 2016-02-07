package com.radualmasan.anagram.strategy;

import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author developer
 *
 */
public interface AnagramStrategy {

	/**
	 * @return a stream of anagram pairs.
	 */
	Stream<Set<String>> getAnagramPairs();

	/**
	 * Ingest a word.
	 *
	 * @param word
	 *            the word to ingest
	 * @return {@code true} if the word was ingested
	 */
	boolean ingest(String word);
}
