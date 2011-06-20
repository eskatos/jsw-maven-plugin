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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.codeartisans.mojo.jsw.config.JavaService;
import org.codehaus.plexus.util.IOUtil;

public class AppWrapperUnixWriter
{

    private static final String TEMPLATE_RSRC = "sh.script.in";
    private final String confDirname;
    private final JavaService config;

    public AppWrapperUnixWriter( String confDirname, JavaService config )
    {
        this.confDirname = confDirname;
        this.config = config;
    }

    public void build( Writer writer )
            throws IOException
    {
        StringWriter sw = new StringWriter();
        IOUtil.copy( getClass().getResourceAsStream( TEMPLATE_RSRC ), sw );
        String template = sw.toString();
        String sh = template;
        sh = sh.replaceAll( "@conf.dir@", confDirname );
        sh = sh.replaceAll( "@conf.name@", config.getDaemonName() );
        sh = sh.replaceAll( "@app.name@", config.getDaemonName() );
        sh = sh.replaceAll( "@app.long.name@", config.getAppName() );
        writer.write( sh );
    }

}
