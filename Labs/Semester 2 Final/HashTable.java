public class HashTable<T extends Comparable<T>> {
    private DLList<T>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        table = new DLList[100];
        size = 0;
    }

    public void add(T data) {
        int i = data.hashCode() % table.length;
        if (table[i] == null) {
            table[i] = new DLList<T>();
            size++;
        }
        table[i].add(data);
    }

    public DLList<T> get(int index) {
//        return table[index];
        DLList<T> temp = null;
        int currentIndex = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            else {
                temp = table[i];
                if (currentIndex == index) return temp;
                currentIndex++;
            }
        }

        return temp;
    }

    public void remove(int index) {
        DLList<T> temp = null;
        int currentIndex = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            else {
                temp = table[i];
                if (currentIndex == index) {
                    table[i] = null;
                    size--;
                    System.out.println("Removed @ index=" + index);
                }
                currentIndex++;
            }
        }
    }

    public DLList<T> getByHashCode(int code) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].hashCode() == code) return table[i];
        }

        return null;
    }

    public int size() {
        return size;
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].size() > 0)
                out += i + " --> " + table[i].toString() + "\n";
        }
        return out;
    }
}