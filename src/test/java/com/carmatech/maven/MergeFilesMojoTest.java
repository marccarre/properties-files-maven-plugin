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
import java.util.Properties;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class MergeFilesMojoTest extends AbstractMojoTestCase {
	public void testMergeTwoPropertiesFilesWithNoIntersection() throws Exception {
		File pom = new File(getBasedir(), "/target/test-classes/poms/testing_pom_01.xml");
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", pom);
		assertNotNull(mojo);

		mojo.execute();
		File mergedPropertiesFile = new File(getBasedir(), "/target/test-classes/poms/target_01.properties");
		Properties properties = new Properties();
		properties.load(new FileInputStream(mergedPropertiesFile));

		assertThat(properties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(properties.get("key1").toString(), is("value1")); // from a.properties
		assertThat(properties.get("key2").toString(), is("value2")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersection() throws Exception {
		File pom = new File(getBasedir(), "/target/test-classes/poms/testing_pom_02.xml");
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", pom);
		assertNotNull(mojo);

		mojo.execute();
		File mergedPropertiesFile = new File(getBasedir(), "/target/test-classes/poms/target_02.properties");
		Properties properties = new Properties();
		properties.load(new FileInputStream(mergedPropertiesFile));

		assertThat(properties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(properties.get("key1").toString(), is("valueForEnv")); // from a.properties
		assertThat(properties.get("key2").toString(), is("valueForRegion")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersectionUsingSimpleMerger() throws Exception {
		File pom = new File(getBasedir(), "/target/test-classes/poms/testing_pom_03.xml");
		MergeFilesMojo mojo = (MergeFilesMojo) lookupMojo("merge", pom);
		assertNotNull(mojo);

		mojo.execute();
		File mergedPropertiesFile = new File(getBasedir(), "/target/test-classes/poms/target_03.properties");
		Properties properties = new Properties();
		properties.load(new FileInputStream(mergedPropertiesFile));

		assertThat(properties.get("key0").toString(), is("value0")); // from a.properties
		assertThat(properties.get("key1").toString(), is("valueForEnv")); // from a.properties
		assertThat(properties.get("key2").toString(), is("valueForRegion")); // from b.properties
	}
}
