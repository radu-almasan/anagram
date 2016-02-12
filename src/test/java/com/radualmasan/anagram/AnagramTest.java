package com.radualmasan.anagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.runners.MockitoJUnitRunner;

import com.radualmasan.anagram.input.ScannerReader;
import com.radualmasan.anagram.output.FileWriter;
import com.radualmasan.anagram.strategy.AnagramStrategy;

/**
 * @author almasan.radu@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class AnagramTest {

    /** Input file path name. */
    private static final String INPUT_FILE_PATH_NAME = "C:\\in.txt";
    /** Output file path name. */
    private static final String OUTPUT_FILE_PATH_NAME = "C:\\out.txt";

    /** The tested anagram. */
    private Anagram anagram;
    /** Anagram pairs. */
    @Mock
    private Stream<Set<String>> anagramPairs;
    /** The anagram strategy. */
    @Mock
    private AnagramStrategy anagramStrategy;
    /** The reader. */
    @Mock
    private ScannerReader reader;
    /** The writer. */
    @Mock
    private FileWriter writer;

    /**
     * Set up test case.
     */
    @Before
    public void init() {
        anagram = new Anagram(new String[] { INPUT_FILE_PATH_NAME, OUTPUT_FILE_PATH_NAME }) {
            @Override
            AnagramStrategy newAnagramStrategy() {
                return anagramStrategy;
            }

            @Override
            ScannerReader newReader(Path inputFilePath) throws IOException {
                return reader;
            }

            @Override
            FileWriter newWriter(Path outputFilePath) {
                return writer;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEmptyInut() throws Exception {
        new Anagram(new String[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testNewNullInput() {
        new Anagram((String[]) null);
    }

    @Test
    public void testRun() throws Exception {
        final String[] readWords = { "from", "test", "form" };
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when(reader.next()).thenReturn(readWords[0], Arrays.copyOfRange(readWords, 1, readWords.length));

        when(anagramStrategy.getAnagramPairs()).thenReturn(anagramPairs);

        anagram.run();

        final ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(anagramStrategy, times(3)).ingest(stringCaptor.capture());

        final List<String> ingestedWords = stringCaptor.getAllValues();
        assertEquals(3, ingestedWords.size());
        assertThat(ingestedWords.toArray(new String[ingestedWords.size()]), new ArrayEquals(readWords));

        verify(writer).write(anagramPairs);
    }
}
