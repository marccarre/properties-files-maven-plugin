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
import java.util.List;

import static com.carmatech.maven.utils.MergeUtils.*;
import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleMerger implements IMerger {

    private final Log logger;

    public SimpleMerger(final Log logger) {
        checkNotNull(logger, "Logger must NOT be null.");
        this.logger = logger;
    }

    @Override
    public void mergeTo(final File targetFile, final List<File> sourceFiles) throws IOException {
        logger.info("Merging [" + targetFile.getName() + "] using " + SimpleMerger.class.getSimpleName() + "...");
        final PropertiesConfiguration allProperties = mergePropertiesFrom(sourceFiles);
        savePropertiesTo(targetFile, allProperties, generateComment(SimpleMerger.class));
    }

    private PropertiesConfiguration mergePropertiesFrom(final List<File> sourceFiles) throws IOException {
        PropertiesConfiguration targetProperties = null;
        for (final File sourceFile : sourceFiles) {
            if (targetProperties == null) {
                targetProperties = toProperties(sourceFile);
            } else {
                putAll(targetProperties, toProperties(sourceFile));
            }
        }
        return targetProperties;
    }
}
