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

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.carmatech.maven.model.IMerger;
import com.carmatech.maven.model.MergerFactory;

/**
 * Utilities to manipulate and process properties files.
 * 
 * @author Marc CARRE
 */
@Mojo(name = "merge", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true, requiresProject = true)
public class MergeFilesMojo extends AbstractMojo {

	@Parameter(required = true)
	private MergeOperation[] operations;

	@Parameter(required = false)
	private boolean parallel = true;

	private Log logger;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		logger = getLog();
		validateOperations();

		try {
			final int numTotalSourceFiles = countSourceFiles(operations);

			for (final MergeOperation operation : operations) {
				final IMerger merger = MergerFactory.getMerger(logger, numTotalSourceFiles, parallel);
				operation.merge(merger);
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private int countSourceFiles(final MergeOperation[] operations) throws IOException {
		int count = 0;
		for (final MergeOperation operation : operations) {
			count += operation.getSourceFiles().size();
		}
		return count;
	}

	private void validateOperations() throws MojoExecutionException {
		if ((operations == null) || (operations.length == 0)) {
			throw new MojoExecutionException("An operation should be defined for maven-properties-files-plugin to work.");
		}
	}
}
