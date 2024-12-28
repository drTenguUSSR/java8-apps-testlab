package mil.teng.q2024.sub.gradle.alone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        logger.debug("App begin/005");
        //System.out.println("via console: App.main");

        CheckSystemProperties checkSystemProperties = new CheckSystemProperties();
        checkSystemProperties.exec();

        RunnerLombok runnerLombok = new RunnerLombok();
        runnerLombok.exec();

        logger.debug("App end");
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
