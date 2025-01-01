package mil.teng.q2024.aspect4call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterProjectCall {
    private static final Logger logger = LoggerFactory.getLogger(InterProjectCall.class);

    public void someCall() {
        logger.info("someCall called");
    }
}
