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
package org.codeartisans.mojo.jsw.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class Main
{

    private static final int DEFAULT_PORT = 8080;

    public static void main( String[] args )
            throws IOException
    {
        int port = findPort();
        InetSocketAddress addr = new InetSocketAddress( port );
        HttpServer server = HttpServer.create( addr, 0 );

        server.createContext( "/", new HttpHandler()
        {

            @Override
            public void handle( HttpExchange exchange )
                    throws IOException
            {
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set( "Content-Type", "text/plain" );
                exchange.sendResponseHeaders( 200, 0 );

                OutputStream responseBody = exchange.getResponseBody();
                Headers requestHeaders = exchange.getRequestHeaders();
                Set<String> keySet = requestHeaders.keySet();
                Iterator<String> iter = keySet.iterator();
                responseBody.write( "Hello World!\n\n".getBytes( "UTF-8" ) );
                while ( iter.hasNext() ) {
                    String key = iter.next();
                    List values = requestHeaders.get( key );
                    String s = key + " = " + values.toString() + "\n";
                    responseBody.write( s.getBytes() );
                }
                responseBody.close();
            }

        } );
        server.setExecutor( Executors.newCachedThreadPool() );
        server.start();
        System.out.println( "Server is listening on port " + port );
    }

    private static int findPort()
    {
        String env = System.getenv( "HTTP_PORT" );
        if ( env == null ) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.valueOf( env );
        } catch ( NumberFormatException ex ) {
            ex.printStackTrace();
            return DEFAULT_PORT;
        }
    }

}
