package mil.teng.q2024.animals4classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Animal {
    private static final Logger logger = LoggerFactory.getLogger(Animal.class);

    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected Logger getLogger() {
        return logger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public abstract void makeSound();
}
