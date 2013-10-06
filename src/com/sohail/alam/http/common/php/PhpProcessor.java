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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.sohail.alam.http.server.ServerProperties.PROP;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 24/9/13
 * Time: 8:11 PM
 */
public class PhpProcessor {

    private PhpProcessor() {

    }

    /**
     * Copy result.
     *
     * @param is      the is
     * @param builder the builder
     *
     * @return the int
     *
     * @throws IOException the iO exception
     */
    private static int copyResult(InputStream is, StringBuilder builder) throws IOException {
        int data, length = 0;
        while ((data = is.read()) != -1) {
            length++;
            builder.append((char) data);
        }
        return length;
    }

    /**
     * Process php.
     *
     * @param phpFile  the php file
     * @param callback the callback
     */
    public static <T extends PhpProcessorCallback> void processPhp(String phpFile, T callback) {
        int successLength = 0, failureLength = 0;
        StringBuilder successBuilder = new StringBuilder();
        StringBuilder failureBuilder = new StringBuilder();
        File file = new File(phpFile);
        ProcessBuilder processBuilder;
        Process p = null;

        try {
            if (!file.exists()) {
                callback.fileNotFound(phpFile, new FileNotFoundException("The requested PHP File does not exists: " + phpFile));
            }

            processBuilder = new ProcessBuilder(PROP.PHP_INSTALL_PATH, file.getAbsolutePath());
            processBuilder.redirectErrorStream(false);
            p = processBuilder.start();
//            p.waitFor();
            successLength = copyResult(p.getInputStream(), successBuilder);
            failureLength = copyResult(p.getErrorStream(), failureBuilder);

            callback.success(phpFile, successBuilder.toString().getBytes(), successLength);
            if (failureLength > 0) {
                callback.failure(phpFile, failureBuilder.toString().getBytes(), failureLength);
            }
        } catch (Exception e) {
            callback.exceptionCaught(phpFile, e);
        }
    }
}
