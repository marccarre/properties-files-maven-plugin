[![Build Status](https://travis-ci.org/marccarre/properties-files-maven-plugin.png?branch=master)](https://travis-ci.org/marccarre/properties-files-maven-plugin) [![Coverage Status](https://coveralls.io/repos/marccarre/properties-files-maven-plugin/badge.png)](https://coveralls.io/r/marccarre/properties-files-maven-plugin)

A Maven plugin to manipulate properties files in an easy way.

Features:
  - Merge multiple properties files into a single one.
  - Merge multiple properties files into a single one, in parallel.
  - Resources filtering (using System, Maven and merged properties).
  - On missing input file, either skip and log warning or throw error.

Latest version (from either [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.carmatechnologies.maven) or [OSS Sonatype](https://oss.sonatype.org/#nexus-search;quick~com.carmatechnologies.maven)):

    
    <dependency>
      <groupId>com.carmatechnologies.maven</groupId>
      <artifactId>properties-files-maven-plugin</artifactId>
      <version>0.2</version>
    </dependency>


*******************************************************************************
**Example #1** - Merge using default merger (parallel merger):


    <build>
        <plugins>
            <plugin>
                <groupId>com.carmatechnologies.maven</groupId>
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
                            <targetFile>${basedir}/target/merged.properties</targetFile>
                            <sourceFileSets>
                                <sourceFileSet>
                                    <directory>${basedir}/src/main/resources</directory>
                                    <includes>
                                        <include>a.properties</include>
                                        <include>b.properties</include>
                                    </includes>
                                </sourceFileSet>
                                <sourceFileSet>
                                    <directory>${basedir}/src/main/resources/env</directory>
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


**Example #2** - Merge using simple merger:


    <build>
        <plugins>
            <plugin>
                <groupId>com.carmatechnologies.maven</groupId>
                <artifactId>properties-files-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>merge</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <parallel>false</parallel>
                    <operations>
                        <operation>
                            <targetFile>${basedir}/target/merged.properties</targetFile>
                            <sourceFileSets>
                                <sourceFileSet>
                                    <directory>${basedir}/src/main/resources</directory>
                                    <includes>
                                        <include>a.properties</include>
                                        <include>b.properties</include>
                                    </includes>
                                </sourceFileSet>
                                <sourceFileSet>
                                    <directory>${basedir}/src/main/resources/env</directory>
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


**Example #3** - Merge using resource filtering:


    <build>
        <plugins>
            <plugin>
                <groupId>com.carmatechnologies.maven</groupId>
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
                            <filtering>true</filtering>
                            [...]
                        </operation>
                    </operations>
                </configuration>
            </plugin>
        </plugins>
    </build>


**Example #4** - Fail on missing input properties file:


    <build>
        <plugins>
            <plugin>
                <groupId>com.carmatechnologies.maven</groupId>
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
                            <errorOnMissingFile>true</errorOnMissingFile>
                            [...]
                        </operation>
                    </operations>
                </configuration>
            </plugin>
        </plugins>
    </build>

