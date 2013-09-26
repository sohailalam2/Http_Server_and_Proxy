/*
 * Copyright 2013 The Http Server & Proxy
 *
 *  The File Watch Service Project licenses this file to you under the Apache License, version 2.0 (the "License");
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

package com.sohail.alam.http.common.smartcache;

import com.sohail.alam.http.common.utils.watchservice.WatchService;
import com.sohail.alam.http.common.utils.watchservice.WatchServiceCallback;
import com.sohail.alam.http.common.utils.watchservice.WatchServiceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 25/9/13
 * Time: 8:56 PM
 */
public class SmartCache<T extends SmartCachePojo> {

    private final Map<String, T> CACHE = new ConcurrentHashMap<String, T>();
    private final WatchService WATCH_SERVICE;
    public AtomicLong cacheSizeInBytes;


    private SmartCache() {
        LOGGER.trace("Smart Cache initialized");
        cacheSizeInBytes = new AtomicLong(0);

        WATCH_SERVICE = WatchService.watcher();
        try {
            WATCH_SERVICE.addCallbackListener(new FileWatchServiceCallback());
        } catch (WatchServiceException e) {
            LOGGER.debug("Error Adding File Watch Service Callback", e);
        }
    }

    public static SmartCache cache() {
        return SingletonHolder.INSTANCE;
    }

    public long size() {
        return CACHE.size();
    }

    public void put(String absFileName, T data) {
        if (CACHE.get(absFileName) == null) {
            CACHE.put(absFileName, data);
            cacheSizeInBytes.set(cacheSizeInBytes.get() + data.fileSize);
            LOGGER.debug("Data of Size: {}, Last Modified: {}, added to Smart Cache => {}",
                    data.fileSize, data.lastModified, absFileName);

            WATCH_SERVICE.watch(data.file);
        }
    }

    public T get(String absFileName) {
        return CACHE.get(absFileName);
    }

    public T remove(String absFileName) {
        if (CACHE.get(absFileName) != null) {
            T data = CACHE.remove(absFileName);
            cacheSizeInBytes.set(cacheSizeInBytes.get() - data.fileSize);
            LOGGER.debug("Data of Size: {}, Last Modified: {}, removed from Smart Cache => {}",
                    data.fileSize, data.lastModified, absFileName);
            return data;
        }
        return null;
    }

    private interface SingletonHolder {
        public static final SmartCache INSTANCE = new SmartCache();
    }

    /**
     * The type File watch service callback.
     */
    private class FileWatchServiceCallback implements WatchServiceCallback.FileService {

        /**
         * File modified.
         *
         * @param file            the file
         * @param oldLastModified the old last modified
         * @param newLastModified the new last modified
         */
        @Override
        public void fileModified(File file, long oldLastModified, long newLastModified) {
            LOGGER.debug("File {} was modified", file.getAbsoluteFile());

            T pojo = CACHE.get(file.getAbsolutePath());

            try {
                FileInputStream is = new FileInputStream(file);
                pojo.fileData = new byte[is.available()];
                pojo.fileSize = is.read(pojo.fileData);
                pojo.lastModified = newLastModified;
                CACHE.put(file.getAbsolutePath(), pojo);
                cacheSizeInBytes.set(cacheSizeInBytes.get() + pojo.fileSize);
                LOGGER.debug("Data of Size: {}, Last Modified: {}, updated into Smart Cache => {}",
                        pojo.fileSize, pojo.lastModified, file.getAbsolutePath());
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }

        /**
         * File no longer exists.
         *
         * @param file         the file
         * @param lastModified the last modified
         */
        @Override
        public void fileNoLongerExists(File file, long lastModified) {
            LOGGER.debug("File {} no longer exists", file.getAbsoluteFile());
            SmartCache.cache().remove(file.getAbsolutePath());
        }

        /**
         * Exception caught.
         *
         * @param file  the file
         * @param cause the cause
         */
        @Override
        public void exceptionCaught(File file, Throwable cause) {
            LOGGER.debug("Exception Caught in FileWatchServiceCallback: ", cause);
        }

        /**
         * Already watching file.
         *
         * @param file the file
         */
        @Override
        public void alreadyWatchingFile(File file) {
            // ignored
        }
    }
}
