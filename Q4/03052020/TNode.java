import java.util.TooManyListenersException;

public class TNode<E> {
    private TNode<E> left;
    private TNode<E> right;
    private E data;

    public TNode(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void set(E data) {
        this.data = data;
    }

    public void setLeft(TNode<E> left) {
        this.left = left;
    }

    public void setRight(TNode<E> right) {
        this.right = right;
    }

    public TNode<E> getLeft() {
        return left;
    }

    public TNode<E> getRight() {
        return right;
    }

    public E get() {
        return data;
    }

    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(Object oth) {
        TNode o = (TNode) oth;

        return data.equals(o.get());
    }
}