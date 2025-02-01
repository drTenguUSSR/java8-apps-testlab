package mil.teng24c.aspectj.gradle;

import mil.teng24b.aspectj.alib.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtCall24bAlib {
    private static final Logger logger = LoggerFactory.getLogger(ExtCall24bAlib.class);

    public String simpleStringConcat(String s1, String s2) {
        logger.debug("simpleStringConcat called with '{}' and '{}'", s1, s2);
        if (s1 == null) {
            s1 = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        return s1 + s2;
    }

    public void exec() {
        logger.debug("ExtCall24bAlib:exec BEG");
        Cat vaska = new Cat("Vaska", 2);
        logger.info("vaska is name={} age={}", vaska.getName(), vaska.getAge());
        logger.info("play with RED ball");
        String ret = vaska.playingWoolBall(5, "RED");
        logger.info("return {}", ret);
        logger.debug("ExtCall24bAlib:exec END");
    }
}
