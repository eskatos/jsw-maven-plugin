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
package org.codeartisans.mojo.jsw.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see http://wrapper.tanukisoftware.com/doc/english/integrate.html
 */
public final class JavaService
{

    private Map<String, String> environment = new HashMap<String, String>();
    // Wrapper configuration
    private Boolean wrapperDebug;
    private Boolean wrapperJmx;
    private String wrapperConsoleLogFormat;
    private WrapperLogLevel wrapperConsoleLogLevel;
    private String wrapperLogfile;
    private String wrapperLogfileFormat;
    private WrapperLogLevel wrapperLogfileLevel;
    private String wrapperLogfileMaxSize;
    private Integer wrapperLogfileMaxFiles;
    private WrapperLogLevel wrapperSyslogLogLevel;
    // Java configuration
    private Boolean javaAutoBits; // 32/64 bits
    private Boolean javaLogGeneratedCommand;
    private List<String> javaArguments = new ArrayList<String>();
    private List<String> javaLibraryPaths = new ArrayList<String>();
    private List<String> javaClassPaths = new ArrayList<String>();
    // Application configuration
    private String appName;
    private String appMainClass;
    private List<String> appArguments = new ArrayList<String>();
    // Daemon configuration
    private String daemonName;

    public JavaService applyDefaults()
    {
        wrapperDebug = Boolean.FALSE;
        wrapperJmx = Boolean.TRUE;
        wrapperConsoleLogFormat = "PM";
        wrapperConsoleLogLevel = WrapperLogLevel.INFO;
        wrapperLogfileFormat = "LPTM";
        wrapperLogfileLevel = WrapperLogLevel.INFO;
        wrapperLogfileMaxSize = "0";
        wrapperLogfileMaxFiles = 0;
        wrapperSyslogLogLevel = WrapperLogLevel.NONE;
        javaAutoBits = Boolean.TRUE;
        javaLogGeneratedCommand = Boolean.FALSE;
        return this;
    }

    public void setDaemonName( String daemonName )
    {
        this.daemonName = daemonName;
    }

    public void addEnvironment( String variable, String value )
    {
        environment.put( variable, value );
    }

    public void addEnvironments( Map<String, String> env )
    {
        environment.putAll( env );
    }

    public void clearEnvironment()
    {
        environment.clear();
    }

    public void addJavaArgument( String javaArgument )
    {
        javaArguments.add( javaArgument );
    }

    public void addJavaArguments( Collection<String> javaArgs )
    {
        javaArguments.addAll( javaArgs );
    }

    public void addJavaArgumentsFirst( Collection<String> javaArgs )
    {
        List<String> newJavaArguments = new ArrayList<String>( javaArgs );
        newJavaArguments.addAll( javaArguments );
        javaArguments = newJavaArguments;
    }

    public void clearJavaArguments()
    {
        javaArguments.clear();
    }

    public void addJavaLibraryPath( String javaLibraryPath )
    {
        javaLibraryPaths.add( javaLibraryPath );
    }

    public void addJavaLibraryPaths( Collection<String> javaArgs )
    {
        javaLibraryPaths.addAll( javaArgs );
    }

    public void addJavaLibraryPathsFirst( Collection<String> javaLibPaths )
    {
        List<String> newJavaLibPaths = new ArrayList<String>( javaLibPaths );
        newJavaLibPaths.addAll( javaLibraryPaths );
        javaLibraryPaths = newJavaLibPaths;
    }

    public void clearJavaLibraryPaths()
    {
        javaLibraryPaths.clear();
    }

    public void addJavaClassPath( String javaClassPath )
    {
        javaClassPaths.add( javaClassPath );
    }

    public void addJavaClassPaths( Collection<String> javaCPs )
    {
        javaClassPaths.addAll( javaCPs );
    }

    public void addJavaClassPathFirst( String javaCP )
    {
        addJavaClassPathsFirst( Collections.singleton( javaCP ) );
    }

    public void addJavaClassPathsFirst( Collection<String> javaCPs )
    {
        List<String> newJavaClassPaths = new ArrayList<String>( javaCPs );
        newJavaClassPaths.addAll( javaClassPaths );
        javaClassPaths = newJavaClassPaths;
    }

    public void clearJavaClassPaths()
    {
        javaClassPaths.clear();
    }

    public void addAppArgument( String appArgument )
    {
        appArguments.add( appArgument );
    }

    public void addAppArguments( Collection<String> appArgs )
    {
        appArguments.addAll( appArgs );
    }

    public void clearAppArguments()
    {
        appArguments.clear();
    }

    public void setAppMainClass( String appMainClass )
    {
        this.appMainClass = appMainClass;
    }

    public void setAppName( String appName )
    {
        this.appName = appName;
    }

    public void setWrapperJmx( Boolean wrapperJmx )
    {
        this.wrapperJmx = wrapperJmx;
    }

    public void setWrapperLogfile( String wrapperLogfile )
    {
        this.wrapperLogfile = wrapperLogfile;
    }

