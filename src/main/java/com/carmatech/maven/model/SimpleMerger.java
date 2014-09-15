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

import static com.carmatech.maven.utils.MergeUtils.putAll;
import static com.carmatech.maven.utils.MergeUtils.toProperties;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;

public class SimpleMerger implements IMerger {
	@Override
	public PropertiesConfiguration merge(final List<File> sourceFiles) throws IOException {
		PropertiesConfiguration targetProperties = null;
		for (final File sourceFile : sourceFiles) {
			// Returned PropertiesConfiguration object, holding all values, is initialized with the first file to keep all comments.
			if (targetProperties == null) {
				targetProperties = toProperties(sourceFile);
			} else {
				putAll(targetProperties, toProperties(sourceFile));
			}
		}
		return targetProperties;
	}
}
