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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Stack;

/* package */ final class Permissions
{

    /* package */ static void fixPermissions( File baseDir )
            throws IOException
    {
        setBasePermissions( baseDir );
        File bin = new File( baseDir, "bin" );
        for ( File eachBin : bin.listFiles() ) {
            forceExecutable( eachBin, true );
        }
        File lib = new File( baseDir, "lib" );
        for ( File eachLib : lib.listFiles( new FilenameFilter()
        {

            @Override
            public boolean accept( File dir, String name )
            {
                return !name.endsWith( ".jar" );
            }

        } ) ) {
            forceExecutable( eachLib, true );
        }
    }

    private static void setBasePermissions( File baseDir )
            throws IOException
    {
        Stack<File> stack = new Stack<File>();
        for ( File eachFile : baseDir.listFiles() ) {
            stack.push( eachFile );
        }
        while ( !stack.empty() ) {
            File eachFile = stack.pop();
            forceReadable( eachFile, true );
            forceWritable( eachFile, true );
            forceExecutable( eachFile, eachFile.isDirectory() );
            if ( eachFile.isDirectory() ) {
                for ( File eachChild : eachFile.listFiles() ) {
                    stack.push( eachChild );
                }
            }
        }
    }

    public static void forceReadable( File file, boolean readable )
            throws IOException
    {
        if ( !file.setReadable( readable, true ) ) {
            throw new IOException( "Unable to set readable? " + readable + " on " + file.getAbsolutePath() );
        }
    }

    public static void forceWritable( File file, boolean writable )
            throws IOException
    {
        if ( !file.setWritable( writable, true ) ) {
            throw new IOException( "Unable to set writable? " + writable + " on " + file.getAbsolutePath() );
        }
    }

    public static void forceExecutable( File file, boolean executable )
            throws IOException
    {
        if ( !file.setExecutable( executable, true ) ) {
            throw new IOException( "Unable to set executable? " + executable + " on " + file.getAbsolutePath() );
        }
    }

    private Permissions()
    {
    }

}
