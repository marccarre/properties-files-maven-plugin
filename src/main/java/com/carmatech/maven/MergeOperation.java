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
package com.carmatech.maven;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import com.carmatech.maven.model.IMerger;

public class MergeOperation {

	private static final String COMMA = ",";

	@Parameter(required = true)
	private File targetFile;

	@Parameter(required = true)
	private FileSet[] sourceFileSets;

	private volatile List<File> sourceFiles;

	private final Object lock = new Object();

	public void merge(final IMerger merger) throws IOException {
		final List<File> sources = getSourceFiles();
		final File target = getTargetFile();
		merger.mergeTo(target, sources);
	}

	public File getTargetFile() {
		checkNotNull(targetFile, "A valid target file path must be provided.");
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
}
