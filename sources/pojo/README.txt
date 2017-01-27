This is the Readme for the Borhan Java POJO API Client Library.
You should read this before setting up the client in eclipse.

== CONTENTS OF THIS PACKAGE ==

 - Borhan Java POJO Client Library API (/src/com)
 - Compilation and Run test script (/src/Borhan.java)
 - JUnit tests (/src/com/borhan/client/test)
 - lib (JARs required to build the client library)


== DEPENDENCIES ==
 
The API depends on these libraries:
 - Apache Commons HTTP Client 3.1 (legacy): http://hc.apache.org/downloads.cgi
 - Log4j: http://logging.apache.org/log4j/1.2/download.html
 - Apache Commons Logging 1.1: http://commons.apache.org/downloads/download_logging.cgi
 - Apache Commons Codec 1.4: http://commons.apache.org/codec/download_codec.cgi
 - JUnit 3.8.2 (optional): http://sourceforge.net/projects/junit/files/junit/

 
== BUILDING FROM SOURCE ==

To build the API:
 - Setup the project in eclipse.
 - Build the project


== TESTING THE API CLIENT LIBRARY ==

To run the main class (Borhan.java):
 - Edit the /src/BorhanTestConfig.java file, enter valid data to PARTNER_ID, SECRET and ADMIN_SECRET variables.
 - Compile the client library.
 - Right click the Borhan.java file and choose Debug As > Java Application.

To run the JUnit test suite that accompanies this source:
 - Edit the /src/BorhanTestConfig.java file, enter valid data to PARTNER_ID, SECRET and ADMIN_SECRET variables.
 - Compile the client library.
 - Right click the BorhanTestSuite.java file and choose Debug As > JUnit Test.
  
== SETUP log4j LOGGING IN ECLIPSE ==

The launch settings are saved in the following files:
- 1. BorhanTestSuite.launch (the JUnit tests)
- 2. BorhanMainTest.launch (A main test class for quickly testing the build)

There is a log4j.properties file in /src/log4j. 
 - Edit it to set the log level as desired.