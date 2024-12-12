package mil.teng.q2024.animals4classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundToolObj {
    private static final Logger logger = LoggerFactory.getLogger(SoundToolObj.class);

    public void makeSound(String prefix, String data) {
        logger.info("SoundToolObj {}. {}", prefix, data);
    }
}
