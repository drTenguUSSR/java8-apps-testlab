/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mil.teng24c.aspectj.gradle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void xlog(String msg) {
        System.out.println(msg);
    }

    @Test
    public void appHasAGreeting() {
        String bootMsg = "appHasAGreeting run";
        logger.debug("log4j {}", bootMsg);
        xlog("console " + bootMsg);
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    @Test
    public void classpathInfo() {
        String bootMsg = "classpathInfo run";
        logger.debug("log4j {}", bootMsg);
        xlog("console " + bootMsg);

        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        xlog("classpath(" + classpathEntries.length + ")=[");
        for (int i1 = 0; i1 < classpathEntries.length; i1++) {
            xlog("cp=" + classpathEntries[i1]);
        }
        xlog("]");
    }

    //@Test
    public void fastFail() {
        logger.debug("fastFail: debug");
        logger.warn("fastFail: warn");
        String str1 = "aaa";
        Assertions.assertEquals("wrong-string", str1);
    }

    @Test
    public void checkPlainGetter() {
        logger.error("checkPlainGetter on error"); //not work if test passed
        UserLocalName data2 = new UserLocalName("first Data2", "last Data2", 102);
        Assertions.assertEquals("first Data2", data2.getFirst());
        Assertions.assertEquals("last Data2", data2.getLast());
        Assertions.assertEquals(102, data2.getAge());
    }
}
