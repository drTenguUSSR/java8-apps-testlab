package mil.teng24b.aspectj.alib;

public class Dog extends Animal {
    private SoundToolObj soundToolObj = new SoundToolObj();

    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        getLogger().info("dog say-beg");
        soundToolObj.makeSound("dog say Bark! with name", getName());
        getLogger().info("dog say-end");
    }
}
