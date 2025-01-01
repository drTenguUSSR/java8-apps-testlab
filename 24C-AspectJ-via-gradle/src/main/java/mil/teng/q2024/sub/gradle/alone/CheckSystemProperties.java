package mil.teng.q2024.sub.gradle.alone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckSystemProperties {
    private static final Logger logger = LoggerFactory.getLogger(CheckSystemProperties.class);
    public void exec() {
        logger.debug("execRunnerApp beg");
        String var123 = System.getProperty("var123");
        logger.debug("var123 via log={}",var123);
        System.out.println("via console: var123="+var123);

        String tempDir=System.getProperty("java.io.tmpdir");
        logger.debug("tempDir={}",tempDir);

        logger.debug("execRunnerApp end");
    }
}
