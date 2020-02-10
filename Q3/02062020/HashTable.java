public class HashTable<T extends Comparable<T>> {
    private DLList<T>[] table;

    @SuppressWarnings("unchecked")
    public HashTable() {
        table = (DLList<T>[]) new DLList[10];
    }

    public void add(T data) {
        int i = data.hashCode() % table.length;
        if (table[i] == null) table[i] = new DLList<T>();
        table[i].add(data);
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < table.length; i++) {
            if (table[i].size() > 0) out += i + "\t" + table[i].toString() + "\n";
        }
        return out;
    }
}