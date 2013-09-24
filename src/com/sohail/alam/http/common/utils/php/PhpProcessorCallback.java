package com.sohail.alam.http.common.utils.php;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 24/9/13
 * Time: 9:39 PM
 */
public interface PhpProcessorCallback {

    /**
     * Success void.
     *
     * @param fileName   the file name
     * @param data       the data
     * @param dataLength the data length
     */
    public void success(String fileName, byte[] data, int dataLength);

    /**
     * Failure void.
     *
     * @param fileName   the file name
     * @param data       the data
     * @param dataLength the data length
     */
    public void failure(String fileName, byte[] data, int dataLength);

    /**
     * File not found.
     *
     * @param fileName the file name
     * @param cause    the cause
     */
    public void fileNotFound(String fileName, Throwable cause);

    /**
     * Exception caught.
     *
     * @param fileName the file name
     * @param cause    the cause
     */
    public void exceptionCaught(String fileName, Throwable cause);

}