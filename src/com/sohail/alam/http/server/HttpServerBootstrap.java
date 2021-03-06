/*
 * Copyright 2013 The Http Server & Proxy
 *
 *  The Http Server & Proxy Project licenses this file to you under the Apache License, version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *               http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

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
