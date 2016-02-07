package com.radualmasan.anagram.input;

import java.io.Closeable;
import java.util.Iterator;

/**
 * @author almasan.radu@gmail.com
 */
public interface Reader extends Closeable, Iterator<String> {
}
