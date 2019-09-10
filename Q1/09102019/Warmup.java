import java.util.ArrayList;

class MyItem<T> {
    private T item;

    public MyItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public String toString() {
        return item.toString();
    }
}

public class Warmup {
    public static void main(String[] args) {
        ArrayList<MyItem> lst = new ArrayList<MyItem>();
        lst.add(new MyItem<String>("This is text."));
        lst.add(new MyItem<Integer>(100));
        lst.add(new MyItem<Double>(3.14159));

        for (MyItem i : lst) {
            System.out.println(i);
        }
    }
}