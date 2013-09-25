package test.com.sohail.alam.http.common;

import com.sohail.alam.http.common.utils.LocalFileFetcher;
import com.sohail.alam.http.common.utils.LocalFileFetcherCallback;
import com.sohail.alam.http.server.ServerProperties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.sohail.alam.http.server.ServerProperties.PROP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * LocalFileFetcher Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 22, 2013</pre>
 */
public class LocalFileFetcherTest {

    private LocalFileFetcher fetcher;

    /**
     * Before void.
     *
     * @throws Exception the exception
     */
    @Before
    public void before() throws Exception {
        fetcher = LocalFileFetcher.FETCHER;
        ServerProperties.PROP.initialize();
    }

    /**
     * After void.
     *
     * @throws Exception the exception
     */
    @After
    public void after() throws Exception {
        fetcher = null;
    }

    /**
     * Method: normalizePath(String path)
     *
     * @throws Exception the exception
     */
    @Test
    public void testNormalizePath() throws Exception {
        // Test for /
        assertEquals("The path was not normalized properly: ", "./www/" + PROP.DEFAULT_INDEX_PAGE, invokeNormalizePath("/"));

        List<String> pathsToNormalize = new ArrayList<String>();
        pathsToNormalize.add("abc");
        pathsToNormalize.add("abc/");
        pathsToNormalize.add("/abc");
        pathsToNormalize.add("/abc/");
        pathsToNormalize.add("www/abc");
        pathsToNormalize.add("/www/abc");
        pathsToNormalize.add("www/abc/");
        pathsToNormalize.add("/www/abc/");

        // Other cases
        String expectedPath1 = "./www/abc";
        String expectedPath2 = "./www/abc/" + PROP.DEFAULT_INDEX_PAGE;
        for (String path : pathsToNormalize) {
            String actualPath = invokeNormalizePath(path);
            if (path.endsWith("/")) {
                assertEquals("The path was not normalized properly: ", expectedPath2, actualPath);
            } else {
                assertEquals("The path was not normalized properly: ", expectedPath1, actualPath);
            }
        }
    }

    /**
     * Invoke normalize path.
     *
     * @param path the path
     *
     * @return the string
     */
    private String invokeNormalizePath(String path) {
        try {
            Method method = LocalFileFetcher.class.getDeclaredMethod("normalizePath", String.class);
            method.setAccessible(true);
            return (String) method.invoke(fetcher, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Method: fetch(String path)
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetBytes() throws Exception {
        fetcher.fetch(PROP.DEFAULT_INDEX_PAGE, new LocalFileFetcherCallback() {
            @Override
            public void fetchSuccess(String path, byte[] data, String mediaType, long dataLength) {
                Assert.assertTrue("Data Received of length <= 0", data.length > 0);
            }

            @Override
            public void fileNotFound(String path, Throwable cause) {
                fail("PATH: " + path + "\nException: " + cause.getMessage());
            }

            /**
             * Exception caught.
             *
             * @param path  the path
             * @param cause the cause
             */
            @Override
            public void exceptionCaught(String path, Throwable cause) {
                fail("PATH: " + path + "\nException: " + cause.getMessage());
            }
        });
    }

} 
