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
package com.carmatech.maven;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.carmatech.maven.model.Operation;

///**
// * Merge properties files
// * 
// * @author <a href="mailto:carre.marc@gmail.com">Marc CARRE</a>
// * @goal merge
// * @requiresProject
// */
@Mojo(name = "merge", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true, requiresProject = true)
// @Execute(goal = "merge", phase = LifecyclePhase.PROCESS_RESOURCES, lifecycle = "merge-default")
public class PropertiesFilesMojo extends AbstractMojo /* implements Contextualizable */{
	// private PlexusContainer plexusContainer;
	// /**
	// * @parameter default-value="${project}"
	// * @required
	// * @readonly
	// */
	// private MavenProject project;
	// @Component
	// protected MavenProject project;
	// @Parameter(defaultValue = "${project}", required = true, readonly = true)
	// protected MavenProject project;
	//
	// @Parameter(readonly = true, defaultValue = "${basedir}")
	// private File basedir;

	@Parameter(required = true)
	private Operation[] operations;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		validateOperations();

		for (Operation op : operations) {
			try {
				op.generateTargetFile();
			} catch (IOException e) {
				throw new MojoExecutionException(e.getMessage(), e);
			}
		}
	}

	private void validateOperations() throws MojoExecutionException {
		if ((operations == null) || (operations.length == 0)) {
			throw new MojoExecutionException("An operation should be defined for maven-properties-files-plugin to work.");
		}
	}
	//
	// @Override
	// public void contextualize(Context context) throws ContextException {
	// plexusContainer = (PlexusContainer) context.get(PlexusConstants.PLEXUS_KEY);
	// }
}
