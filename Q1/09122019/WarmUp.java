import java.util.ArrayList;

public class WarmUp {
    public static void main(String[] args) {
        ArrayList<MyItem> lst = new ArrayList<>();
        lst.add(new MyItem<String, Double>("Cookies", 9.99));
        lst.add(new MyItem<String, Double>("Waffles", 5.99));
        lst.add(new MyItem<String, Double>("Doughnuts", 3.99));

        for (MyItem i : lst) {
            System.out.println(i);
        }
    }
}