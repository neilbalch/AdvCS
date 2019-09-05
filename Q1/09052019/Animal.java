public abstract class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public abstract String speak();

    public abstract String getColor();

    public String getName() {
        return name;
    }

    public void printInfo() {
        System.out.println("My name is: " + getName());
        System.out.println("I say: " + speak());
        System.out.println(getColor());
    }
}
