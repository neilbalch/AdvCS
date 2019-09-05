public class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }

    @Override
    public String getColor() {
        return "My color is blue and green.";
    }

    @Override
    public String speak() {
        return "Tweet Tweet.";
    }
}
