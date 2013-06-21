/*******************************************************************************
 * Copyright 2012-2013 Marc CARRE
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public final class MergerTestUtils {
	private MergerTestUtils() {
		// Pure utility class, do NOT instantiate.
	}

	public static void assertThatPropertiesAreSameAsSources(final File targetFile) throws IOException, FileNotFoundException {
		final Properties properties = new Properties();
		properties.load(new FileInputStream(targetFile));
		assertThat(properties.getProperty("FamilyName"), is("Skywalker"));
		assertThat(properties.getProperty("first_name"), is("Luke"));
		assertThat(properties.getProperty("Sabrolaser"), is("bLuE"));
		assertThat(properties.getProperty("planets"), is("endor, tatooine, naboo, coruscant, alderaan, kamino, yavin"));
		assertThat(properties.getProperty("children"), is(""));
	}

	public static List<File> getSourceFiles(final String... filePaths) {
		final List<File> sourceFiles = new LinkedList<File>();
		for (final String filePath : filePaths) {
			sourceFiles.add(new File(filePath));
		}
		return sourceFiles;
	}
}
