package mil.teng24c.aspectj.gradle;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * run via ide: run configuration/VM options:
 * -Dvar123=ide-vm-opt
 * -Djava.io.tmpdir=tmpFolder
 * -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static boolean debug_env = false;

    public static void main(String[] args) throws IOException {
        logger.debug("App begin/008");

        CheckSystem checkSystem = new CheckSystem();
        if (debug_env) {
            checkSystem.showClasspath(false);
            checkSystem.checkEnviroment();
            checkSystem.checkCreateTempFile();
        }

        RunnerLombok runnerLombok = new RunnerLombok();
        runnerLombok.exec();

        ExtCall24bAlib extCall24bAlib = new ExtCall24bAlib();
        logger.debug("simpleStringConcat before");
        String res1 = extCall24bAlib.simpleStringConcat("msg1", "msg2");
        logger.debug("simpleStringConcat after. res={}", res1);
        logger.debug("exec before");
        extCall24bAlib.exec();
        logger.debug("exec after");

        //long call
        logger.debug("work-beg");
        Hasher dat1 = Hashing.md5().newHasher();
        logger.debug("dat1.class={}",dat1.getClass().getCanonicalName());
        dat1.putLong(100400L);
        dat1.putLong(100200L);
        HashCode resHash = dat1.hash();
        logger.debug("work-end. valA={}",resHash);



        logger.debug("App end");
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
