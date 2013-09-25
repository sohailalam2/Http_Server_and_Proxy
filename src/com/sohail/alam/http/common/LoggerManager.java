package com.sohail.alam.http.common;

import com.sohail.alam.http.server.ServerProperties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 9:25 PM
 */
public class LoggerManager {
    public static final Logger LOGGER;
    private static String currentLogLevel;

    static {
        LOGGER = LogManager.getLogger("LoggerManager");
        setCurrentLogLevel(ServerProperties.PROP.DEFAULT_LOG_LEVEL);
    }

    /**
     * Private Constructor
     */
    private LoggerManager() {
    }

    /**
     * Gets current log level.
     *
     * @return the current log level
     */
    public static String getCurrentLogLevel() {
        return LoggerManager.currentLogLevel;
    }

    /**
     * Sets current log level.
     *
     * @param newLogLevel the new log level
     *
     * @return the current log level
     */
    public static boolean setCurrentLogLevel(String newLogLevel) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.toLevel(newLogLevel, Level.ERROR));
        ctx.updateLoggers();
        LoggerManager.currentLogLevel = loggerConfig.getLevel().toString();
        if (LoggerManager.currentLogLevel.equalsIgnoreCase(newLogLevel)) {
            LOGGER.log(Level.toLevel(LoggerManager.currentLogLevel), "Logger Level was changed to => " + LoggerManager.currentLogLevel);
            return true;
        } else {
            return false;
        }
    }

}
