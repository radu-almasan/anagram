package com.radualmasan.anagram.output;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @author almasan.radu@gmail.com
 */
@FunctionalInterface
public interface Writer {

	/**
	 * Write the strategies results.
	 *
	 * @param anagramPairs
	 *            the anagram pair to write
	 */
	void write(Stream<Set<String>> anagramPairs);
}
