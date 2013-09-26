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

package com.sohail.alam.http.common.utils.watchservice;

import java.io.File;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 25/9/13
 * Time: 9:29 AM
 */
public interface WatchServiceCallback {

    /**
     * Exception caught.
     *
     * @param file  the file
     * @param cause the cause
     */
    public void exceptionCaught(File file, Throwable cause);

    /**
     * Already watching file.
     *
     * @param file the file
     */
    public void alreadyWatchingFile(File file);

    /**
     * The interface Directory service.
     */
    public interface DirectoryService extends WatchServiceCallback {
        /**
         * Directory modified.
         *
         * @param directory       the directory
         * @param oldLastModified the old last modified
         * @param newLastModified the new last modified
         */
        public void directoryModified(File directory, long oldLastModified, long newLastModified);

        /**
         * New file added.
         *
         * @param directory        the directory
         * @param oldNumberOfItems the old number of items
         * @param newNumberOfItems the new number of items
         */
        public void fileCountChanged(File directory, long oldNumberOfItems, long newNumberOfItems);

        /**
         * New directories added.
         *
         * @param directory        the directory
         * @param oldNumberOfItems the old number of items
         * @param newNumberOfItems the new number of items
         */
        public void directoryCountChanged(File directory, long oldNumberOfItems, long newNumberOfItems);

        /**
         * Directory no longer exists.
         *
         * @param directory       the directory
         * @param oldLastModified the old last modified
         */
        public void directoryNoLongerExists(File directory, long oldLastModified);
    }

    /**
     * The interface File service.
     */
    public interface FileService extends WatchServiceCallback {
        /**
         * File modified.
         *
         * @param file            the file
         * @param oldLastModified the old last modified
         * @param newLastModified the new last modified
         */
        public void fileModified(File file, long oldLastModified, long newLastModified);

        /**
         * File no longer exists.
         *
         * @param file         the file
         * @param lastModified the last modified
         */
        public void fileNoLongerExists(File file, long lastModified);
    }
}
