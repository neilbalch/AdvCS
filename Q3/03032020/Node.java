public class Node<E> {
    private Node<E> left;
    private Node<E> right;
    private E data;

    public Node(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }

    public Node<E> getLeft() {
        return left;
    }

    public Node<E> getRight() {
        return right;
    }

    public E get() {
        return data;
    }


    public String toString() {
        return data.toString();
    }
}