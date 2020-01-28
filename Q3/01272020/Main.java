public class Main {
    public static void main(String[] args) {
        DLList<String> test = new DLList<>();
        test.add("abc");
        test.add("ghi");
        test.add("def");

        System.out.println(test);
        System.out.println(test.get(0));
        System.out.println(test.get(1));
        System.out.println(test.get(2));
        System.out.println();

        test.remove("def");

        System.out.println(test);

        test.remove(0);

        System.out.println(test);

        test.remove(1);

        System.out.println(test);
    }
}