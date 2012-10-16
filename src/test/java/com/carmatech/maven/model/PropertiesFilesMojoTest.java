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
import java.util.Properties;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.carmatech.maven.PropertiesFilesMojo;

public class PropertiesFilesMojoTest extends AbstractMojoTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMergeTwoPropertiesFilesWithNoIntersection() throws Exception {
		File pom = new File(getBasedir(), "/target/test-classes/unit/poms/testing_pom_01.xml");
		PropertiesFilesMojo mojo = (PropertiesFilesMojo) lookupMojo("merge", pom);
		assertNotNull(mojo);

		mojo.execute();
		File mergedPropertiesFile = new File(getBasedir(), "/target/test-classes/unit/props/target.properties");
		Properties properties = new Properties();
		properties.load(new FileInputStream(mergedPropertiesFile));
		assertEquals("value0", properties.get("key0")); // from a.properties
		assertEquals("value1", properties.get("key1")); // from a.properties
		assertEquals("value2", properties.get("key2")); // from b.properties
	}

	public void testMergeFourPropertiesFilesWithIntersection() throws Exception {
		File pom = new File(getBasedir(), "/target/test-classes/unit/poms/testing_pom_02.xml");
		PropertiesFilesMojo mojo = (PropertiesFilesMojo) lookupMojo("merge", pom);
		assertNotNull(mojo);

		mojo.execute();
		File mergedPropertiesFile = new File(getBasedir(), "/target/test-classes/unit/props/target.properties");
		Properties properties = new Properties();
		properties.load(new FileInputStream(mergedPropertiesFile));
		assertEquals("value0", properties.get("key0")); // from a.properties
		assertEquals("valueForEnv", properties.get("key1")); // from a.properties
		assertEquals("valueForRegion", properties.get("key2")); // from b.properties
	}
}
