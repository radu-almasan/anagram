package com.radualmasan.anagram.input;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * @author almasan.radu@gmail.com
 */
public class ScannerReader implements Reader {

	/** The pattern to search for splitting the words. */
	private static final String PATTERN = "[ ,\\.;:]+";

	/** The scanner. */
	private final Scanner scanner;

	/**
	 * Create a new instance.
	 *
	 * @throws IOException
	 *             if IO problems occur
	 */
	public ScannerReader(Path inputFilePath) throws IOException {
		scanner = new Scanner(inputFilePath);
		scanner.useDelimiter(PATTERN);
	}

	@Override
	public void close() {
		scanner.close();
	}

	@Override
	public boolean hasNext() {
		return scanner.hasNext();
	}

	@Override
	public String next() {
		return scanner.next();
	}
}
