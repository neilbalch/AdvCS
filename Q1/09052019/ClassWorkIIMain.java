import java.util.ArrayList;

public class ClassWorkIIMain {
    public static void main(String[] args) {
        ArrayList<Animal> animals = new ArrayList<Animal>();
        animals.add(new Cat("Geoffery"));
        animals.add(new Dog("Raymond"));
        animals.add(new Bird("Jack"));

        for (Animal a : animals) {
            a.printInfo();
        }
    }
}
