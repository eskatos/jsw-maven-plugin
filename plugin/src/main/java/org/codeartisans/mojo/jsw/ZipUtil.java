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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.codehaus.plexus.util.FileUtils;

/* package */ final class ZipUtil
{

    /* package */ static void unzipRemovingBaseDir( File zip, File extractTo, String baseDirName )
            throws IOException
    {
        ZipFile archive = new ZipFile( zip );
        Enumeration e = archive.entries();
        while ( e.hasMoreElements() ) {
            ZipEntry entry = ( ZipEntry ) e.nextElement();
            if ( entry.getName().length() - 1 != baseDirName.length() ) {
                File file = new File( extractTo, entry.getName().substring( baseDirName.length() + 1 ) );
                if ( entry.isDirectory() && !file.exists() ) {
                    FileUtils.forceMkdir( file );
                } else {
                    if ( !file.getParentFile().exists() ) {
                        FileUtils.forceMkdir( file.getParentFile() );
                    }
                    InputStream in = archive.getInputStream( entry );
                    BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ) );
                    byte[] buffer = new byte[ 8192 ];
                    int read;
                    while ( -1 != ( read = in.read( buffer ) ) ) {
                        out.write( buffer, 0, read );
                    }
                    in.close();
                    out.close();
                }
            }
        }
    }

    private ZipUtil()
    {
    }

}
