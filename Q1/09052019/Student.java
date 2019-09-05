public class Student extends Profile {
    public Student(String name) {
        super(name);
    }

    @Override
    public String saying() {
        return super.saying() + " and I am a student.";
    }
}