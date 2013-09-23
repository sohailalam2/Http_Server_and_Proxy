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
    // Server Configurations
    private String HTTP_SERVER_IP;
    private int HTTP_SERVER_PORT;
    // Networking Parameters
    private boolean TCP_NODELAY;
    private boolean SO_KEEPALIVE;
    private boolean SO_REUSEADDR;
    private int SO_BACKLOG;
    // Web App Directory and default file names/paths
    private String WEBAPP_PATH;
    private String DEFAULT_INDEX_PAGE;
    private String DEFAULT_404_PAGE;
    private String DEFAULT_500_PAGE;
    // HTTP Methods
    private boolean IS_CONNECT_ALLOWED;
    private boolean IS_DELETE_ALLOWED;
    private boolean IS_GET_ALLOWED;
    private boolean IS_HEAD_ALLOWED;
    private boolean IS_OPTIONS_ALLOWED;
    private boolean IS_PATCH_ALLOWED;
    private boolean IS_POST_ALLOWED;
    private boolean IS_PUT_ALLOWED;
    private boolean IS_TRACE_ALLOWED;

    public void initialize() {
        try {
            SERVER_PROPERTIES.load(new FileInputStream(new File("configurations/server.properties")));

            // Server Configurations
            // Default Http Server IP => Localhost IP
            String tempIP = SERVER_PROPERTIES.getProperty("HTTP_SERVER_IP", InetAddress.getLocalHost().getHostAddress()).trim();
            // TODO: Check for valid ip instead of null
            if (tempIP != null) {
                HTTP_SERVER_IP = tempIP;
            } else {
                HTTP_SERVER_IP = InetAddress.getLocalHost().getHostAddress();
            }
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

    public String httpServerIP() {
        return HTTP_SERVER_IP;
    }

    public int httpServerPort() {
        return HTTP_SERVER_PORT;
    }

    public boolean tcpNoDelay() {
        return TCP_NODELAY;
    }

    public void tcpNoDelay(boolean TCP_NODELAY) {
        this.TCP_NODELAY = TCP_NODELAY;
    }

    public boolean soKeepAlive() {
        return SO_KEEPALIVE;
    }

    public void soKeepAlive(boolean SO_KEEPALIVE) {
        this.SO_KEEPALIVE = SO_KEEPALIVE;
    }

    public boolean soReuseAddress() {
        return SO_REUSEADDR;
    }

    public void soReuseAddress(boolean SO_REUSEADDR) {
        this.SO_REUSEADDR = SO_REUSEADDR;
    }

    public int soBacklog() {
        return SO_BACKLOG;
    }

    public void soBacklog(int SO_BACKLOG) {
        this.SO_BACKLOG = SO_BACKLOG;
    }

    public String webappPath() {
        return WEBAPP_PATH;
    }

    public void webappPath(String path) {
        this.WEBAPP_PATH = path;
    }

    public boolean isConnectMethodAllowed() {
        return IS_CONNECT_ALLOWED;
    }

    public void isConnectMethodAllowed(boolean IS_CONNECT_ALLOWED) {
        this.IS_CONNECT_ALLOWED = IS_CONNECT_ALLOWED;
    }

    public boolean isDeleteMethodAllowed() {
        return IS_DELETE_ALLOWED;
    }

    public void isDeleteMethodAllowed(boolean IS_DELETE_ALLOWED) {
        this.IS_DELETE_ALLOWED = IS_DELETE_ALLOWED;
    }

    public boolean isGetMethodAllowed() {
        return IS_GET_ALLOWED;
    }

    public void isGetMethodAllowed(boolean IS_GET_ALLOWED) {
        this.IS_GET_ALLOWED = IS_GET_ALLOWED;
    }

    public boolean isHeadMethodAllowed() {
        return IS_HEAD_ALLOWED;
    }

    public void isHeadMethodAllowed(boolean IS_HEAD_ALLOWED) {
        this.IS_HEAD_ALLOWED = IS_HEAD_ALLOWED;
    }

    public boolean isOptionsMethodAllowed() {
        return IS_OPTIONS_ALLOWED;
    }

    public void isOptionsMethodAllowed(boolean IS_OPTIONS_ALLOWED) {
        this.IS_OPTIONS_ALLOWED = IS_OPTIONS_ALLOWED;
    }

    public boolean isPatchMethodAllowed() {
        return IS_PATCH_ALLOWED;
    }

    public void isPatchMethodAllowed(boolean IS_PATCH_ALLOWED) {
        this.IS_PATCH_ALLOWED = IS_PATCH_ALLOWED;
    }

    public boolean isPostMethodAllowed() {
        return IS_POST_ALLOWED;
    }

    public void isPostMethodAllowed(boolean IS_POST_ALLOWED) {
        this.IS_POST_ALLOWED = IS_POST_ALLOWED;
    }

    public boolean isPutMethodAllowed() {
        return IS_PUT_ALLOWED;
    }

    public void isPutMethodAllowed(boolean IS_PUT_ALLOWED) {
        this.IS_PUT_ALLOWED = IS_PUT_ALLOWED;
    }

    public boolean isTraceMethodAllowed() {
        return IS_TRACE_ALLOWED;
    }

    public void isTraceMethodAllowed(boolean IS_TRACE_ALLOWED) {
        this.IS_TRACE_ALLOWED = IS_TRACE_ALLOWED;
    }

    public String defaultIndexPage() {
        return this.DEFAULT_INDEX_PAGE;
    }

    public void defaultIndexPage(String path) {
        this.DEFAULT_INDEX_PAGE = path;
    }

    public String default404Page() {
        return this.DEFAULT_404_PAGE;
    }

    public void default404Page(String path) {
        this.DEFAULT_404_PAGE = path;
    }

    public String default500Page() {
        return this.DEFAULT_500_PAGE;
    }

    public void default500Page(String path) {
        this.DEFAULT_500_PAGE = path;
    }
}
