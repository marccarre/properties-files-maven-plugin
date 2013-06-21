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
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.maven.plugin.logging.Log;

public class ParallelMerger implements IMerger {
	private final Log logger;
	private final ExecutorService threadPool;

	public ParallelMerger(final Log logger, final ExecutorService threadPool) {
		checkNotNull(logger, "Logger must NOT be null.");
		checkNotNull(logger, "Thread pool must NOT be null.");
		this.logger = logger;
		this.threadPool = threadPool;
	}

	@Override
	public void mergeTo(final File targetFile, final List<File> sourceFiles) throws IOException, FileNotFoundException {
		try {
			logger.info("Merging [" + targetFile.getName() + "] using " + ParallelMerger.class.getSimpleName() + "...");
			final Properties allProperties = mergePropertiesFrom(sourceFiles);
			savePropertiesTo(targetFile, allProperties, generateComment(ParallelMerger.class));
		} catch (InterruptedException e) {
			logger.warn("Parallel merger was interrupted: " + e.getMessage() + ". Falling back to simple merger...", e);
			new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);
		} catch (ExecutionException e) {
			logger.warn("Parallel merger failed to complete: " + e.getMessage() + ". Falling back to simple merger...", e);
			new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);
		}
	}

	private Properties mergePropertiesFrom(final List<File> sourceFiles) throws IOException, InterruptedException, ExecutionException {
		final List<Future<Properties>> futures = parallelToProperties(sourceFiles);
		return waitForCompletionAndMergeProperties(futures);
	}

	private List<Future<Properties>> parallelToProperties(final List<File> sourceFiles) {
		final List<Future<Properties>> futures = new LinkedList<Future<Properties>>();
		for (final File sourceFile : sourceFiles) {
			futures.add(threadPool.submit(new Callable<Properties>() {
				@Override
				public Properties call() throws Exception {
					return toProperties(sourceFile);
				}
			}));
		}
		return futures;
	}

	private Properties waitForCompletionAndMergeProperties(final List<Future<Properties>> futures) throws InterruptedException, ExecutionException {
		final Properties targetProperties = new Properties();
		for (final Future<Properties> future : futures) {
			targetProperties.putAll(future.get());
		}
		return targetProperties;
	}
}
