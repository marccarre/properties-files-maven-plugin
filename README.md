A Maven plugin to manipulate properties files in an easy way.

Features:
  - Merge multiple properties files into a single one.
  - Merge multiple properties files into a single one, in parallel.

*******************************************************************************
**Example #1** - Merge using default merger (parallel merger):


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


