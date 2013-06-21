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
package com.carmatech.maven.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.logging.Log;

public final class ThreadingUtils {
	private ThreadingUtils() {
		// Pure utility class, do NOT instantiate.
	}

	public static ExecutorService createThreadPool(final Log logger, final int totalNumFiles) {
		final int degreeOfParallelism = Math.max(1, Math.min(Runtime.getRuntime().availableProcessors(), totalNumFiles));
		final ExecutorService threadPool = Executors.newFixedThreadPool(degreeOfParallelism);
		logger.info("Created thread pool of size [" + degreeOfParallelism + "] in order to merge files in parallel.");
		return threadPool;
	}
}
