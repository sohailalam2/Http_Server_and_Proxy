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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 8:33 PM
 */
public class ServerProperties {
    public static final ServerProperties PROP = new ServerProperties();
    private static final Properties SERVER_PROPERTIES = new Properties();
    private final Logger LOGGER = LogManager.getLogger("ServerProperties");
    public String DEFAULT_LOG_LEVEL;
    // Server Configurations
    public String HTTP_SERVER_IP;
    public int HTTP_SERVER_PORT;
    // Php Service
    public boolean ENABLE_PHP;
    public String PHP_INSTALL_PATH;
    // Networking Parameters
    public boolean TCP_NODELAY;
    public boolean SO_KEEPALIVE;
    public boolean SO_REUSEADDR;
    public int SO_BACKLOG;
    // Web App Directory and default file names/paths
    public String WEBAPP_PATH;
    public String DEFAULT_INDEX_PAGE;
    public String DEFAULT_404_PAGE;
    public String DEFAULT_500_PAGE;
    // HTTP Methods
    public boolean IS_CONNECT_ALLOWED;
    public boolean IS_DELETE_ALLOWED;
    public boolean IS_GET_ALLOWED;
    public boolean IS_HEAD_ALLOWED;
    public boolean IS_OPTIONS_ALLOWED;
    public boolean IS_PATCH_ALLOWED;
    public boolean IS_POST_ALLOWED;
    public boolean IS_PUT_ALLOWED;
    public boolean IS_TRACE_ALLOWED;

    public void initialize() {
        try {
            SERVER_PROPERTIES.load(new FileInputStream(new File("configurations/server.properties")));

            // Default Log Level
            DEFAULT_LOG_LEVEL = SERVER_PROPERTIES.getProperty("DEFAULT_LOG_LEVEL", "ERROR").trim();

            // Server Configurations
            // Default Http Server IP => Localhost IP
            String tempIP = SERVER_PROPERTIES.getProperty("HTTP_SERVER_IP", InetAddress.getLocalHost().getHostAddress()).trim();
            // TODO: Check for valid ip instead of null
            if (tempIP != null) {
                HTTP_SERVER_IP = tempIP;
            } else {
                HTTP_SERVER_IP = InetAddress.getLocalHost().getHostAddress();
            }

            // Php Service
            ENABLE_PHP = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("ENABLE_PHP", "true").trim());
            PHP_INSTALL_PATH = SERVER_PROPERTIES.getProperty("PHP_INSTALL_PATH", "php");

            // Default Http Server Port => 8080
            int parsedPort = Integer.parseInt(SERVER_PROPERTIES.getProperty("HTTP_SERVER_PORT", "8080").trim());
            if (parsedPort > 0 && parsedPort < 65536) {
                HTTP_SERVER_PORT = parsedPort;
            } else {
                LOGGER.error("Out of bound port number => {}. Setting it to 8080", parsedPort);
                HTTP_SERVER_PORT = 8080;
            }

            // Networking Parameters
            TCP_NODELAY = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("TCP_NODELAY", "true").trim());
            SO_KEEPALIVE = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("SO_KEEPALIVE", "true").trim());
            SO_REUSEADDR = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("SO_REUSEADDR", "true").trim());
            SO_BACKLOG = Integer.parseInt(SERVER_PROPERTIES.getProperty("SO_BACKLOG", "65536").trim());

            // Web App Directory and default file names/paths
            WEBAPP_PATH = SERVER_PROPERTIES.getProperty("WEBAPP_PATH", "www").trim();
            DEFAULT_INDEX_PAGE = SERVER_PROPERTIES.getProperty("DEFAULT_INDEX_PAGE", "index.html").trim();
            DEFAULT_404_PAGE = SERVER_PROPERTIES.getProperty("DEFAULT_404_PAGE", "www/404.html").trim();
            DEFAULT_500_PAGE = SERVER_PROPERTIES.getProperty("DEFAULT_500_PAGE", "www/500.html").trim();

            // HTTP Methods
            IS_CONNECT_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_CONNECT_ALLOWED", "false").trim());
            IS_DELETE_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_DELETE_ALLOWED", "false").trim());
            IS_GET_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_GET_ALLOWED", "false").trim());
            IS_HEAD_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_HEAD_ALLOWED", "false").trim());
            IS_OPTIONS_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_OPTIONS_ALLOWED", "false").trim());
            IS_POST_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_POST_ALLOWED", "false").trim());
            IS_PATCH_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_PATCH_ALLOWED", "false").trim());
            IS_PUT_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_PUT_ALLOWED", "false").trim());
            IS_TRACE_ALLOWED = Boolean.parseBoolean(SERVER_PROPERTIES.getProperty("IS_TRACE_ALLOWED", "false").trim());

            LOGGER.info("Server Properties loaded Successfully");
        }
        // TODO: Logger - Exception handling
        catch (FileNotFoundException e) {
            LOGGER.fatal("Configuration File Not Found: {}", e.getMessage());
        } catch (UnknownHostException e) {
            LOGGER.fatal("Problem with Server IP: {}", e.getMessage());
        } catch (IOException e) {
            LOGGER.fatal("Problem Reading Configuration File: {}", e.getMessage());
        } catch (NumberFormatException e) {
            LOGGER.fatal("Input must be an Integer: {}", e.getMessage());
        }
    }
}
