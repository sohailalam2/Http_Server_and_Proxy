package com.sohail.alam.http.common.utils;

/**
 * The interface Local file fetcher callback which must be used to receive callbacks
 * <p/>
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 25/9/13
 * Time: 12:28 AM
 */
public interface LocalFileFetcherCallback {
    /**
     * Fetch success is called when a file is read successfully and
     * the data is ready to be delivered.
     *
     * @param path       the path from which the file was read (Normalized Path)
     * @param data       the data as byte array
     * @param mediaType  the media type
     * @param dataLength the data length
     */
    public void fetchSuccess(String path, byte[] data, String mediaType, long dataLength);

    /**
     * Fetch failed is called whenever there was an error reading the file
     *
     * @param path  the path from which the file was to be read (Normalized Path)
     * @param cause the throwable object containing the cause
     */
    public void fileNotFound(String path, Throwable cause);

    /**
     * Exception caught.
     *
     * @param path  the path
     * @param cause the cause
     */
    public void exceptionCaught(String path, Throwable cause);
}