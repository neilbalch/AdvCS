public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public String getColor() {
        return "My color is white and black.";
    }

    @Override
    public String speak() {
        return "Meowww.";
    }
}
