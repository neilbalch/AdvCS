import java.io.Serializable;

public class BTree<T extends Comparable<T>> implements Serializable {
    private TNode<T> root;
    private int passes;
    private int size;

    private int getTempCounter;

    public BTree() {
        root = null;
        passes = 0;
    }

    public int getPasses() {
        return passes;
    }

    public int getSize() {
        return size;
    }

    public int add(T data) {
        passes = 1;
        size++;

        if (root == null) root = new TNode<T>(data);
        else {
            add(data, root);
        }

        return passes;
    }

    private void add(T data, TNode<T> newRoot) {
        if (contains(data)) return;

        passes++;

        if (newRoot.get().compareTo(data) > 0) {
//            System.out.println(data + " < " + newRoot.get());
            if (newRoot.getLeft() == null) {
                TNode<T> newNode = new TNode<>(data);
                newNode.setParent(newRoot);
                newRoot.setLeft(newNode);
            } else add(data, newRoot.getLeft());
        } else if (newRoot.get().compareTo(data) < 0) {
//            System.out.println(data + " > " + newRoot.get());
            if (newRoot.getRight() == null) {
                TNode<T> newNode = new TNode<>(data);
                newNode.setParent(newRoot);
                newRoot.setRight(newNode);
            } else add(data, newRoot.getRight());
        }
    }

    public T get(T query) {
        if (!contains(query)) return null;

        passes = 0;
        return get(query, root);
    }

    private T get(T query, TNode<T> root) {
        passes++;

//        System.out.println(query + " ? " + root);

        if (root.get().equals(query)) return root.get();
        else if (root.get().compareTo(query) > 0)
            return get(query, root.getLeft());
        else return get(query, root.getRight());
    }

    public T get(int queryIndex) {
        if (queryIndex > size - 1) return null;
        getTempCounter = 0;
        passes = 0;

        return get(queryIndex, root);
    }

    private T get(int queryIndex, TNode<T> root) {
        passes++;
        if (root == null) return null;

        T result = get(queryIndex, root.getLeft());
        if (result != null) return result;

        if (queryIndex == getTempCounter) return root.get();
        getTempCounter++;

        return get(queryIndex, root.getRight());
    }

    public String toStringInOrder(TNode<T> root) {
        if (root == null) return "";

        String out = "";
        out += toStringInOrder(root.getLeft());
        out += root.get() + " ";
        out += toStringInOrder(root.getRight());

        return out;
    }

    public String toStringPreOrder() {
        return printPreOrder(root);
    }

    private String printPreOrder(TNode<T> root) {
        if (root == null) return "";

        String out = "";
        out += root.get() + " ";
        out += printPreOrder(root.getLeft());
        out += printPreOrder(root.getRight());

        return out;
    }

//    public String toStringPost(){
//        printPostOrder(root);
//    }
//
//    private String printPostOrder(TNode<T> root) {
//        if(root == null) return "";
//
//        String out = "";
//        out += root.get() + " ");
//        out += toStringInOrder(root.getLeft());
//        out += toStringInOrder(root.getRight());
//
//        return out;
//    }

    public boolean contains(T element) {
        passes = 0;
        return contains(element, root);
    }

    private boolean contains(T element, TNode<T> root) {
        if (root == null) return false;
        passes++;

//        System.out.println("\"" + element +"\", " + "\"" + root.get() +"\" " + (element.equals(root.get())));

        if (root.get().equals(element)) return true;
        else if (root.get().compareTo(element) > 0) return contains(element, root.getLeft());
        else return contains(element, root.getRight());
    }

    public T remove(T element) {
        if (!contains(element)) return null;
        else return remove(element, root);
    }

    private T getLeastInTree(TNode<T> root) {
        if (root == null) return null;

        boolean hasLeftChild = root.getLeft() != null;
        boolean hasRightChild = root.getRight() != null;

        if (!hasLeftChild && !hasRightChild) return root.get();
        else if (hasLeftChild && hasRightChild) {
            T leastInLeftTree = getLeastInTree(root.getLeft());
            T leastInRightTree = getLeastInTree(root.getRight());

            T least = root.get().compareTo(leastInLeftTree) < 0 ? root.get() : leastInLeftTree;
            least = least.compareTo(leastInRightTree) < 0 ? least : leastInRightTree;

            return least;
        } else if (hasLeftChild) {
            T leastInTree = getLeastInTree(root.getLeft());
            if (root.get().compareTo(leastInTree) < 0) return root.get();
            else return leastInTree;
        } else if (hasRightChild) { // hasRightChild
            T leastInTree = getLeastInTree(root.getRight());
            if (root.get().compareTo(leastInTree) < 0) return root.get();
            else return leastInTree;
        } else return null;
    }

    private T remove(T element, TNode<T> current) {
        if (current == null) return null;

        if (current.get().equals(element)) {
            boolean hasLeftChild = current.getLeft() != null;
            boolean hasRightChild = current.getRight() != null;
            boolean currentIsOnLeftOfParent;
            if (current.getParent() == null) {
                currentIsOnLeftOfParent = false;
            } else {
                if (current.getParent().getLeft() != null) {
                    currentIsOnLeftOfParent = current.getParent().getLeft().equals(current);
                } else {
                    currentIsOnLeftOfParent = false;
                }
            }

            if (!hasLeftChild && !hasRightChild) {
                if (current.getParent() == null) root = null;
                else {
                    if (currentIsOnLeftOfParent) current.getParent().setLeft(null);
                    else current.getParent().setRight(null);
                }
            } else if (hasLeftChild && hasRightChild) {
                T leastInRightTree = getLeastInTree(current.getRight());
                remove(leastInRightTree);
                TNode<T> leftOfCurrent = current.getLeft();
                TNode<T> rightOfCurrent = current.getRight();

                current.set(leastInRightTree);
                current.setLeft(leftOfCurrent);
                current.setRight(rightOfCurrent);
            } else if (hasLeftChild) {
                if (current.getParent() == null) root = current.getLeft();
                else {
                    if (currentIsOnLeftOfParent) current.getParent().setLeft(current.getLeft());
                    else current.getParent().setRight(current.getLeft());
                }
            } else { // hasRightChild
                if (current.getParent() == null) root = current.getRight();
                else {
                    current.getRight().setParent(current.getParent());
                    if (currentIsOnLeftOfParent) current.getParent().setLeft(current.getRight());
                    else current.getParent().setRight(current.getRight());
                }
            }

            return current.get();
        } else if (current.get().compareTo(element) > 0) {
            return remove(element, current.getLeft());
        } else {
            return remove(element, current.getRight());
        }
    }

    public String toString() {
        return inOrderString(root);
    }

    public String inOrderString(TNode<T> root) {
        String txt = "";
        if (root == null) return "";

        txt += inOrderString(root.getLeft());
        txt += (root.get() + ", ");
        txt += inOrderString(root.getRight());

        return txt;
    }
}