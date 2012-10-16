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
package com.carmatech.maven.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import com.carmatech.maven.utils.StringUtils;

public class Operation {

	private static final String COMMA = ",";

	@Parameter(required = true)
	private File targetFile;

	@Parameter(required = true)
	private FileSet[] fileSets;

	public void generateTargetFile() throws IOException {
		validateFileSets();
		validateTargetFile();
		File defaultDir = new File(System.getProperty("user.dir"));

		Properties properties = new Properties();

		for (FileSet fileSet : fileSets) {
			List<File> files = toFiles(fileSet, defaultDir);

			for (File file : files) {
				properties.putAll(toProperties(file));
			}
		}

		properties.store(new FileOutputStream(targetFile), null);
	}

	private void validateTargetFile() {
		if (targetFile == null) {
			throw new IllegalArgumentException("A valid target file path must be provided.");
		}
	}

	private void validateFileSets() {
		if ((fileSets == null) || (fileSets.length == 0)) {
			throw new IllegalArgumentException("Valid file sets must be provided.");
		}
	}

	private List<File> toFiles(FileSet fileSet, File defaultDirectory) throws IOException {
		File directory = new File(fileSet.getDirectory());
		String includes = StringUtils.join(fileSet.getIncludes(), COMMA);
		String excludes = StringUtils.join(fileSet.getExcludes(), COMMA);

		List<File> files = FileUtils.getFiles(directory, includes, excludes);
		return files;
	}

	private Properties toProperties(File file) throws IOException {
		Properties properties = new Properties();
		FileInputStream in = new FileInputStream(file);
		properties.load(in);
		in.close();
		return properties;
	}
}
