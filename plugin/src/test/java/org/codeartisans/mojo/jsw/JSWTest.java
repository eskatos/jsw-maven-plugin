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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.codeartisans.mojo.jsw.config.JavaService;
import org.codeartisans.mojo.jsw.writers.AppWrapperConfWriter;
import org.codeartisans.mojo.jsw.writers.AppWrapperUnixWriter;

import org.codehaus.plexus.util.IOUtil;

import org.junit.Ignore;
import static org.junit.Assert.*;
import org.junit.Test;

public class JSWTest
{

    @Test
    @Ignore
    public void testFullExtract()
            throws IOException
    {
        File destDir = new File( "target/full-extract" );
        JSW.fullExtractInto( destDir );

        File bin = new File( destDir, "bin" );
        File lib = new File( destDir, "lib" );
        assertTrue( bin.exists() );
        assertTrue( lib.exists() );

        File demoapp = new File( bin, "wrapper-linux-x86-32" );
        assertTrue( demoapp.exists() );
        assertTrue( demoapp.canExecute() );

        File linux32 = new File( lib, "libwrapper-linux-x86-32.so" );
        assertTrue( linux32.exists() );
        assertTrue( linux32.canExecute() );
    }

    @Test
    @Ignore
    public void testExtract()
            throws IOException
    {
        File destDir = new File( "target/small-extract" );
        JSW.extractInto( destDir );

        File bin = new File( destDir, "bin" );
        File lib = new File( destDir, "lib" );
        assertTrue( bin.exists() );
        assertTrue( lib.exists() );

        File demoapp = new File( bin, "wrapper-linux-x86-32" );
        assertTrue( demoapp.exists() );
        assertTrue( demoapp.canExecute() );

        File linux32 = new File( lib, "libwrapper-linux-x86-32.so" );
        assertTrue( linux32.exists() );
        assertTrue( linux32.canExecute() );
    }

    @Test
    @Ignore
    public void testConfig()
            throws IOException
    {
        JavaService config = newConfigInstance();

        StringWriter sw = new StringWriter();
        new AppWrapperConfWriter( config ).build( sw );
        String wrapperConf = sw.toString();
        System.out.println( wrapperConf );
        // WARN No assertion here !

        File destDir = new File( "target/config-extract" );
        JSW.extractInto( destDir );
        File confDir = new File( destDir, "conf" );
        confDir.mkdirs();
        File wrapperConfFile = new File( confDir, "wrapper.conf" );
        IOUtil.copy( new StringReader( wrapperConf ), new FileOutputStream( wrapperConfFile ) );

        assertTrue( wrapperConfFile.exists() );
    }

    @Test
    public void testSH()
            throws IOException
    {
        File destDir = new File( "target/linux-extract" );
        JSW.extractInto( destDir );
        JavaService config = newConfigInstance();

        StringWriter sw = new StringWriter();
        new AppWrapperUnixWriter( "etc", config ).build( sw );
        String shContent = sw.toString();
        System.out.println( shContent );

        File bin = new File( destDir, "bin" );
        File sh = new File( bin, config.getAppName() );

        IOUtil.copy( new StringReader( shContent ), new FileOutputStream( sh ) );

    }

    private JavaService newConfigInstance()
    {
        JavaService config = new JavaService().applyDefaults();

        config.setWrapperJmx( false );
        config.setWrapperLogfile( "../var/log/wrapper.log.ROLLNUM" );

        config.addEnvironment( "FUCK_ENV", "a duck" );

        config.setJavaLogGeneratedCommand( true );
        config.addJavaArgument( "-server" );
        config.addJavaArgument( "-Xms64m" );
        config.addJavaLibraryPath( "../lib" );
        config.addJavaClassPath( "../lib/foo.jar" );
        config.addJavaClassPath( "../lib/bar.jar" );

        config.setAppName( "TestApp" );
        config.setAppMainClass( getClass().getName() );
        config.addAppArgument( "start" );

        config.setDaemonName( "test-daemon" );

        return config;
    }

}
