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

package com.sohail.alam.http.common.watchservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 25/9/13
 * Time: 8:56 AM
 */
public class WatchService {

    public long repeatInterval = 1;
    public TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * Private Constructor
     */
    private WatchService() {

    }

    /**
     * Watcher watch service.
     *
     * @return the watch service
     */
    public static WatchService watcher() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Add callback listener.
     *
     * @param listener the listener
     *
     * @throws WatchServiceException the watch service exception
     */
    public <T extends WatchServiceCallback> void addCallbackListener(T listener) throws WatchServiceException {
        // If the listener to be added implements DirectoryService then add it to DIR_CALLBACK_LISTENERS
        if (listener instanceof WatchServiceCallback.DirectoryService) {
            SingletonHolder.DIR_CALLBACK_LISTENERS.get().add((WatchServiceCallback.DirectoryService) listener);
        }
        // If the listener to be added implements FileService then add it to FILE_CALLBACK_LISTENERS
        else if (listener instanceof WatchServiceCallback.FileService) {
            SingletonHolder.FILE_CALLBACK_LISTENERS.get().add((WatchServiceCallback.FileService) listener);
        }
        // If the listener to be added simply implements WatchServiceCallback, then throw Exception
        else {
            throw new WatchServiceException("Listener MUST either implement DirectoryService or FileService type of WatchServiceCallback");
        }
    }

    /**
     * Remove the appropriate callback listener.
     *
     * @param listener the listener
     */
    public <T extends WatchServiceCallback> void removeCallbackListener(T listener) {
        if (listener instanceof WatchServiceCallback.DirectoryService) {
            SingletonHolder.DIR_CALLBACK_LISTENERS.get().remove(listener);
        } else if (listener instanceof WatchServiceCallback.FileService) {
            SingletonHolder.FILE_CALLBACK_LISTENERS.get().remove(listener);
        }
    }

    /**
     * Start watching a particular file.
     * <p/>
     * Here the reference to 'file' can be equally understood as a 'file' or 'directory'.
     * <p/>
     * To receive any callback events, the callback handlers MUST be
     * added BEFORE starting to watch service for the file.
     * <p/>
     * If the file to be watched already has a watch service running then a
     * {@link WatchServiceCallback#alreadyWatchingFile(java.io.File)} callback
     * is provided.
     * <p/>
     * This is a fail first kind of method.
     * <p/>
     * If the file which is to be watched does not exists at the time of execution of this method,
     * then an {@link WatchServiceCallback#exceptionCaught(java.io.File, Throwable)}
     * callback will be provided. You will NOT be notified further about this
     * non existing file, however, if the file existed at the time of starting the watch service
     * and was deleted/moved after starting the watch service, then an appropriate callback is provided -
     * {@link WatchServiceCallback.DirectoryService#directoryNoLongerExists(java.io.File, long)} or
     * {@link WatchServiceCallback.FileService#fileNoLongerExists(java.io.File, long)}.
     *
     * @param file the file/directory
     */
    public void watch(File file) {
        if (!file.exists()) {
            provideCallbackExceptionCaught(file, new FileNotFoundException("The file does not exists: " + file.getName()));
            return;
        }
        if (SingletonHolder.WATCHER_MAP.get(file) == null) {
            SingletonHolder.WATCHER_MAP.put(file, file.lastModified());
            startWatching(file);
        } else {
            provideCallbackAlreadyWatching(file);
        }
    }

    /**
     * Gets directory service callback listeners.
     *
     * @return the directory service callback listeners
     */
    private List<WatchServiceCallback.DirectoryService> getDirServiceCallbackListeners() {
        return SingletonHolder.DIR_CALLBACK_LISTENERS.get();
    }

    /**
     * Gets file service callback listeners.
     *
     * @return the file service callback listeners
     */
    private List<WatchServiceCallback.FileService> getFileServiceCallbackListeners() {
        return SingletonHolder.FILE_CALLBACK_LISTENERS.get();
    }

    /**
     * Start watching.
     *
     * @param file the file
     */
    private void startWatching(File file) {
        if (file.isFile()) {
            ScheduledFuture future = SingletonHolder.WATCH_SERVICE.
                    scheduleAtFixedRate(new FileWatchTask(file), 0, repeatInterval, timeUnit);
            SingletonHolder.WATCH_SERVICE_FUTURE.put(file, future);
        } else if (file.isDirectory()) {
            ScheduledFuture future = SingletonHolder.WATCH_SERVICE.
                    scheduleAtFixedRate(new DirWatchTask(file), 0, repeatInterval, timeUnit);
            SingletonHolder.WATCH_SERVICE_FUTURE.put(file, future);
        }
    }

