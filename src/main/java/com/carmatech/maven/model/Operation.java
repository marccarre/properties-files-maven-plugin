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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

public class Operation {

	private static final String COMMA = ",";

	@Parameter(required = true)
	private File targetFile;

	@Parameter(required = true)
	private FileSet[] fileSets;

	public void mergePropertiesToTargetFile() throws IOException {
		checkNotNull(fileSets, "Valid file sets must be provided: file sets were null.");
		checkArgument(fileSets.length != 0, "Valid file sets must be provided: file sets were empty.");
		checkNotNull(targetFile, "A valid target file path must be provided.");

		final List<File> files = findAllFiles();
		final Properties allProperties = mergePropertiesFrom(files);
		saveToTargetFile(allProperties);
	}

	private List<File> findAllFiles() throws IOException {
		final List<File> files = new LinkedList<File>();
		for (final FileSet fileSet : fileSets) {
			files.addAll(toFiles(fileSet));
		}
		return files;
	}

	private List<File> toFiles(final FileSet fileSet) throws IOException {
		final File directory = new File(fileSet.getDirectory());
		final String includes = StringUtils.join(fileSet.getIncludes(), COMMA);
		final String excludes = StringUtils.join(fileSet.getExcludes(), COMMA);

		return FileUtils.getFiles(directory, includes, excludes);
	}

	private Properties mergePropertiesFrom(final List<File> files) throws IOException {
		final Properties properties = new Properties();
		for (final File file : files) {
			properties.putAll(toProperties(file));
		}
		return properties;
	}

	private Properties toProperties(final File propertiesFile) throws IOException {
		final Properties properties = new Properties();
		final InputStream in = new BufferedInputStream(new FileInputStream(propertiesFile));
		properties.load(in);
		in.close();
		return properties;
	}

	private void saveToTargetFile(final Properties properties) throws FileNotFoundException, IOException {
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
		properties.store(out, null);
		out.flush();
		out.close();
	}
}
