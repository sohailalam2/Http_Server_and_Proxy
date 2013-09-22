package test.com.sohail.alam.http.server;

import com.sohail.alam.http.server.LocalFileFetcher;
import com.sohail.alam.http.server.ServerProperties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals("The path was not normalized properly: ", "./www", invokeNormalizePath("/"));

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
        String expectedPath = "./www/abc";
        for (String path : pathsToNormalize) {
            String actualPath = invokeNormalizePath(path);
            assertEquals("The path was not normalized properly: ", expectedPath, actualPath);
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
        fetcher.fetch("/www/index.html", new LocalFileFetcher.LocalFileFetcherCallback() {
            @Override
            public void fetchSuccess(String path, byte[] data) {
                Assert.assertTrue("Data Received of length <= 0", data.length > 0);
            }

            @Override
            public void fetchFailed(String path, Throwable cause) {
                fail("PATH: " + path + "\nException: " + cause.getMessage());
            }
        });
    }

} 
