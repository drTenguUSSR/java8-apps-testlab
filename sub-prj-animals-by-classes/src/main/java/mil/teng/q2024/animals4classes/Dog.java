package mil.teng.q2024.animals4classes;

public class Dog extends Animal {
    public Dog(String name,int age) {
        super(name,age);
    }

    @Override
    public void makeSound() {
        getLogger().info("dog {} say: Bark!",getName());
    }
}
