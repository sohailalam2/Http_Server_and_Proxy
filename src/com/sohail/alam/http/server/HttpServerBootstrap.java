package com.sohail.alam.http.server;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 8:32 PM
 */
public class HttpServerBootstrap {

    private static String welcomeScreen() {
        return "Welcome to HTTP Server & Proxy Server\n\n\n";
    }

    public static void main(String[] args) {

        System.out.println(welcomeScreen());

        // Load the properties
        ServerProperties.PROP.initialize();

        // Start the Http Server
        SetupServer.instance(ServerProperties.PROP.httpServerIP(), ServerProperties.PROP.httpServerPort())
                .initialize();
    }
}
