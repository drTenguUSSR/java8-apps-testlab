package mil.teng251.smtpsender;

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

        logger.debug("App end");
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
