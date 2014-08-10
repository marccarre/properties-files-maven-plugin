/*******************************************************************************
 * Copyright 2012-2014 Marc CARRE
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
package com.carmatech.maven.model;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.hamcrest.Matcher;

import com.google.common.collect.Lists;

public final class MergerTestUtils {

	private MergerTestUtils() {
		// Pure utility class, do NOT instantiate.
	}

	public static void assertThatPropertiesAreSameAsSources(final File targetFile) throws Exception {
		final PropertiesConfiguration properties = new PropertiesConfiguration(targetFile);
		assertThat(properties.getString("FamilyName"), is("Skywalker"));
		assertThat(properties.getString("first_name"), is("Luke"));
		assertThat(properties.getString("Sabrolaser"), is("bLuE"));
		assertThat(properties.getProperty("planets"), isObject(asList("endor", "tatooine", "naboo", "coruscant", "alderaan", "kamino", "yavin")));
		assertThat(properties.getProperty("children"), is(nullValue()));
	}

	/**
	 * Trick to have the matcher comparing objects, instead of trying to compare more specific types.
	 */
	private static Matcher<Object> isObject(final Object value) {
		return is(value);
	}

	public static List<File> getSourceFiles(final String... filePaths) {
		final List<File> sourceFiles = Lists.newArrayListWithExpectedSize(filePaths.length);
		for (final String filePath : filePaths) {
			sourceFiles.add(new File(filePath));
		}
		return sourceFiles;
	}
}
