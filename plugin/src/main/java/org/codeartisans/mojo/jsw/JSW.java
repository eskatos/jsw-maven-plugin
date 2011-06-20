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
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.codeartisans.mojo.jsw.config.JavaService;
import org.codeartisans.mojo.jsw.writers.AppWrapperConfWriter;
import org.codeartisans.mojo.jsw.writers.AppWrapperUnixWriter;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

public final class JSW
{

    private static final String DELTA_PACK = "jsw-community-3.5.9-delta-pack.zip";
    private static final String ENTRY_NAME_PREFIX = "wrapper-delta-pack-3.5.9";

    public static void extractInto( File destDir )
            throws IOException
    {
        fullExtractInto( destDir );
        File bin = new File( destDir, "bin" );
        for ( File eachBin : bin.listFiles( new FilenameFilter()
        {

            @Override
            public boolean accept( File dir, String name )
            {
                return !name.startsWith( "wrapper" );
            }

        } ) ) {
            FileUtils.forceDelete( eachBin );
        }
        File conf = new File( destDir, "conf" );
        FileUtils.forceDelete( new File( conf, "wrapper.conf" ) );
        FileUtils.forceDelete( new File( conf, "demoapp.conf" ) );
        FileUtils.forceDelete( new File( destDir, "doc" ) );
        FileUtils.forceDelete( new File( destDir, "jdoc" ) );
        FileUtils.forceDelete( new File( destDir, "README_de.txt" ) );
        FileUtils.forceDelete( new File( destDir, "README_en.txt" ) );
        FileUtils.forceDelete( new File( destDir, "README_es.txt" ) );
        FileUtils.forceDelete( new File( destDir, "README_ja.txt" ) );
        File src = new File( destDir, "src" );
        FileUtils.forceDelete( new File( src, "conf" ) );
    }

    public static File generateWrapperConfiguration( File baseDir, JavaService service )
            throws IOException
    {
        StringWriter sw = new StringWriter();
        new AppWrapperConfWriter( service ).build( sw );
        String serviceConf = sw.toString();
        File confDir = new File( baseDir, "conf" );
        File serviceConfFile = new File( confDir, service.getDaemonName() + ".conf" );
        IOUtil.copy( new StringReader( serviceConf ), new FileOutputStream( serviceConfFile ) );
        Permissions.forceExecutable( serviceConfFile, true );
        return serviceConfFile;
    }

    public static File generateWrapperUnixScript( File baseDir, JavaService service )
            throws IOException
    {
        File shTemplateFile = new File( baseDir, "src/bin/sh.script.in" );
        StringWriter sw = new StringWriter();
        IOUtil.copy( new FileReader( shTemplateFile ), sw );
        String shTemplate = sw.toString();
        sw = new StringWriter();
        new AppWrapperUnixWriter( shTemplate, service ).build( sw );
        String shContent = sw.toString();
        File bin = new File( baseDir, "bin" );
        File sh = new File( bin, service.getDaemonName() );
        IOUtil.copy( new StringReader( shContent ), new FileOutputStream( sh ) );
        Permissions.forceExecutable( sh, true );
        return sh;
    }


    /* package */ static void fullExtractInto( File destDir )
            throws IOException
    {
        if ( !destDir.exists() ) {
            FileUtils.forceMkdir( destDir );
        }
        File tmp = File.createTempFile( "jsw-maven-plugin", "" + System.currentTimeMillis() );
        tmp.deleteOnExit();
        InputStream is = JSW.class.getResourceAsStream( DELTA_PACK );
        IOUtil.copy( is, new FileOutputStream( tmp ) );
        ZipUtil.unzipRemovingBaseDir( tmp, destDir, ENTRY_NAME_PREFIX );
        Permissions.fixPermissions( destDir );
    }

    private JSW()
    {
    }

}
