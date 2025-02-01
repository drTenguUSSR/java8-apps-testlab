package mil.teng24c.aspectj.gradle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * run via ide: run configuration/VM options:
 *  -Dvar123=ide-vm-opt
 *  -Djava.io.tmpdir=tmpFolder
 *  -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws IOException {
        String bootMsg="App begin/007";
        logger.debug("log4j "+bootMsg);
        xlog("console "+bootMsg);

        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        xlog("classpath("+classpathEntries.length+")=[");
        for(int i1=0;i1<classpathEntries.length;i1++) {
            xlog("cp="+classpathEntries[i1]);
        }
        xlog("]");


        CheckSystem checkSystem = new CheckSystem();
        checkSystem.checkEnviroment();
        checkSystem.checkCreateTempFile();

        RunnerLombok runnerLombok = new RunnerLombok();
        runnerLombok.exec();

        ExtCall24bAlib extCall24bAlib = new ExtCall24bAlib();
        extCall24bAlib.exec();

        logger.debug("App end");
    }

    public static void xlog(String msg) {
        System.out.println(msg);
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
