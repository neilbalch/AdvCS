public class Node<T> {
    private T data;
    private Node next;
    private Node prev;

    public Node(T data, Node prev, Node next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

    public String toString() {
        return data.toString();
    }

    public T get() {
        return data;
    }

    public Node next() {
        return next;
    }

    public Node prev() {
        return prev;
    }

    public void set(T data) {
        this.data = data;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}