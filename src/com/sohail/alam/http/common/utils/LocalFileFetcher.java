package com.sohail.alam.http.common.utils;

import com.sohail.alam.http.common.utils.php.PhpProcessor;
import com.sohail.alam.http.common.utils.php.PhpProcessorCallback;
import com.sohail.alam.http.server.ServerProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;
import static com.sohail.alam.http.server.ServerProperties.PROP;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 8:37 PM
 */
public class LocalFileFetcher {
    public static final LocalFileFetcher FETCHER = new LocalFileFetcher();

    /**
     * Instantiates a new Local file fetcher.
     */
    private LocalFileFetcher() {
        LOGGER.trace("LocalFileFetcher Constructor Initialized");
    }

    /**
     * Parse file type.
     *
     * @param pathInfo the path info
     *
     * @return the string
     */
    private String parseFileType(String pathInfo) {
        String fileName = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * Normalize a given relative path String to get the actual path String
     *
     * @param path the path
     *
     * @return the normalized path
     */
    private String normalizePath(String path) {
        String normalizedPath = new String();
        // Make Sure Path Starts with a slash (/)
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        // If path ends with a "/" then append the default page to it (Eg. index.html)
        if (path.endsWith("/")) {
            path = path + PROP.DEFAULT_INDEX_PAGE;
        }
        // ./www/somePath
        if (path.startsWith("/" + ServerProperties.PROP.WEBAPP_PATH)) {
            normalizedPath = "." + path;
        } else {
            normalizedPath = "./www" + path;
        }
        LOGGER.debug("Normalizing Path '{}' to '{}'", path, normalizedPath);
        return normalizedPath;
    }

    /**
     * Process php file.
     *
     * @param phpFilePath the php file path
     * @param callback    the callback
     */
    private <T extends LocalFileFetcherCallback> void processPhpFile(final String phpFilePath, final T callback) {
        PhpProcessor.processPhp(phpFilePath, new PhpProcessorCallback() {
            @Override
            public void success(String fileName, byte[] data, int dataLength) {
                LOGGER.debug("Successfully processed PHP File '{}' - {} bytes", fileName, dataLength);
                callback.fetchSuccess(phpFilePath, data, MediaType.getType(".html"), dataLength);
            }

            @Override
            public void failure(String fileName, byte[] data, int dataLength) {
                LOGGER.debug("Failed to processed PHP File '{}' - {} bytes", fileName, dataLength);
                callback.fetchSuccess(phpFilePath, data, MediaType.getType(".html"), dataLength);
            }

            @Override
            public void exceptionCaught(String fileName, Throwable cause) {
                LOGGER.debug("Exception Caught while processing PHP File '{}': ", fileName, cause.getMessage());
                callback.exceptionCaught(phpFilePath, cause);
            }
        });
    }

    /**
     * Get bytes of the file.
     *
     * @param path     the relative path of the file, which will be normalized automatically
     * @param callback the callback
     *
     * @return the byte[] containing the file data
     */
    public <T extends LocalFileFetcherCallback> void fetch(String path, T callback) {
        final byte[] fileBytes;
        final File file = new File(this.normalizePath(path));
        FileInputStream is = null;

        try {
            // If the file referred is a PHP File then
            // parse using PHP Processor, if PHP Processing is enabled
            // FileNotFound is taken care of here instead of PhpProcessor
            if (PROP.ENABLE_PHP && file.getName().endsWith(".php")) {
                processPhpFile(file.getAbsolutePath(), callback);
            } else {
                is = new FileInputStream(file);
                fileBytes = new byte[is.available()];
                int length = is.read(fileBytes);
                LOGGER.debug("File '{}' Fetched Successfully - {} bytes", path, length);
                callback.fetchSuccess(path, fileBytes, MediaType.getType(parseFileType(file.getName())), length);
            }
        } catch (FileNotFoundException e) {
            LOGGER.debug("Exception Caught: {}", e.getMessage());
            callback.fileNotFound(path, e);
        } catch (IOException e) {
            LOGGER.debug("Exception Caught: {}", e.getMessage());
            callback.exceptionCaught(path, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                    LOGGER.debug("IO Exception Caught while closing Input Stream in finally, Nothing can be done here");
                }
            }
        }
    }
}
