package mil.teng24c.aspectj.gradle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CheckSystem {
    private static final Logger logger = LoggerFactory.getLogger(CheckSystem.class);
    public void showClasspath(boolean toConsole) {
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        xlog2(toConsole,"classpath("+classpathEntries.length+")=[");
        for(int i1=0;i1<classpathEntries.length;i1++) {
            xlog2(toConsole,"cp="+classpathEntries[i1]);
        }
        xlog2(toConsole,"]");
    }

    private void xlog2(boolean toConsole, String msg) {
        if(toConsole) {
            System.out.println(msg);
        } else {
            logger.error(msg);
        }
    }

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
