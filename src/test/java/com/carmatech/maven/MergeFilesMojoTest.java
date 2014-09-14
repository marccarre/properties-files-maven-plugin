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
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class MergeFilesMojoTest extends AbstractMojoTestCase {
	public void testMergeTwoPropertiesFilesWithNoIntersection() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", localFile("target/test-classes/poms/testing_pom_01.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_01.properties");
		assertThat(actualProperties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(actualProperties.get("key1").toString(), is("value1")); // from a.properties
		assertThat(actualProperties.get("key2").toString(), is("value2")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersection() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", localFile("target/test-classes/poms/testing_pom_02.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_02.properties");
		assertThat(actualProperties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(actualProperties.get("key1").toString(), is("valueForEnv")); // from a.properties
		assertThat(actualProperties.get("key2").toString(), is("valueForRegion")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersectionUsingSimpleMerger() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", localFile("target/test-classes/poms/testing_pom_03.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/target_03.properties");
		assertThat(actualProperties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(actualProperties.get("key1").toString(), is("valueForEnv")); // from a.properties
		assertThat(actualProperties.get("key2").toString(), is("valueForRegion")); // from b.properties
	}

	public void testMergePropertiesFilesToFileWithMissingParentFolder() throws Exception {
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", localFile("src/test/resources/poms/testing_pom_non_existing_target_folder.xml"));
		assertNotNull(mojo);

		mojo.execute();

		Properties actualProperties = loadPropertiesFrom("target/test-classes/poms/non_existing_target_folder/target.properties");
		assertThat(actualProperties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(actualProperties.get("key1").toString(), is("value1")); // from a.properties
		assertThat(actualProperties.get("key2").toString(), is("value2")); // from b.properties
	}

	private File localFile(final String path) {
		return new File(getBasedir(), path);
	}

	private Properties loadPropertiesFrom(final String path) throws IOException, FileNotFoundException {
		final File propertiesFile = localFile(path);
		final Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		return properties;
	}
}
