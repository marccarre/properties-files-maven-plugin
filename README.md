A Maven plugin to manipulate properties files in an easy way.
Features:
  - Merge multiple properties files into a single one.

*******************************************************************************
**Example**:


    <build>
        <plugins>
            <plugin>
                <groupId>com.carmatech.maven</groupId>
                <artifactId>properties-files-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>merge</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <operations>
                        <operation>
                            <targetFile>${basedir}/target/test-classes/poms/target_02.properties</targetFile>
                            <sourceFileSets>
                                <sourceFileSet>
                                    <directory>${basedir}/target/test-classes/poms</directory>
                                    <includes>
                                        <include>a.properties</include>
                                        <include>b.properties</include>
                                    </includes>
                                </sourceFileSet>
                                <sourceFileSet>
                                    <directory>${basedir}/target/test-classes/poms/env</directory>
                                    <includes>
                                        <include>**/*.properties</include>
                                    </includes>
                                </sourceFileSet>
                            </sourceFileSets>
                        </operation>
                    </operations>
                </configuration>
            </plugin>
        </plugins>
    </build>


*******************************************************************************
Copyright 2012-2013 Marc CARRE - Licensed under the Apache License, Version 2.0 (the "License");

