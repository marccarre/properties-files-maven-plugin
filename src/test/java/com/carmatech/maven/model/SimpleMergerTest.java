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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

import com.google.common.io.Files;

public class SimpleMergerTest {

	@Test
	public void mergePropertiesInConsistentWay() throws Exception {
		File targetFile = new File("target/test-classes/unit/simple_merger.properties");
		List<File> sourceFiles = getSourceFiles("src/test/resources/unit/all_formats.properties");

		Log logger = new SystemStreamLog();
		new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);

		assertThatPropertiesAreSameAsSources(targetFile);
	}

	@Test
	public void targetPropertiesFileRetainsItsNiceFormatting() throws Exception {
		File targetFile = new File("target/test-classes/unit/simple_merger.properties");
		List<File> sourceFiles = getSourceFiles("src/test/resources/unit/nicely_formatted.properties", "src/test/resources/unit/all_formats.properties");

		Log logger = new SystemStreamLog();
		new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);

		String expectedProperties = Files.toString(new File("src/test/resources/unit/nicely_formatted_merged.properties"), Charset.forName("UTF-8"));
		expectedProperties = expectedProperties.replace("${merger}", SimpleMerger.class.getSimpleName());
		assertThat(Files.toString(targetFile, Charset.defaultCharset()), equalTo(expectedProperties));
	}
}
