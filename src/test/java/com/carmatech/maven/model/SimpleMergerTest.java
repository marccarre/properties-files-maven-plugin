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

import static com.carmatech.maven.model.MergerTestUtils.assertThatPropertiesAreSameAsSources;
import static com.carmatech.maven.model.MergerTestUtils.getSourceFiles;
import static com.carmatech.maven.model.MergerTestUtils.readExpectedProperties;
import static com.carmatech.maven.model.MergerTestUtils.readProperties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

public class SimpleMergerTest {
	private final Log logger = new SystemStreamLog();

	@Test
	public void mergePropertiesInConsistentWay() throws Exception {
		File targetFile = new File("target/test-classes/unit/simple_merger_1_" + UUID.randomUUID().toString() + "_.properties");
		List<File> sourceFiles = getSourceFiles("src/test/resources/unit/all_formats.properties");

		new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);

		assertThatPropertiesAreSameAsSources(targetFile);
	}

	@Test
	public void targetPropertiesFileRetainsItsNiceFormatting() throws Exception {
		File targetFile = new File("target/test-classes/unit/simple_merger_2_" + UUID.randomUUID().toString() + "_.properties");
		List<File> sourceFiles = getSourceFiles("src/test/resources/unit/nicely_formatted.properties", "src/test/resources/unit/all_formats.properties");

		new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);

		String expectedProperties = readExpectedProperties("src/test/resources/unit/nicely_formatted_merged.properties", SimpleMerger.class);
		assertThat(readProperties(targetFile), is(expectedProperties));
	}
}