    /**
     * Provide callback already watching.
     *
     * @param file the file
     */
    private void provideCallbackAlreadyWatching(File file) {
        if (file.isDirectory()) {
            for (WatchServiceCallback callback : getDirServiceCallbackListeners()) {
                callback.alreadyWatchingFile(file);
            }
        } else if (file.isFile()) {
            for (WatchServiceCallback callback : getFileServiceCallbackListeners()) {
                callback.alreadyWatchingFile(file);
            }
        }
    }

    /**
     * Provide callback exception caught.
     *
     * @param file  the file
     * @param cause the cause
     */
    private void provideCallbackExceptionCaught(File file, Throwable cause) {
        if (file.isDirectory()) {
            for (WatchServiceCallback callback : getDirServiceCallbackListeners()) {
                callback.exceptionCaught(file, cause);
            }
        } else if (file.isFile()) {
            for (WatchServiceCallback callback : getFileServiceCallbackListeners()) {
                callback.exceptionCaught(file, cause);
            }
        }
    }

    /**
     * Provide callback directory modified.
     *
     * @param directory       the directory
     * @param oldLastModified the old last modified
     * @param newLastModified the new last modified
     */
    private void provideCallbackDirectoryModified(File directory, long oldLastModified, long newLastModified) {
        for (WatchServiceCallback.DirectoryService callback : getDirServiceCallbackListeners()) {
            callback.directoryModified(directory, oldLastModified, newLastModified);
        }
    }

    /**
     * Provide callback directory no longer exists.
     *
     * @param file            the file
     * @param oldLastModified the old last modified
     */
    private void provideCallbackDirectoryNoLongerExists(File file, long oldLastModified) {
        for (WatchServiceCallback.DirectoryService callback : getDirServiceCallbackListeners()) {
            callback.directoryNoLongerExists(file, oldLastModified);
        }
    }

    /**
     * Provide callback new directory added.
     *
     * @param file             the file
     * @param oldNumberOfItems the old number of items
     * @param newNumberOfItems the new number of items
     */
    private void provideCallbackDirectoryCountChanged(File file, long oldNumberOfItems, long newNumberOfItems) {
        for (WatchServiceCallback.DirectoryService callback : getDirServiceCallbackListeners()) {
            callback.directoryCountChanged(file, oldNumberOfItems, newNumberOfItems);
        }
    }

    /**
     * Provide callback new file added.
     *
     * @param file             the file
     * @param oldNumberOfItems the old number of items
     * @param newNumberOfItems the new number of items
     */
    private void provideCallbackFileCountChanged(File file, long oldNumberOfItems, long newNumberOfItems) {
        for (WatchServiceCallback.DirectoryService callback : getDirServiceCallbackListeners()) {
            callback.fileCountChanged(file, oldNumberOfItems, newNumberOfItems);
        }
    }

    /**
     * Provide callback file modified.
     *
     * @param file            the file
     * @param oldLastModified the old last modified
     * @param newLastModified the new last modified
     */
    private void provideCallbackFileModified(File file, long oldLastModified, long newLastModified) {
        for (WatchServiceCallback.FileService callback : getFileServiceCallbackListeners()) {
            callback.fileModified(file, oldLastModified, newLastModified);
        }
    }

    /**
     * Provide callback file no longer exists.
     *
     * @param file         the file
     * @param lastModified the last modified
     */
    private void provideCallbackFileNoLongerExists(File file, long lastModified) {
        for (WatchServiceCallback.FileService callback : getFileServiceCallbackListeners()) {
            callback.fileNoLongerExists(file, lastModified);
        }
    }

    /**
     * The interface Singleton holder.
     */
    private static interface SingletonHolder {
        public static final WatchService INSTANCE
                = new WatchService();
        public static final ScheduledThreadPoolExecutor WATCH_SERVICE
                = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
        public static final Map<File, ScheduledFuture> WATCH_SERVICE_FUTURE
                = new HashMap<File, ScheduledFuture>();
        public static final Map<File, Long> WATCHER_MAP
                = new ConcurrentHashMap<File, Long>();
        public static final AtomicReference<List<WatchServiceCallback.DirectoryService>> DIR_CALLBACK_LISTENERS
                = new AtomicReference<List<WatchServiceCallback.DirectoryService>>(new ArrayList<WatchServiceCallback.DirectoryService>());
        public static final AtomicReference<List<WatchServiceCallback.FileService>> FILE_CALLBACK_LISTENERS
                = new AtomicReference<List<WatchServiceCallback.FileService>>(new ArrayList<WatchServiceCallback.FileService>());
    }

