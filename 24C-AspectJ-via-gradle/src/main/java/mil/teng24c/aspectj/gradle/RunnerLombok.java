package mil.teng24c.aspectj.gradle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnerLombok {
    private static final Logger logger = LoggerFactory.getLogger(RunnerLombok.class);
    public void exec() {
        logger.debug("RunnerLombok-exec-beg");
        UserLocalName data1 = new UserLocalName();
        data1.setLast("last Data1");
        data1.setFirst("first Data1");
        data1.setAge(101);
        logger.debug("data1={}",data1);

        UserLocalName data2 = new UserLocalName("first Data2","last Data2",102);
        logger.debug("data2-102={}",data2);
        data2.setFirst(data2.getFirst()+"-3f");
        data2.setLast(data2.getLast()+"-3l");
        data2.setAge(103);
        logger.debug("data2-102={}",data2);

        logger.debug("RunnerLombok-exec-end");
    }
}
