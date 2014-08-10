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
package com.carmatech.maven.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public final class MergeUtils {

	private MergeUtils() {
		// Pure utility class, do NOT instantiate.
	}

	public static PropertiesConfiguration toProperties(final File propertiesFile) throws IOException {
		try {
			return new PropertiesConfiguration(propertiesFile);
		} catch (ConfigurationException e) {
			throw new IOException("Unable to load properties file " + propertiesFile, e);
		}
	}

	public static void savePropertiesTo(final File targetFile, final PropertiesConfiguration properties, final String comment) throws IOException {
		properties.setHeader(comment);
		try {
			properties.save(targetFile);
		} catch (ConfigurationException e) {
			throw new IOException("Unable to save properties file " + targetFile);
		}
	}

	public static void putAll(PropertiesConfiguration target, PropertiesConfiguration source) {
		for (Iterator<String> it = source.getKeys(); it.hasNext();) {
			String key = it.next();
			Object property = source.getProperty(key);
			target.setProperty(key, property);
		}
	}

	public static <T> String generateComment(final Class<T> clazz) {
		return "File merged by properties-files-maven-plugin, using " + clazz.getSimpleName() + ", at:";
	}
}
