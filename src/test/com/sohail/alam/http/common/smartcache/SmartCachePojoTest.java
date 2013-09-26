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

package test.com.sohail.alam.http.common.smartcache;

import com.sohail.alam.http.common.smartcache.SmartCache;
import com.sohail.alam.http.common.smartcache.SmartCachePojo;
import com.sohail.alam.http.common.utils.LocalFileFetcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.sohail.alam.http.server.ServerProperties.PROP;

/**
 * SmartCachePojo Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 25, 2013</pre>
 */
public class SmartCachePojoTest {

    private static final String FILENAME = "test.txt";
    private static File file;
    private static OutputStream out;

    private static void create() throws IOException {
        PROP.initialize();
        file = new File(LocalFileFetcher.FETCHER.normalizePath("/test.txt"));
        out = new FileOutputStream(file);
        out.write("Hello world!".getBytes());
        out.flush();
    }

    private static void edit() throws IOException {
        out.write("Hello world Again!".getBytes());
        out.flush();
        out.close();
    }

    private static void delete() {
        System.out.println("Deleting File: " + file.delete());
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        create();

        SmartCachePojo pojo = new SmartCachePojo(file, "Hello World".getBytes(), 11, "text/plain");
        SmartCache.cache().put(file.getAbsolutePath(), pojo);

        Thread.sleep(2000);

        edit();

        Thread.sleep(2000);

        delete();

    }

}
