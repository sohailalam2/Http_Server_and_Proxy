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

package com.sohail.alam.http.common.php;

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