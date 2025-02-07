package mil.teng251.smtpsender;

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
    public void fastPass() {
        logger.debug("fastPass: debug");
        logger.warn("fastPass: warn");
        String str1 = "aaa";
        Assertions.assertEquals("aaa", str1);
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
}
