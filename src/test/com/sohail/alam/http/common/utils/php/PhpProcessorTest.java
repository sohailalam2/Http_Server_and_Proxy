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

package test.com.sohail.alam.http.common.utils.php;

import com.sohail.alam.http.common.php.PhpProcessor;
import com.sohail.alam.http.common.php.PhpProcessorCallback;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.sohail.alam.http.server.ServerProperties.PROP;
import static org.junit.Assert.fail;

/**
 * PhpProcessor Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 25, 2013</pre>
 */
public class PhpProcessorTest {
    private String testFilePath;

    @Before
    public void before() throws Exception {
        PROP.initialize();
        testFilePath = PROP.WEBAPP_PATH + "/test2.php";
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: processPhp(String phpFile, PhpProcessorCallback callback)
     */
    @Test
    public void testProcessPhp() throws Exception {
        PhpProcessor.processPhp(testFilePath, new PhpProcessorCallback() {

            @Override
            public void success(String fileName, byte[] data, int dataLength) {
                System.out.println("DATA:\n" + new String(data));
            }

            @Override
            public void failure(String fileName, byte[] data, int dataLength) {
                fail(new String(data));
            }

            @Override
            public void fileNotFound(String fileName, Throwable cause) {
                fail(cause.getMessage());
            }

            @Override
            public void exceptionCaught(String fileName, Throwable cause) {
                fail(cause.getMessage());
            }
        });
    }

    /**
     * Method: copyResult(InputStream is, StringBuilder builder)
     */
    @Test
    public void testCopyResult() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = PhpProcessor.getClass().getMethod("copyResult", InputStream.class, StringBuilder.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
