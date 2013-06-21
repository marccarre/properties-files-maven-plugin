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

import org.apache.maven.plugin.logging.Log;

import com.carmatech.maven.utils.ThreadingUtils;

public class MergerFactory {
	public static IMerger getMerger(final Log logger, final int numTotalSourceFiles) {
		switch (numTotalSourceFiles) {
		case 1:
			logger.info("Creating " + SimpleMerger.class.getSimpleName() + "...");
			return SimpleMerger.getInstance();
		default:
			logger.info("Creating " + ParallelMerger.class.getSimpleName() + "...");
			return new ParallelMerger(logger, ThreadingUtils.createThreadPool(logger, numTotalSourceFiles));
		}
	}
}
