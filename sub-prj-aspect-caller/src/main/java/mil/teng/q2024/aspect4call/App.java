package mil.teng.q2024.aspect4call;

import mil.teng.q2024.animals4classes.Animal;
import mil.teng.q2024.animals4classes.Cat;
import mil.teng.q2024.animals4classes.Dog;
import mil.teng.q2024.animals4classes.habit.HomeHabit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.error("===================================");
        logger.info("load info Animal:{}",Animal.getRawInfo());

        logger.info("makeSound-before");
        makeSound();
        logger.info("makeSound-after");

        String stamp = "sub-prj-aspect-caller: " + Instant.now().toString();
        System.out.println("console message:" + stamp);
        logger.info("message info {}", stamp);
        logger.error("message error {}", stamp);

        Animal vaska = new Cat("Vaska", 3);
        logger.info("vaska is name={} age={}", vaska.getName(), vaska.getAge());
        vaska.makeSound();
        checkAndPlay(vaska);

        Animal toto = new Dog("Toto", 10);
        logger.info("toto is name={} age={}", toto.getName(), toto.getAge());
        checkAndPlay(toto);
        toto.makeSound();

        InterProjectCall ipc = new InterProjectCall();
        ipc.someCall();
        logger.error("========= done!");
    }

    private static void checkAndPlay(Animal animal) {
        if (!(animal instanceof HomeHabit)) {
            logger.info("animal {} can't play",animal.getName());
            return;
        }
        HomeHabit homed = (HomeHabit) animal;
        logger.info("play start with RED ball");
        String ret = homed.playingWoolBall(5, "RED");
        logger.info("play return {}", ret);
    }

    public static void makeSound() {
        logger.info("inProject make sound");
    }
}
