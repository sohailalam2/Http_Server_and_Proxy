package com.sohail.alam.http.server;

import com.sohail.alam.http.common.Constants;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 8:32 PM
 */
public class HttpServerBootstrap {

    public static void main(String[] args) {

        System.err.println(Constants.welcomeScreen());

        // Load the properties
        ServerProperties.PROP.initialize();

        // Start the Http Server
        SetupServer.instance(ServerProperties.PROP.HTTP_SERVER_IP, ServerProperties.PROP.HTTP_SERVER_PORT)
                .initialize();
    }
}
