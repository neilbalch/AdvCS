import java.io.Serializable;

class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>>, Serializable {
    public K key;
    public V value;

    public Pair() {
    }

    public Pair(K k, V v) {
        this.key = k;
        this.value = v;
    }

    @Override
    public int compareTo(Pair o) {
        return value.compareTo((V) o.value);
    }
}

public class HashMap<K extends Comparable<K>, V extends Comparable<V>> implements Serializable {
    private Pair<K, DLList<V>>[] table;
    private DLList<K> keys;
    private int size;

    @SuppressWarnings("unchecked")
    public HashMap() {
        table = new Pair[10000];
        keys = new DLList<K>();
        keys = new DLList<K>();
        size = 0;
    }

    public void put(K key, V value) {
        int code = key.hashCode();
        if (table[code] == null) {
            table[code] = new Pair<K, DLList<V>>();
            table[code].key = key;
            table[code].value = new DLList<V>();
            keys.add(key);
            size++;
        }

        if (value != null) table[code].value.add(value);
    }

    @SuppressWarnings("unchecked")
    public DLList<K> getKeys() {
        DLList<K> keysArr = new DLList<K>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) keysArr.add(table[i].key);
        }

        return keysArr;
    }

    public DLList<V> getValue(K key) {
        for (Pair<K, DLList<V>> item : table) {
            if (item == null) continue;
            if (item.key.equals(key)) return item.value;
        }

        return null;
    }

    public int size() {
        return size;
    }

    public String[] toStrArr() {
        String[] arr = new String[size];

        int index = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                arr[index] = table[i].key.toString() + " --> " + table[i].value.toString();
                index++;
            }
        }

        return arr;
    }

    public String toString() {
        String out = "";

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null)
                out += i + " --> " + table[i].key.toString() + " : " + table[i].value.toString() + "\n";
        }
        return out;
    }
}