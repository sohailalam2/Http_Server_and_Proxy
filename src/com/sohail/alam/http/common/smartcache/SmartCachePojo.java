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

package com.sohail.alam.http.common.smartcache;

import java.io.File;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 25/9/13
 * Time: 9:15 PM
 */
public class SmartCachePojo {

    public long timestamp;
    public File file;
    public long lastModified;
    public long fileSize;
    public byte[] fileData;
    public String mediaType;

    /**
     * Instantiates a new Smart cache pojo.
     *
     * @param file     the file
     * @param fileData the file data
     * @param fileSize the file size
     */
    public SmartCachePojo(File file, byte[] fileData, long fileSize, String mediaType) {
        this.timestamp = System.currentTimeMillis();
        this.fileData = fileData;
        this.file = file;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.lastModified = file.lastModified();
    }
}
