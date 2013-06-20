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

import static com.carmatech.maven.utils.MergeUtils.generateComment;
import static com.carmatech.maven.utils.MergeUtils.savePropertiesTo;
import static com.carmatech.maven.utils.MergeUtils.toProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SimpleMerger implements IMerger {
	private static IMerger INSTANCE = new SimpleMerger();

	public static IMerger getInstance() {
		return INSTANCE;
	}

	@Override
	public void mergeTo(final File targetFile, final List<File> sourceFiles) throws IOException, FileNotFoundException {
		final Properties allProperties = mergePropertiesFrom(sourceFiles);
		savePropertiesTo(targetFile, allProperties, generateComment(SimpleMerger.class));
	}

	private Properties mergePropertiesFrom(final List<File> sourceFiles) throws IOException {
		final Properties targetProperties = new Properties();
		for (final File sourceFile : sourceFiles) {
			targetProperties.putAll(toProperties(sourceFile));
		}
		return targetProperties;
	}
}
