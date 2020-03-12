import java.io.Serializable;

public class TNode<E> implements Serializable {
    private TNode<E> left;
    private TNode<E> right;
    private TNode<E> parent;
    private E data;

    public TNode(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public TNode(E data, TNode<E> left, TNode<E> right, TNode<E> parent) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.parent = parent;
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

    public void setParent(TNode<E> parent) {
        this.parent = parent;
    }

    public E get() {
        return data;
    }

    public TNode<E> getLeft() {
        return left;
    }

    public TNode<E> getRight() {
        return right;
    }

    public TNode<E> getParent() {
        return parent;
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