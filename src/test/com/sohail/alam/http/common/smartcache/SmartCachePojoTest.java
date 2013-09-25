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
