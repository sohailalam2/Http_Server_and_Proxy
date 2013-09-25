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
