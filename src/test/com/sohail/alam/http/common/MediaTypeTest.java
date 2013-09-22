package test.com.sohail.alam.http.common;

import com.sohail.alam.http.common.MediaType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * MediaType Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 22, 2013</pre>
 */
public class MediaTypeTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getType(String str)
     */
    @Test
    public void testGetType() throws Exception {
        String actualType = MediaType.getType(".html");
        String expectedType = "text/html";
        Assert.assertEquals(expectedType, actualType);
    }


} 
