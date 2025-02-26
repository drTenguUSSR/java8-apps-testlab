package mil.teng251.codesnippets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * run via ide: run configuration/VM options:
 * -Djava.io.tmpdir=tmpFolder
 * -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("codesnippets app beg");
        NowToUTCString.execute(args);
        logger.debug("codesnippets app end");
    }

}
