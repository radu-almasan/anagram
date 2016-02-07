package com.radualmasan.anagram.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Map based
 *
 * @author almasan.radu@gmail.com
 */
public class MapBasedAnagramStrategy implements AnagramStrategy {

    /** Computed and sorted data. */
    private final Map<Integer, Map<Integer, Map<String, Set<String>>>> data = new HashMap<>();

    @Override
    public Stream<Set<String>> getAnagramPairs() {
        return data.values().stream() //
                .flatMap(byByteSum -> byByteSum.values().stream()) //
                .flatMap(bySortedChars -> bySortedChars.values().stream()) //
                .filter(anagrams -> anagrams.size() > 1);
    }

    @Override
    public boolean ingest(String word) {
        final int length = word.length();
        if (length < 2) {
            return false;
        }

        final Map<Integer, Map<String, Set<String>>> dataByLength = data.computeIfAbsent(length, i -> new HashMap<>());

        final byte[] bytes = word.getBytes();
        final int sum = IntStream.range(0, length).map(i -> bytes[i]).parallel().reduce(0, (a, b) -> a + b);
        final Map<String, Set<String>> dataByCharSum = dataByLength.computeIfAbsent(sum, k -> new HashMap<>());

        final char[] chars = word.toCharArray();
        Arrays.sort(chars);
        final String sortedWord = new String(chars);

        dataByCharSum.compute(sortedWord, (k, v) -> {
            v = v != null ? v : new LinkedHashSet<>();
            v.add(word);
            return v;
        });

        return true;
    }
}
