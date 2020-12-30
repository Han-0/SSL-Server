# SSL-Server
Basic Java implementation of an SSL server-client exchange

Limitations/dependencies: 
The server and client source files are intended to run on two separate machines. 
Running both programs on a single machine is not supported.
The server program is limited to a single client connection at one time.

NOTE:
These programs require the creation of a keystore/truststore in compliance with JSSE (Java Secure Socket Extension).
For information regarding creating these refer to the JSSE Reference Guide (link below).

https://docs.oracle.com/javase/9/security/java-secure-socket-extension-jsse-reference-guide.htm#JSSEC-GUID-3D26386B-BC7A-41BB-AC70-80E6CD147D6F

Usage: 
1. Both programs contain lines similar to the following (in the "server" program you will see "keyStore" instead of "trustStore"): 

   System.setProperty("javax.net.ssl.trustStore", "...");
   
   System.setProperty("javax.net.ssl.trustStorePassword", "...");
   
   Replace '...' with the respective name and password of your trusted cert entry.

2. First compile and execute server.java and follow the terminal prompts.
3. Then compile and execute client.java and follow the terminal prompts.