    public void setJavaAutoBits( Boolean javaAutoBits )
    {
        this.javaAutoBits = javaAutoBits;
    }

    public void setJavaLogGeneratedCommand( Boolean javaLogGeneratedCommand )
    {
        this.javaLogGeneratedCommand = javaLogGeneratedCommand;
    }

    public void setWrapperConsoleLogFormat( String wrapperConsoleLogFormat )
    {
        this.wrapperConsoleLogFormat = wrapperConsoleLogFormat;
    }

    public void setWrapperConsoleLogLevel( WrapperLogLevel wrapperConsoleLogLevel )
    {
        this.wrapperConsoleLogLevel = wrapperConsoleLogLevel;
    }

    public void setWrapperDebug( Boolean wrapperDebug )
    {
        this.wrapperDebug = wrapperDebug;
    }

    public void setWrapperLogfileFormat( String wrapperLogfileFormat )
    {
        this.wrapperLogfileFormat = wrapperLogfileFormat;
    }

    public void setWrapperLogfileLevel( WrapperLogLevel wrapperLogfileLevel )
    {
        this.wrapperLogfileLevel = wrapperLogfileLevel;
    }

    public void setWrapperLogfileMaxFiles( Integer wrapperLogfileMaxFiles )
    {
        this.wrapperLogfileMaxFiles = wrapperLogfileMaxFiles;
    }

    public void setWrapperLogfileMaxSize( String wrapperLogfileMaxSize )
    {
        this.wrapperLogfileMaxSize = wrapperLogfileMaxSize;
    }

    public void setWrapperSyslogLogLevel( WrapperLogLevel wrapperSyslogLogLevel )
    {
        this.wrapperSyslogLogLevel = wrapperSyslogLogLevel;
    }

    public List<String> getAppArguments()
    {
        return Collections.unmodifiableList( appArguments );
    }

    public String getAppMainClass()
    {
        return appMainClass;
    }

    public String getAppName()
    {
        return appName;
    }

    public String getDaemonName()
    {
        return daemonName;
    }

    public Map<String, String> getEnvironment()
    {
        return Collections.unmodifiableMap( environment );
    }

    public List<String> getJavaArguments()
    {
        return Collections.unmodifiableList( javaArguments );
    }

    public Boolean isJavaAutoBits()
    {
        return javaAutoBits;
    }

    public List<String> getJavaClassPaths()
    {
        return Collections.unmodifiableList( javaClassPaths );
    }

    public List<String> getJavaLibraryPaths()
    {
        return Collections.unmodifiableList( javaLibraryPaths );
    }

    public Boolean isJavaLogGeneratedCommand()
    {
        return javaLogGeneratedCommand;
    }

    public String getWrapperConsoleLogFormat()
    {
        return wrapperConsoleLogFormat;
    }

    public WrapperLogLevel getWrapperConsoleLogLevel()
    {
        return wrapperConsoleLogLevel;
    }

    public Boolean isWrapperDebug()
    {
        return wrapperDebug;
    }

    public Boolean isWrapperJmx()
    {
        return wrapperJmx;
    }

    public String getWrapperLogfile()
    {
        return wrapperLogfile;
    }

    public String getWrapperLogfileFormat()
    {
        return wrapperLogfileFormat;
    }

    public WrapperLogLevel getWrapperLogfileLevel()
    {
        return wrapperLogfileLevel;
    }

    public Integer getWrapperLogfileMaxFiles()
    {
        return wrapperLogfileMaxFiles;
    }

    public String getWrapperLogfileMaxSize()
    {
        return wrapperLogfileMaxSize;
    }

    public WrapperLogLevel getWrapperSyslogLogLevel()
    {
        return wrapperSyslogLogLevel;
    }

    @Override
    public String toString()
    {
        return "JavaService{" + "environment=" + environment + "wrapperDebug=" + wrapperDebug + "wrapperJmx=" + wrapperJmx + "wrapperConsoleLogFormat=" + wrapperConsoleLogFormat + "wrapperConsoleLogLevel=" + wrapperConsoleLogLevel + "wrapperLogfile=" + wrapperLogfile + "wrapperLogfileFormat=" + wrapperLogfileFormat + "wrapperLogfileLevel=" + wrapperLogfileLevel + "wrapperLogfileMaxSize=" + wrapperLogfileMaxSize + "wrapperLogfileMaxFiles=" + wrapperLogfileMaxFiles + "wrapperSyslogLogLevel=" + wrapperSyslogLogLevel + "javaAutoBits=" + javaAutoBits + "javaLogGeneratedCommand=" + javaLogGeneratedCommand + "javaArguments=" + javaArguments + "javaLibraryPaths=" + javaLibraryPaths + "javaClassPaths=" + javaClassPaths + "appName=" + appName + "appMainClass=" + appMainClass + "appArguments=" + appArguments + "daemonName=" + daemonName + '}';
    }

}
