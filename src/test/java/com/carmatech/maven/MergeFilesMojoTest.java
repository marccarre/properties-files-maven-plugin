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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MergeFilesMojoTest extends AbstractMojoTestCase {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	public void testMergeTwoPropertiesFilesWithNoIntersection() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", getTestFile("src/test/resources/poms/testing_pom_01.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_01.properties");
		assertThat(actualProperties.getProperty("key0"), is("value0")); // from a.properties
		assertThat(actualProperties.getProperty("key1"), is("value1")); // from a.properties
		assertThat(actualProperties.getProperty("key2"), is("value2")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersection() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", getTestFile("src/test/resources/poms/testing_pom_02.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_02.properties");
		assertThat(actualProperties.getProperty("key0"), is("value0")); // from a.properties
		assertThat(actualProperties.getProperty("key1"), is("valueForEnv")); // from a.properties
		assertThat(actualProperties.getProperty("key2"), is("valueForRegion")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersectionUsingSimpleMerger() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", getTestFile("src/test/resources/poms/testing_pom_03.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_03.properties");
		assertThat(actualProperties.getProperty("key0"), is("value0")); // from a.properties
		assertThat(actualProperties.getProperty("key1"), is("valueForEnv")); // from a.properties
		assertThat(actualProperties.getProperty("key2"), is("valueForRegion")); // from b.properties
	}

	public void testMergePropertiesFilesToFileWithMissingParentFolder() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", getTestFile("src/test/resources/poms/testing_pom_non_existing_target_folder.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/non_existing_target_folder/target.properties");
		assertThat(actualProperties.getProperty("key0"), is("value0")); // from a.properties
		assertThat(actualProperties.getProperty("key1"), is("value1")); // from a.properties
		assertThat(actualProperties.getProperty("key2"), is("value2")); // from b.properties
	}

	// TODO: Make this test or something equivalent pass when building. Problem: MavenProject does not get injected properly.
//	public void testMergePropertiesFilesWithResourceFiltering() throws Exception {
//		File pom = getTestFile("src/test/resources/poms/testing_pom_resource_filtering.xml");
//		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", pom);
//		assertNotNull(mojo);
//
//		mojo.execute();
//
//		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_resource_filtering.properties");
//		assertThat(actualProperties.getProperty("key0"), is("value0")); // from a.properties
//		assertThat(actualProperties.getProperty("key1"), is("value1")); // from a.properties
//		assertThat(actualProperties.getProperty("key2"), is("value2")); // from b.properties
//		// from to_filter.properties
//		assertThat(actualProperties.getProperty("injected_by_resource_filtering"), is("Successfully injected from static Maven Property!"));
//	}

	public void testMergePropertiesFilesToFileWithMissingInputFile() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", getTestFile("src/test/resources/poms/testing_pom_non_existing_input_file.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_non_existing_input_file.properties");
		assertThat(actualProperties.getProperty("key0"), is("value0")); // from a.properties
		assertThat(actualProperties.getProperty("key1"), is("value1")); // from a.properties
		assertThat(actualProperties.getProperty("key2"), is("value2")); // from b.properties
	}

	public void testMergePropertiesFilesToFileWithMissingInputFileAndErrorOnMissingFile() throws Exception {
		exception.expect(MojoExecutionException.class);
		exception.expectMessage(containsString("non_existing.properties"));

		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", getTestFile("src/test/resources/poms/testing_pom_non_existing_input_file_failure.xml"));
		assertNotNull(mojo);
		mojo.execute();
	}

	private Properties loadPropertiesFrom(final String path) throws IOException, FileNotFoundException {
		final File propertiesFile = getTestFile(path);
		final Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		return properties;
	}
}