    /**
     * The type Dir watch task.
     */
    private class DirWatchTask implements Runnable {
        private File file;
        private Long oldLastModified;
        private long numberOfDirItems = 0;
        private long numberOfFileItems = 0;

        /**
         * Instantiates a new Dir watch task.
         *
         * @param file the file
         */
        public DirWatchTask(File file) {
            this.file = file;
            this.oldLastModified = file.lastModified();

            countItems();
        }

        /**
         * Count items.
         */
        private void countItems() {
            File[] all = this.file.listFiles();
            if (all != null) {
                this.numberOfDirItems = 0;
                this.numberOfFileItems = 0;
                for (File file : all) {
                    if (file.isDirectory())
                        this.numberOfDirItems++;
                    else if (file.isFile())
                        this.numberOfFileItems++;
                }
            } else {
                this.numberOfDirItems = 0;
                this.numberOfFileItems = 0;
            }
        }

        /**
         * Run void.
         */
        @Override
        public void run() {
            if (file.exists()) {
                long newLastModified = file.lastModified();
                long oldNumberOfDirItems = this.numberOfDirItems;
                long oldNumberOfFileItems = this.numberOfFileItems;

                if ((oldLastModified = SingletonHolder.WATCHER_MAP.get(this.file)) != null) {
                    if (newLastModified > oldLastModified) {
                        SingletonHolder.WATCHER_MAP.put(file, newLastModified);
                        WatchService.watcher().provideCallbackDirectoryModified(file, oldLastModified, newLastModified);
                        countItems();
                        if (oldNumberOfDirItems != this.numberOfDirItems)
                            WatchService.watcher().provideCallbackDirectoryCountChanged(file, oldNumberOfDirItems, this.numberOfDirItems);
                        if (oldNumberOfFileItems != this.numberOfFileItems)
                            WatchService.watcher().provideCallbackFileCountChanged(file, oldNumberOfFileItems, this.numberOfFileItems);
                    }
                } else {
                    WatchService.watcher().provideCallbackExceptionCaught(file,
                            new WatchServiceException("Directory seems to be missing from WatchService!"));
                }
            } else {
                WatchService.watcher().provideCallbackDirectoryNoLongerExists(file, oldLastModified);
                SingletonHolder.WATCHER_MAP.remove(file);
                SingletonHolder.WATCH_SERVICE_FUTURE.remove(file).cancel(false);
                SingletonHolder.WATCH_SERVICE.remove(this);
                SingletonHolder.WATCH_SERVICE.purge();
            }
        }
    }

    /**
     * The type File watch task.
     */
    private class FileWatchTask implements Runnable {

        private File file;
        private Long oldLastModified;

        /**
         * Instantiates a new File watch task.
         *
         * @param file the file
         */
        public FileWatchTask(File file) {
            this.file = file;
            this.oldLastModified = file.lastModified();
            LOGGER.debug("Watching File for modification: {}", file.getAbsoluteFile());
        }

        /**
         * Run void.
         */
        @Override
        public void run() {
            if (file.exists()) {
                long newLastModified = file.lastModified();
                if ((oldLastModified = SingletonHolder.WATCHER_MAP.get(this.file)) != null) {
                    if (newLastModified > oldLastModified) {
                        SingletonHolder.WATCHER_MAP.put(file, newLastModified);
                        WatchService.watcher().provideCallbackFileModified(file, oldLastModified, newLastModified);
                    }
                } else {
                    WatchService.watcher().provideCallbackExceptionCaught(file,
                            new WatchServiceException("File seems to be missing from WatchService!"));
                }
            } else {
                WatchService.watcher().provideCallbackFileNoLongerExists(file, oldLastModified);
                SingletonHolder.WATCHER_MAP.remove(file);
                SingletonHolder.WATCH_SERVICE_FUTURE.remove(file).cancel(false);
                SingletonHolder.WATCH_SERVICE.remove(this);
                SingletonHolder.WATCH_SERVICE.purge();
            }
        }
    }
}
