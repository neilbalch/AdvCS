public class Animal implements Comparable<Animal> {
    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Animal o) {
        Animal a = (Animal) o;
        if (a.getName().compareTo(getName()) != 0) {
            return getName().compareTo(a.getName());
        } else {
            return getAge() - a.getAge();
        }
    }

    @Override
    public boolean equals(Object o) {
        Animal a = (Animal) o;
        if (a.getName().compareTo(getName()) == 0 && a.getAge() == getAge()) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return getName() + " - " + getAge();
    }
}