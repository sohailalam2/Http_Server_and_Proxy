package test.com.sohail.alam.http.common.utils.watchservice;

import com.sohail.alam.http.common.utils.LocalFileFetcher;
import com.sohail.alam.http.common.utils.watchservice.WatchService;
import com.sohail.alam.http.common.utils.watchservice.WatchServiceCallback;
import com.sohail.alam.http.common.utils.watchservice.WatchServiceException;

import java.io.File;

import static com.sohail.alam.http.server.ServerProperties.PROP;

/**
 * WatchService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 25, 2013</pre>
 */
public class WatchServiceTest {

    private static final WatchServiceTest TEST = new WatchServiceTest();
    private final String FILE_PATH;
    private final String DIR_PATH;
    private WatchService watchService;

    private WatchServiceTest() {
        PROP.initialize();
        FILE_PATH = LocalFileFetcher.FETCHER.normalizePath("/test/test1.txt");
        DIR_PATH = LocalFileFetcher.FETCHER.normalizePath("/test");
    }

    public static void main(String[] args) throws Exception {
        // Test 1
        TEST.before();
        TEST.testAddAndRemoveCallbackListener();
        TEST.after();

        // Test 2
        TEST.before();
        TEST.testAddFileCallback();
        TEST.testFileWatchFile();
        TEST.testFileWatchFile();
//        TEST.after();

        // Test 3
        TEST.before();
        TEST.testAddDirCallback();
        TEST.testDirWatchFile();
        TEST.testDirWatchFile();
//        TEST.after();
    }

    public void before() throws Exception {
        watchService = WatchService.watcher();
    }

    public void after() throws Exception {
        watchService = null;
    }

    /**
     * Method: addCallbackListener(T listener)
     */
    public void testAddAndRemoveCallbackListener() throws Exception {
        // Try to add Directory WatchServiceCallback
        try {
            TestDirCallback callback = new TestDirCallback();
            watchService.addCallbackListener(callback);
            watchService.removeCallbackListener(callback);
        } catch (WatchServiceException e) {
            System.err.println("Failed to add Dir Callback");
        }
        // Try to add File WatchServiceCallback
        try {
            TestFileCallback callback = new TestFileCallback();
            watchService.addCallbackListener(callback);
            watchService.removeCallbackListener(callback);
        } catch (WatchServiceException e) {
            System.err.println("Failed to add File Callback");
        }
        // Try to add WatchServiceCallback - must throw exception
        try {
            watchService.addCallbackListener(new WatchServiceCallback() {
                @Override
                public void exceptionCaught(File file, Throwable cause) {

                }

                @Override
                public void alreadyWatchingFile(File file) {

                }
            });
        } catch (WatchServiceException e) {
            System.err.println("Failed to add Generic Callback");
        }
    }

    public void testAddFileCallback() throws WatchServiceException {
        watchService.addCallbackListener(new TestFileCallback());
    }

    public void testAddDirCallback() throws WatchServiceException {
        watchService.addCallbackListener(new TestDirCallback());
    }

    public void testDirWatchFile() throws Exception {
        File dir = new File(DIR_PATH);
        watchService.watch(dir);
    }

    public void testFileWatchFile() throws Exception {
        File file = new File(FILE_PATH);
        watchService.watch(file);
    }

    private class TestDirCallback implements WatchServiceCallback.DirectoryService {
        @Override
        public void directoryModified(File directory, long oldLastModified, long newLastModified) {
            System.out.println("Directory Modified => oldLastModified: " + oldLastModified + ", newLastModified: " + newLastModified);
        }

        @Override
        public void fileCountChanged(File directory, long oldNumberOfItems, long newNumberOfItems) {
            System.out.println("New File Added => oldNumberOfItems: " + oldNumberOfItems + ", newNumberOfItems: " + newNumberOfItems);
        }

        @Override
        public void directoryCountChanged(File directory, long oldNumberOfItems, long newNumberOfItems) {
            System.out.println("New Directories Added => oldNumberOfItems: " + oldNumberOfItems + ", newNumberOfItems: " + newNumberOfItems);
        }

        @Override
        public void directoryNoLongerExists(File directory, long oldLastModified) {
            System.out.println("Directory No Longer Exists => oldLastModified: " + oldLastModified);

        }

        @Override
        public void exceptionCaught(File file, Throwable cause) {
            System.err.println("Exception Caught: " + cause.getMessage());
        }

        @Override
        public void alreadyWatchingFile(File file) {
            System.err.println("Already watching directory: " + file.getAbsoluteFile());
        }
    }

    private class TestFileCallback implements WatchServiceCallback.FileService {
        @Override
        public void fileModified(File file, long oldLastModified, long newLastModified) {
            System.out.println("File Modified => Old Last Modified: " + oldLastModified + ", New Last Modified: " + newLastModified);
        }

        @Override
        public void fileNoLongerExists(File file, long lastModified) {
            System.out.println("File No Longer Exists => Last Modified: " + lastModified);
        }

        @Override
        public void exceptionCaught(File file, Throwable cause) {
            System.err.println("Exception Caught: " + cause.getMessage());
        }

        @Override
        public void alreadyWatchingFile(File file) {
            System.err.println("Already Watching File: " + file.getAbsoluteFile());
        }
    }

} 
