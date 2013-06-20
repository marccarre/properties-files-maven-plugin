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
package com.carmatech.maven.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public final class MergeUtils {
	private MergeUtils() {
		// Pure utility class, do NOT instantiate.
	}

	public static Properties toProperties(final File propertiesFile) throws IOException {
		final Properties properties = new Properties();
		final InputStream in = new BufferedInputStream(new FileInputStream(propertiesFile));
		properties.load(in);
		in.close();
		return properties;
	}

	public static void savePropertiesTo(final File targetFile, final Properties properties, final String comment) throws FileNotFoundException, IOException {
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
		properties.store(out, comment);
		out.flush();
		out.close();
	}

	public static <T> String generateComment(final Class<T> clazz) {
		return "File merged by properties-files-maven-plugin, using " + clazz.getSimpleName() + ", at:";
	}
}
