public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public String getColor() {
        return "My color is brown.";
    }

    @Override
    public String speak() {
        return "WOOF! WOOF!.";
    }
}
