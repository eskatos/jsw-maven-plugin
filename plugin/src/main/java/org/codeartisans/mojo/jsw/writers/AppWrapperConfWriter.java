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
package org.codeartisans.mojo.jsw.writers;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.codeartisans.mojo.jsw.config.JavaService;

/**
 * @see http://wrapper.tanukisoftware.com/doc/english/integrate-simple-nix.html
 */
public class AppWrapperConfWriter
{

    private static final String EQ = "=";
    private static final String DASH = "#";
    private final JavaService config;

    public AppWrapperConfWriter( JavaService config )
    {
        this.config = config;
    }

    public void build( Writer writer )
    {
        PrintWriter out = new PrintWriter( writer );
        out.println( "#encoding=UTF-8" );
        out.println();

        int nextJavaArgument = 1;
        int nextJavaLibraryPath = 1;
        int nextJavaClassPath = 1;
        int nextAppArgument = 1;

        // Environment variables
        for ( Map.Entry<String, String> eachEnv : config.getEnvironment().entrySet() ) {
            out.print( "set." );
            out.print( eachEnv.getKey() );
            out.print( EQ );
            out.println( eachEnv.getValue() );
        }
        out.println();

        // Wrapper debug
        if ( !config.isWrapperDebug() ) {
            out.print( DASH );
        }
        out.println( "wrapper.debug=TRUE" );
        out.println();

        // Wrapper console log
        out.print( "wrapper.console.title=" );
        out.println( config.getAppName() );
        out.print( "wrapper.console.format=" );
        out.println( config.getWrapperConsoleLogFormat() );
        out.print( "wrapper.console.loglevel=" );
        out.println( config.getWrapperConsoleLogLevel() );
        out.println();

        // Wrapper file log
        out.print( "wrapper.logfile=" );
        out.println( config.getWrapperLogfile() );
        out.print( "wrapper.logfile.format=" );
        out.println( config.getWrapperLogfileFormat() );
        out.print( "wrapper.logfile.loglevel=" );
        out.println( config.getWrapperLogfileLevel() );
        out.println();

        // Java automatic 32/64 bits
        if ( !config.isJavaAutoBits() ) {
            out.print( DASH );
        }
        out.println( "wrapper.java.additional.auto_bits=TRUE" );
        out.println();

        // Java Command
        out.println( "wrapper.java.command=java" );
        if ( !config.isJavaLogGeneratedCommand() ) {
            out.print( DASH );
        }
        out.println( "wrapper.java.command.loglevel=INFO" );
        out.println();

        // Java Arguments
        out.print( "wrapper.java.additional." );
        out.print( nextJavaArgument );
        out.print( "=-Dorg.tanukisoftware.wrapper.WrapperManager.mbean=" );
        out.println( config.isWrapperJmx() );
        nextJavaArgument++;
        for ( String eachJavaArgument : config.getJavaArguments() ) {
            out.print( "wrapper.java.additional." );
            out.print( nextJavaArgument );
            out.print( EQ );
            out.println( eachJavaArgument );
            nextJavaArgument++;
        }
        out.println();

        // Java LibraryPath
        out.print( "wrapper.java.library.path." );
        out.print( nextJavaLibraryPath );
        out.print( EQ );
        out.println( "../lib" );
        nextJavaLibraryPath++;
        for ( String eachJavaLibraryPath : config.getJavaLibraryPaths() ) {
            out.print( "wrapper.java.library.path." );
            out.print( nextJavaLibraryPath );
            out.print( EQ );
            out.println( eachJavaLibraryPath );
            nextJavaLibraryPath++;
        }
        out.println();

        // Java ClassPath
        out.print( "wrapper.java.classpath." );
        out.print( nextJavaClassPath );
        out.println( "=../lib/wrapper.jar" );
        nextJavaClassPath++;
        for ( String eachJavaClassPath : config.getJavaClassPaths() ) {
            out.print( "wrapper.java.classpath." );
            out.print( nextJavaClassPath );
            out.print( EQ );
            out.println( eachJavaClassPath );
            nextJavaClassPath++;
        }
        out.println();
        nextJavaClassPath++;

        // App Main Class
        out.print( "wrapper.java.mainclass=" );
        out.println( config.getAppMainClass() );
        out.println();

        // App Arguments
        if ( config.getAppArguments().isEmpty() ) {
            // app.parameter cannot be empty so we use the app main class as a sample argument
            out.print( "wrapper.app.parameter." );
            out.print( nextAppArgument );
            out.print( EQ );
            out.println( config.getAppMainClass() );
            nextAppArgument++;
        } else {
            for ( String eachAppArgument : config.getAppArguments() ) {
                out.print( "wrapper.app.parameter." );
                out.print( nextAppArgument );
                out.print( EQ );
                out.println( eachAppArgument );
                nextAppArgument++;
            }
        }
        out.println();

        out.flush();
    }

}
