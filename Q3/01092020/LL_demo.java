import java.util.Iterator;
import java.util.LinkedList;

class Pair<K, V> {
    private K item1;
    private V item2;

    public Pair(K item1, V item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public K getItem1() {
        return item1;
    }

    public V getItem2() {
        return item2;
    }

    public String toString() {
        return item1 + " : " + item2;
    }
}

public class LL_demo {
    public static void main(String[] args) {
        LinkedList<Pair<String, Double>> list = new LinkedList<>();
        list.add(new Pair<String, Double>("Pencil", .25));
        list.add(new Pair<String, Double>("Pen", .49));
        list.add(new Pair<String, Double>("Paper", .10));

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            System.out.println("List item: \"" + iter.next() + "\"");
        }
    }
}