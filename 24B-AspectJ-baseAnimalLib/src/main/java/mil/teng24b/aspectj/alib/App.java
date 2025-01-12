package mil.teng24b.aspectj.alib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.error("===================================");
        String stamp = Instant.now().toString();
        System.out.println("console message:" + stamp);
        logger.info("message info {}", stamp);
        logger.error("message error {}", stamp);

        Cat vaska=new Cat("Vaska",2);
        logger.info("vaska is name={} age={}",vaska.getName(),vaska.getAge());
        logger.info("play with RED ball");
        String ret = vaska.playingWoolBall(5, "RED");
        logger.info("return {}",ret);
    }
}
