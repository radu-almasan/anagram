package com.radualmasan.anagram.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * @author almasan.radu@gmail.com
 */
public class ScannerReaderTest {

	/** Number of words in the larger.in file. */
	private static final int LARGER_IN_WORD_COUNT = 674;
	/** Path for the larger.in file. */
	private static final String PATH_LARGER_IN = "larger.in";
	/** Path for the simple.in file. */
	private static final String PATH_SIMPLE_IN = "simple.in";
	/** Number of words in the simple.in file. */
	private static final int SIMPLE_IN_WORD_COUNT = 9;

	@Test
	public void testCountLargerFileWords() throws Exception {
		testCountWords(PATH_LARGER_IN, LARGER_IN_WORD_COUNT);
	}

	@Test
	public void testCountSimpleFileWords() throws Exception {
		testCountWords(PATH_SIMPLE_IN, SIMPLE_IN_WORD_COUNT);
	}

	/**
	 * Test that the file at the given path as the given number of words.
	 * 
	 * @param filePathName
	 *            the file path
	 * @param wordCount
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private void testCountWords(String filePathName, int wordCount) throws URISyntaxException, IOException {
		URL inFileUrl = getClass().getClassLoader().getResource(filePathName);
		Path inFilePath = Paths.get(inFileUrl.toURI());

		List<String> words = new LinkedList<>();
		try (ScannerReader reader = new ScannerReader(inFilePath)) {
			reader.forEachRemaining(words::add);
		}

		assertEquals(wordCount, words.size());
	}
}
