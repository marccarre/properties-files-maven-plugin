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

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.carmatech.maven.utils.MergeUtils.*;
import static com.google.common.base.Preconditions.checkNotNull;

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
    public void mergeTo(final File targetFile, final List<File> sourceFiles) throws IOException {
        try {
            logger.info("Merging [" + targetFile.getName() + "] using " + ParallelMerger.class.getSimpleName() + "...");
            final PropertiesConfiguration allProperties = mergePropertiesFrom(sourceFiles);
            savePropertiesTo(targetFile, allProperties, generateComment(ParallelMerger.class));
        } catch (InterruptedException e) {
            logger.warn("Parallel merger was interrupted: " + e.getMessage() + ". Falling back to simple merger...", e);
            new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);
        } catch (ExecutionException e) {
            logger.warn("Parallel merger failed to complete: " + e.getMessage() + ". Falling back to simple merger...", e);
            new SimpleMerger(logger).mergeTo(targetFile, sourceFiles);
        }
    }

    private PropertiesConfiguration mergePropertiesFrom(final List<File> sourceFiles) throws IOException, InterruptedException, ExecutionException {
        final List<Future<PropertiesConfiguration>> futures = parallelToProperties(sourceFiles);
        return waitForCompletionAndMergeProperties(futures);
    }

    private List<Future<PropertiesConfiguration>> parallelToProperties(final List<File> sourceFiles) {
        final List<Future<PropertiesConfiguration>> futures = new LinkedList<Future<PropertiesConfiguration>>();
        for (final File sourceFile : sourceFiles) {
            futures.add(threadPool.submit(new Callable<PropertiesConfiguration>() {
                @Override
                public PropertiesConfiguration call() throws Exception {
                    return toProperties(sourceFile);
                }
            }));
        }
        return futures;
    }

    private PropertiesConfiguration waitForCompletionAndMergeProperties(final List<Future<PropertiesConfiguration>> futures) throws InterruptedException, ExecutionException {
        PropertiesConfiguration targetProperties = null;
        for (final Future<PropertiesConfiguration> future : futures) {
            if (targetProperties == null) {
                targetProperties = future.get();
            } else {
                putAll(targetProperties, future.get());
            }
        }
        return targetProperties;
    }
}
