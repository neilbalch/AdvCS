import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private int id;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Name: " + name + " ID: " + id;
    }

    public boolean equals(Object o) {
        Student other = (Student) o;

        return other.getId() == id && other.getName().equalsIgnoreCase(name);
//        boolean equals = true;
//        if(other.getId() != id) {
//            System.out.println(other.getId() + " != " + id);
//            equals = false;
//        } else if(!other.getName().equalsIgnoreCase(name)) {
//            System.out.println(other.getName() + " != " + name);
//            equals = false;
//        }
//
//        return equals;
    }
}