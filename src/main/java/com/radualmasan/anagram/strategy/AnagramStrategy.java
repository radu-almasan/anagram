package com.radualmasan.anagram.strategy;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author developer
 *
 */
public interface AnagramStrategy extends AutoCloseable {

    /**
     * @return a stream of anagram pairs.
     */
    Stream<Set<String>> getAnagramPairs();

    /**
     *
     * @param word
     *            the word who's characters to sort
     * @return the sorted words
     */
    default String getSortedChars(String word) {
        final char[] chars = word.toCharArray();
        Arrays.sort(chars);
        final String sortedWord = new String(chars);
        return sortedWord;
    }

    /**
     * Ingest a word.
     *
     * @param word
     *            the word to ingest
     * @return {@code true} if the word was ingested
     * @throws Exception
     *             if any problems occur
     */
    boolean ingest(String word) throws IOException, Exception;
}
