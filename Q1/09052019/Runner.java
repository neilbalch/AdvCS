public class Runner {
    public static void main(String[] args) {
        Student s = new Student("Bob Henken");

        System.out.println(s.saying());
        System.out.println(((Profile) s).saying());
    }
}