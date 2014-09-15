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
package com.carmatech.maven;

import static com.carmatech.maven.utils.MergeUtils.generateComment;
import static com.carmatech.maven.utils.MergeUtils.savePropertiesTo;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import com.carmatech.maven.model.IMerger;
import com.google.common.io.Files;

public class MergeOperation {
	private static final String COMMA = ",";
	private static final String PATTERN_FOR_VARIABLE = "\\$\\{(.+)\\}";
	private static final int INDEX_GROUP_VARIABLE_NAME = 1;

	@Parameter(required = false)
	protected boolean filtering = false;

	@Parameter(required = true)
	protected File targetFile;

	@Parameter(required = true)
	protected FileSet[] sourceFileSets;

	private volatile List<File> sourceFiles;

	private final Object lock = new Object();

	private MavenProject project;
	private Log logger;

	public void setProject(final MavenProject project) {
		this.project = project;
	}

	public void setLogger(final Log logger) {
		this.logger = logger;
	}

	public void merge(final IMerger merger) throws IOException {
		logger.info("---");

		final List<File> sources = getSourceFiles();
		final File targetFile = getTargetFile();

		logger.info("Merging content of [" + targetFile.getName() + "] using " + merger.getClass().getSimpleName() + "...");
		final PropertiesConfiguration allProperties = merger.merge(sources);

		if (filtering) {
			logger.info("Filtering content of [" + targetFile.getName() + "]...");
			filter(allProperties);
		}

		logger.info("Saving [" + targetFile.getName() + "]...");
		savePropertiesTo(targetFile, allProperties, generateComment(merger.getClass()));
	}

	public File getTargetFile() throws IOException {
		checkNotNull(targetFile, "A valid target file path must be provided.");
		Files.createParentDirs(targetFile);
		return targetFile;
	}

	public List<File> getSourceFiles() throws IOException {
		checkNotNull(sourceFileSets, "Valid file sets must be provided: file sets were null.");
		checkArgument(sourceFileSets.length != 0, "Valid file sets must be provided: file sets were empty.");
		return getOrFindSourceFiles();
	}

	private List<File> getOrFindSourceFiles() throws IOException {
		if (sourceFiles == null) {
			synchronized (lock) {
				if (sourceFiles == null) {
					sourceFiles = findAllFiles(sourceFileSets);
				}
			}
		}
		return sourceFiles;
	}

	private List<File> findAllFiles(final FileSet[] fileSets) throws IOException {
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

	private void filter(final PropertiesConfiguration properties) {
		final Matcher variableMatcher = Pattern.compile(PATTERN_FOR_VARIABLE).matcher("");

		for (final Iterator<String> keys = properties.getKeys(); keys.hasNext();) {
			final String name = keys.next();
			final Object value = properties.getProperty(name);

			if (isVariable(value, variableMatcher)) {
				final String variableName = variableMatcher.group(INDEX_GROUP_VARIABLE_NAME);
				overrideIfNotNull(properties, name, variableName, "System", System.getProperty(variableName));
				overrideIfNotNull(properties, name, variableName, "Maven", project.getProperties().getProperty(variableName));
				overrideIfNotNull(properties, name, variableName, "merged", properties.getProperty(variableName));
			}
		}
	}

	private boolean isVariable(final Object value, final Matcher matcher) {
		return (value != null) && (matcher.reset(value.toString()).matches());
	}

	private void overrideIfNotNull(final PropertiesConfiguration properties, final String name, final String variableName, String source, final Object newValue) {
		if (newValue != null) {
			logger.info("Property [" + name + "] parameterized with [" + variableName + "] was set to [" + newValue + "] using " + source + " properties.");
			properties.setProperty(name, newValue);
		}
	}
}
