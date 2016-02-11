package com.radualmasan.anagram.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author almasan.radu@gmail.com
 */
public class FileWriter implements Writer {

	/** The output file path. */
	private final Path outputFilePath;

	/**
	 * Create a new instance.
	 *
	 * @param outputFilePath
	 *            the output file path
	 */
	public FileWriter(Path outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	@Override
	public void write(Stream<Set<String>> anagramPairs) {
		try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
			anagramPairs //
					.map(anagrams -> anagrams.parallelStream().reduce((a, b) -> a.concat(", ").concat(b))) //
					.map(Optional::get) //
					.forEach(line -> writeLine(writer, line));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Write an anagram pair too the file.
	 *
	 * @param writer
	 *            the writer
	 * @param line
	 *            the anagram pair
	 */
	private void writeLine(BufferedWriter writer, String line) {
		try {
			writer.write(line);
			writer.newLine();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
