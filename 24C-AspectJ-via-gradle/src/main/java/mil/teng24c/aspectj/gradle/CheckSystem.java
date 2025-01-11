package mil.teng24c.aspectj.gradle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CheckSystem {
    private static final Logger logger = LoggerFactory.getLogger(CheckSystem.class);
    public void checkEnviroment() {
        logger.debug("checkEnviroment beg");
        String var123 = System.getProperty("var123");
        logger.debug("var123 via log={}",var123);
        System.out.println("via console: var123="+var123);

        String tempDir=System.getProperty("java.io.tmpdir");
        logger.debug("tempDir={}",tempDir);

        logger.debug("checkEnviroment end");
    }

    public void checkCreateTempFile() throws IOException {
        logger.debug("checkCreateTempFile beg");
        String prefix="pref-";
        String extension=".ext";
        File tmpFile = File.createTempFile(prefix, extension);
        logger.debug("created temp file pref=[{}] ext=[{}] as [{}]",prefix,extension,tmpFile.getAbsoluteFile());
        logger.debug("checkCreateTempFile end");
    }
}
