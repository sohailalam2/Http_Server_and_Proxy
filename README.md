# Http Server & Proxy Project

The Http Server & Proxy is a simple Web Server written in Java that servers web pages,
quickly and efficiently.

* It caches the file whenever a fresh new request comes for that file and later on servers the same from the Cache.
* It also watches for any file change and automatically updates the Cache whenever that particular file is changed.
* Automatically identifies the Content-Type and calculates the Content-Length.
* It is capable of serving PHP files (with very limited stateless/session-less features).

It is an ongoing project. Please feel free to contribute to this Project and make it even better.

### Usage:

Have you ever wanted to work with JavaScript XMLHttpRequest (XHR) but could not work it without
a localhost server or web server, well Http Server & Proxy is a low weight solution to
your problems.

### Why the Proxy in the name?

Well as I said, it is a work in progress and you will soon see a working Proxy Server
embedded in it.

### What's the use of a Proxy Server you ask...

Try XMLHttpRequest (XHR) to open http://www.google.com ... Ya Ya... I know you can't.
You cant do Cross Domain Request (for that you need Cross Domain Resource Sharing to be handled at the Server end).
Well... a proxy server can save you from it...

Stay tuned to know how... or help me develop it...



## How to build

You require the following to build the Http Server & Proxy:

* Latest stable [Oracle JDK 7](http://www.oracle.com/technetwork/java/)
* Latest stable [Apache Ant](http://ant.apache.org/)

Note that this is build-time requirement.  JDK 6 is enough to run your Http Server & Proxy based application.
