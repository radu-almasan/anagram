package com.radualmasan.anagram;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.isTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.radualmasan.anagram.input.Reader;
import com.radualmasan.anagram.input.ScannerReader;
import com.radualmasan.anagram.output.FileWriter;
import com.radualmasan.anagram.strategy.AnagramStrategy;
import com.radualmasan.anagram.strategy.DbAnagramStrategy;

/**
 * @author almasan.radu@gmail.com
 */
public class Anagram {

    /** Message for wrong program input. */
    private static final String EX_MSG_WRONG_INPUT = "Expected 2 paths, for input and output files";

    /**
     * @param args
     *            expected 2 paths, one for the input file and one for the
     *            output file
     */
    public static void main(String[] args) {
        new Anagram(args).run();
    }

    /** The input file path. */
    private final Path inputFilePath;
    /** The output file path. */
    private final Path outputFilePath;

    /**
     * Create a new instance.
     *
     * @param pathNames
     *            must be a double sized array, first element containing the
     *            input file path, and the second the output file path
     */
    public Anagram(String[] pathNames) {
        requireNonNull(pathNames, EX_MSG_WRONG_INPUT);
        isTrue(pathNames.length == 2, EX_MSG_WRONG_INPUT);

        inputFilePath = Paths.get(pathNames[0]);
        outputFilePath = Paths.get(pathNames[1]);
    }

    /**
     *
     * @return a new anagram strategy
     */
    AnagramStrategy newAnagramStrategy() {
        return new DbAnagramStrategy();
    }

    /**
     * @param inputFilePath
     *            the input file path
     * @return a new reader
     * @throws IOException
     *             if any IO problems occur
     */
    Reader newReader(Path inputFilePath) throws IOException {
        return new ScannerReader(inputFilePath);
    }

    /**
     * @param outputFilePath
     *            the output file path
     * @return a new writer
     */
    FileWriter newWriter(Path outputFilePath) {
        return new FileWriter(outputFilePath);
    }

    public void run() {
        try (final Reader reader = newReader(inputFilePath); AnagramStrategy anagramStrategy = newAnagramStrategy()) {
            while (reader.hasNext()) {
                anagramStrategy.ingest(reader.next());
            }
            newWriter(outputFilePath).write(anagramStrategy.getAnagramPairs());

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
