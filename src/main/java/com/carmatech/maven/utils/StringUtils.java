/*******************************************************************************
 * Copyright 2012 Marc CARRE
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.carmatech.maven.utils;

import java.util.Iterator;

public class StringUtils {
	private static final int BUFFER_MIN_SIZE = 256;
	private static final String EMPTY = "";

	public static String join(Iterable<?> iterable, String separator) {
		if (iterable == null) {
			return null;
		}
		return join(iterable.iterator(), separator);
	}

	private static String join(Iterator<?> iterator, String separator) {
		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return toString(first);
		}

		// two or more elements
		StringBuilder buffer = new StringBuilder(BUFFER_MIN_SIZE); // Java default is 16, probably too small
		if (first != null) {
			buffer.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buffer.append(separator);
			}
			Object next = iterator.next();
			if (next != null) {
				buffer.append(next);
			}
		}
		return buffer.toString();
	}

	public static String toString(Object obj) {
		return (obj == null) ? EMPTY : obj.toString();
	}
}
