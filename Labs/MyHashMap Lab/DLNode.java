import java.io.Serializable;

public class DLNode<T> implements Serializable {
    private T data;
    private DLNode next;
    private DLNode prev;

    public DLNode(T data, DLNode prev, DLNode next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

    public String toString() {
        return data == null ? "NULL" : data.toString();
    }

    public T get() {
        return data;
    }

    public DLNode next() {
        return next;
    }

    public DLNode prev() {
        return prev;
    }

    public void set(T data) {
        this.data = data;
    }

    public void setNext(DLNode next) {
        this.next = next;
    }

    public void setPrev(DLNode prev) {
        this.prev = prev;
    }
}