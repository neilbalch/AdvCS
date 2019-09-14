public class Pair<T, U> {
    private T item1;
    private U item2;
    private String label1;
    private String label2;

    public Pair(T item1, U item2, String label1, String label2) {
        this.item1 = item1;
        this.item2 = item2;
        this.label1 = label1;
        this.label2 = label2;
    }

    public T getItem1() {
        return item1;
    }

    public U getItem2() {
        return item2;
    }

    public String toString() {
        String output = "";
        if (label1 != null) output += label1 + item1;
        if (label2 != null) output += label2 + item2;

        return output;
    }
}
