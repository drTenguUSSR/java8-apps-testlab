package mil.teng.q2024.animals4classes;

import mil.teng.q2024.animals4classes.habit.HomeHabit;

public class Cat extends Animal implements HomeHabit {
    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        getLogger().info("cat {} say: meow!", getName());
    }

    @Override
    public String playingWoolBall(int ballSize, String bollColor) {
        String retColor;
        if (bollColor==null||bollColor.length()<3) {
            retColor="NOTHING";
        } else if (bollColor.equalsIgnoreCase("GREEN")) {
            retColor="RED";
        } else if (bollColor.equalsIgnoreCase("RED")) {
            retColor="GREEN";
        } else {
            retColor="ORANGE";
        }
        getLogger().info("cat {} play with ball.color={} size={}. return retColor={}",getName(),bollColor,ballSize,retColor);
        return retColor;
    }
}
