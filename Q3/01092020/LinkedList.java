class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T item) {
        this.item = item;
        this.next = null;
    }

    public Node(T item, Node<T> next) {
        this.item = item;
        this.next = next;
    }

    public T getItem() {
        return item;
    }

    public Node<T> next() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public String toString() {
        return item.toString();
    }
}

public class LinkedList {
    public static void main(String[] args) {
        Node<String> node1 = new Node<String>("pig");
        Node<String> node2 = new Node<String>("bear", node1);
        Node<String> node3 = new Node<String>("bird", node2);
        Node<String> node4 = new Node<String>("dog", node3);
        Node<String> node5 = new Node<String>("cat", node4);

        Node<String> head = node5;
        while (head != null) {
            System.out.println("List Item: \"" + head.getItem() + "\"");
            head = head.next();
        }
    }
}