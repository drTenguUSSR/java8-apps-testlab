package mil.teng24b.aspectj.alib;

import mil.teng24b.aspectj.alib.habit.HomeHabit;

public class Cat extends Animal implements HomeHabit {
    private SoundToolObj soundToolObj=new SoundToolObj();
    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        getLogger().info("cat say-beg");
        soundToolObj.makeSound("cat say meow! with name",getName());
        getLogger().info("cat say-end");
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
