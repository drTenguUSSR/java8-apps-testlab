package sub.gradle.alone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {

        //System.out.println(new App().getGreeting());
        System.out.println("exec-beg");
        logger.error("logger message");

    }

    public String getGreeting() {
        return "Hello World!";
    }
}
