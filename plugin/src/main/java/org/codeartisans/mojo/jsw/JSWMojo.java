/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codeartisans.mojo.jsw;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import org.codeartisans.mojo.jsw.config.JavaService;
import org.codehaus.plexus.util.FileUtils;

import org.codehaus.plexus.util.StringUtils;

/**
 * @goal jsw
 * @requiresDependencyResolution runtime
 */
public class JSWMojo
        extends AbstractMojo
{

    // Configuration ---------------------------------------------------------------------------------------------------
    /**
     * @parameter default-value="false"
     */
    private boolean skip;
    /**
     * @parameter expression="${assembleDirectory}" default-value="${project.build.directory}/jsw"
     * @required
     */
    private File assembleDirectory;
    /**
     * @parameter
     */
    private JavaService globals;
    /**
     * @parameter
     */
    private JavaService[] services;
    /**
     * The layout of the generated Maven repository. Supported types - "default" (Maven2) | "legacy" (Maven1) | "flat"
     * (flat <code>lib/</code> style).
     *
     * @parameter default-value="flat"
     */
    private String repositoryLayout;
    // Context ---------------------------------------------------------------------------------------------------------
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    /**
     * @parameter expression="${project.artifact}"
     * @required
     * @readonly
     */
    private Artifact projectArtifact;
    /**
     * @parameter expression="${project.runtimeArtifacts}"
     * @required
     * @readonly
     */
    private List<Artifact> artifacts;
    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;
    // Components ------------------------------------------------------------------------------------------------------
    /**
     * @component
     */
    private ArtifactRepositoryFactory artifactRepositoryFactory;
    /**
     * @component
     */
    private ArtifactInstaller artifactInstaller;
    /**
     * @component role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"
     */
    private Map<String, ArtifactRepositoryLayout> availableRepositoryLayouts;

    @Override
    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        if ( skip ) {
            getLog().info( "jsw-maven-plugin execution is skipped" );
            return;
        }
        if ( services == null || services.length == 0 ) {
            getLog().warn( "No services to wrap as a daemon found in configuration, doing nothing" );
            return;
        }
        getLog().info( "Will wrap " + services.length + " services" );

        applyDefaultsToGlobals();

        for ( JavaService eachService : services ) {
            eachService = applyGlobalsTo( eachService );
        }

        deployJSWTree();
        getLog().info( "JSW tree deployed" );

        List<String> projectClassPaths = deployArtifactRepository();
        getLog().info( "Artifacts repository deployed" );

        for ( JavaService eachService : services ) {
            eachService.addJavaClassPaths( projectClassPaths );
            deployService( eachService );
            getLog().info( "Service " + eachService.getDaemonName() + " deployed" );
        }

        cleanupJSWTree();
        getLog().info( "JSW tree cleaned up" );
    }

    /**
     * Apply defaults to configured globals.
     * @throws MojoExecutionException if configured globals contains forbidden values
     */
    private void applyDefaultsToGlobals()
            throws MojoExecutionException
    {
        if ( globals == null ) {
            globals = new JavaService();
        } else {
            // Check forbidden globals
            if ( !StringUtils.isEmpty( globals.getWrapperLogfile() ) ) {
                String errMsg = "You cannot set a wrapper log file in <globals/>, set it in each <service/> instead";
                getLog().error( errMsg );
                throw new MojoExecutionException( errMsg );
            }
            if ( !StringUtils.isEmpty( globals.getDaemonName() ) ) {
                String errMsg = "You cannot set a daemon name in <globals/>, set it in each <service/> instead";
                getLog().error( errMsg );
                throw new MojoExecutionException( errMsg );
            }
            if ( !StringUtils.isEmpty( globals.getAppName() ) ) {
                String errMsg = "You cannot set a app name in <globals/>, set it in each <service/> instead";
                getLog().error( errMsg );
                throw new MojoExecutionException( errMsg );
            }
            if ( !globals.getAppArguments().isEmpty() ) {
                String errMsg = "You cannot set a app arguments in <globals/>, set it in each <service/> instead";
                getLog().error( errMsg );
                throw new MojoExecutionException( errMsg );
            }
        }

        JavaService defaults = new JavaService().applyDefaults();

        // Apply wrapper defaults
        if ( globals.isWrapperDebug() == null ) {
            globals.setWrapperDebug( defaults.isWrapperDebug() );
        }
        if ( globals.isWrapperJmx() == null ) {
            globals.setWrapperJmx( defaults.isWrapperJmx() );
        }
        if ( StringUtils.isEmpty( globals.getWrapperConsoleLogFormat() ) ) {
            globals.setWrapperConsoleLogFormat( defaults.getWrapperConsoleLogFormat() );
        }
        if ( globals.getWrapperConsoleLogLevel() == null ) {
            globals.setWrapperConsoleLogLevel( defaults.getWrapperConsoleLogLevel() );
        }
        if ( StringUtils.isEmpty( globals.getWrapperLogfile() ) ) {
            globals.setWrapperLogfile( defaults.getWrapperLogfile() );
        }
        if ( StringUtils.isEmpty( globals.getWrapperLogfileFormat() ) ) {
            globals.setWrapperLogfileFormat( defaults.getWrapperLogfileFormat() );
        }
        if ( globals.getWrapperLogfileLevel() == null ) {
            globals.setWrapperLogfileLevel( defaults.getWrapperLogfileLevel() );
        }
        if ( StringUtils.isEmpty( globals.getWrapperLogfileMaxSize() ) ) {
            globals.setWrapperLogfileMaxSize( defaults.getWrapperLogfileMaxSize() );
        }
        if ( globals.getWrapperLogfileMaxFiles() == null ) {
            globals.setWrapperLogfileMaxFiles( defaults.getWrapperLogfileMaxFiles() );
        }
        if ( globals.getWrapperSyslogLogLevel() == null ) {
            globals.setWrapperSyslogLogLevel( defaults.getWrapperSyslogLogLevel() );
        }

        // Apply java defaults
        if ( globals.isJavaAutoBits() == null ) {
            globals.setJavaAutoBits( defaults.isJavaAutoBits() );
        }
        if ( globals.isJavaLogGeneratedCommand() == null ) {
            globals.setJavaLogGeneratedCommand( defaults.isJavaLogGeneratedCommand() );
        }

        // Apply app defaults
        if ( StringUtils.isEmpty( globals.getAppMainClass() ) ) {
            globals.setAppMainClass( defaults.getAppMainClass() );
        }
    }

    /**
     * Apply globals to given service.
     * @throws MojoExecutionException if given service miss a mandatory property
     */
    private JavaService applyGlobalsTo( JavaService service )
            throws MojoExecutionException
    {
        // Apply global environment
        service.addEnvironments( globals.getEnvironment() );

        // Apply wrapper globals
        if ( service.isWrapperDebug() == null ) {
            service.setWrapperDebug( globals.isWrapperDebug() );
        }
        if ( service.isWrapperJmx() == null ) {
            service.setWrapperJmx( globals.isWrapperJmx() );
        }
        if ( StringUtils.isEmpty( service.getWrapperConsoleLogFormat() ) ) {
            service.setWrapperConsoleLogFormat( globals.getWrapperConsoleLogFormat() );
        }
        if ( service.getWrapperConsoleLogLevel() == null ) {
            service.setWrapperConsoleLogLevel( globals.getWrapperConsoleLogLevel() );
        }
        if ( StringUtils.isEmpty( service.getWrapperLogfile() ) ) {
            service.setWrapperLogfile( "../logs/" + service.getDaemonName() + ".log.ROLLNUM" );
        }
        if ( StringUtils.isEmpty( service.getWrapperLogfileFormat() ) ) {
            service.setWrapperLogfileFormat( globals.getWrapperLogfileFormat() );
        }
        if ( service.getWrapperLogfileLevel() == null ) {
            service.setWrapperLogfileLevel( globals.getWrapperLogfileLevel() );
        }
        if ( StringUtils.isEmpty( service.getWrapperLogfileMaxSize() ) ) {
            service.setWrapperLogfileMaxSize( globals.getWrapperLogfileMaxSize() );
        }
        if ( service.getWrapperLogfileMaxFiles() == null ) {
            service.setWrapperLogfileMaxFiles( globals.getWrapperLogfileMaxFiles() );
        }
        if ( service.getWrapperSyslogLogLevel() == null ) {
            service.setWrapperSyslogLogLevel( globals.getWrapperSyslogLogLevel() );
        }

        // Apply java globals
        if ( service.isJavaAutoBits() == null ) {
            service.setJavaAutoBits( globals.isJavaAutoBits() );
        }
        if ( service.isJavaLogGeneratedCommand() == null ) {
            service.setJavaLogGeneratedCommand( globals.isJavaLogGeneratedCommand() );
        }
        service.addJavaArgumentsFirst( globals.getJavaArguments() );
        service.addJavaLibraryPathsFirst( globals.getJavaLibraryPaths() );
        service.addJavaClassPathsFirst( globals.getJavaClassPaths() );

        // Apply app globals
        if ( StringUtils.isEmpty( service.getAppMainClass() ) ) {
            service.setAppMainClass( globals.getAppMainClass() );
        }

        // Check mandatories
        if ( StringUtils.isEmpty( service.getAppMainClass() ) ) {
            String errMsg = "Service {" + service.getDaemonName() + "} has no appMainClass, cannot continue";
            getLog().error( errMsg );
            throw new MojoExecutionException( errMsg );
        }

        return service;
    }

    private void deployJSWTree()
            throws MojoExecutionException
    {
        try {
            JSW.extractInto( assembleDirectory );
        } catch ( IOException ex ) {
            throw new MojoExecutionException( "Unable to deploy JSW tree", ex );
        }
    }

    private List<String> deployArtifactRepository()
            throws MojoExecutionException
    {
        ArtifactRepositoryLayout artifactRepositoryLayout = availableRepositoryLayouts.get( repositoryLayout );
        if ( artifactRepositoryLayout == null ) {
            throw new MojoExecutionException( "Unknown repository layout '" + repositoryLayout + "'." );
        }
        // The repo where the jar files will be installed
        ArtifactRepository artifactRepository = artifactRepositoryFactory.createDeploymentArtifactRepository(
                project.getArtifactId(),
                "file://" + assembleDirectory.getAbsolutePath() + "/lib",
                artifactRepositoryLayout,
                false );
        List<String> classPaths = new ArrayList<String>();
        for ( Artifact artifact : artifacts ) {
            classPaths.add( installArtifact( artifactRepository, artifact ) );
        }
        // install the project's artifact in the new repository
        classPaths.add( installArtifact( artifactRepository, projectArtifact ) );
        return classPaths;
    }

    private String installArtifact( ArtifactRepository artifactRepository, Artifact artifact )
            throws MojoExecutionException
    {
        try {
            // Necessary for the artifact's baseVersion to be set correctly
            // See: http://mail-archives.apache.org/mod_mbox/maven-dev/200511.mbox/%3c437288F4.4080003@apache.org%3e
            artifact.isSnapshot();
            if ( artifact.getFile() != null ) {
                artifactInstaller.install( artifact.getFile(), artifact, artifactRepository );
                return "../lib/" + artifactRepository.pathOf( artifact );
            }
            return null;
        } catch ( ArtifactInstallationException e ) {
            throw new MojoExecutionException( "Failed to copy artifact.", e );
        }
    }

    private void deployService( JavaService eachService )
            throws MojoExecutionException
    {
        try {
            JSW.generateWrapperConfiguration( assembleDirectory, eachService );
            JSW.generateWrapperUnixScript( assembleDirectory, eachService );
        } catch ( IOException ex ) {
            throw new MojoExecutionException( "Unable to deploy service: " + eachService.getDaemonName(), ex );
        }

    }

    private void cleanupJSWTree()
            throws MojoExecutionException
    {
        try {
            FileUtils.forceDelete( new File( assembleDirectory, "src" ) );
        } catch ( IOException ex ) {
            throw new MojoExecutionException( "Unable to clean up JSW tree", ex );
        }
    }

}
