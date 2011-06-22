# jsw-maven-plugin ![Project status](http://stillmaintained.com/eskatos/jsw-maven-plugin.png)

# Snippet

	<build>
	  <plugins>
		<groupId>org.codeartisans</groupId>
		<artifactId>jsw-maven-plugin</artifactId>
		<version>1.0</version>
	  </plugins>
	</build>

# Configuration

	<configuration>
	  
	  <skip>false</skip>
	  
	  <outputDirectory>${project.build.directory}/jsw</outputDirectory>
	  <configDirname>etc</configDirname>
	  <configDirInClasspath>true</configDirInClasspath>
	  <copyConfigResources>true</copyConfigResources>
	  <configResourcesDir>${project.basedir}/src/main/config</configResourcesDir>
	  <repositoryLayout>flat</repositoryLayout>
	  
	  <globals>
		
		<environments>
		  <key>value</key>
		</environments>
		
		<wrapperDebug>false</wrapperDebug>
		<wrapperJmx>true</wrapperJmx>
		<wrapperConsoleLogFormat>PM</wrapperConsoleLogFormat>
		<wrapperConsoleLogLevel>INFO</wrapperConsoleLogLevel>
		<wrapperLogfileFormat>LPTM</wrapperLogfileFormat>
		<wrapperLogfileLevel>INFO</wrapperLogfileLevel>
		<wrapperLogfileMaxSize>0</wrapperLogfileMaxSize>
		<wrapperLogfileMaxFiles>0</wrapperLogfileMaxFiles>
		<wrapperSyslogLogLevel>NONE</wrapperSyslogLogLevel>
		
		<javaAutoBits>true</javaAutoBits>
		<javaLogGeneratedCommand>false</javaLogGeneratedCommand>
		<javaArguments>
		  <javaArgument>arg0</javaArgument>
		</javaArguments>
		<javaLibraryPaths>
		  <javaLibraryPath>../lib</javaLibraryPath>
		</javaArguments>
		<javaClassPaths>
		  <javaClassPath>../lib/wrapper.jar</javaClassPath>
		</javaClassPaths>
		
	  </globals>
	  
	  <services>
		
		<service>
		  ...
		  <wrapperLogfile></wrapperLogfile>
		  ...
		  <appName></appName>
		  <appMainClass></appMainClass>
		  <appArguments>
		    <appArgument>arg0</appArgument>
		  </appArguments>
		  
		  <daemonName></daemonName>
		  
		</service>
		
	  </services>
	  
	</configuration>


[![I use this][2]][1]

[![Flattr this][4]][3]

[1]: https://www.ohloh.net/p/jsw-maven-plugin
[2]: https://www.ohloh.net/images/stack/iusethis/static_logo.png
[3]: https://flattr.com/thing/319117/jsw-maven-plugin
[4]: http://api.flattr.com/button/flattr-badge-large.png


## Changelog

### master

* Documentation in progress
* Initial import of working version
