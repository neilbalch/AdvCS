public class BTree<T extends Comparable<T>> {
    private TNode<T> root;

    public BTree() {
        root = null;
    }

    public void add(T data) {
        if (root == null) root = new TNode<T>(data);
        else {
            add(data, root);
        }
    }

    public void add(T data, TNode<T> newRoot) {
        if (newRoot.get().compareTo(data) > 0) {
            if (newRoot.getLeft() == null) newRoot.setLeft(new TNode(data));
            else add(data, newRoot.getLeft());
        } else if (newRoot.get().compareTo(data) < 0) {
            if (newRoot.getRight() == null) newRoot.setRight(new TNode(data));
            else add(data, newRoot.getRight());
        }
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
        return contains(element, root);
    }

    private boolean contains(T element, TNode<T> root) {
        if (root == null) return false;

        if (root.get().equals(element)) return true;
        else if (root.get().compareTo(element) > 0) return contains(element, root.getLeft());
        else return contains(element, root.getRight());
    }

    public void remove(T element) {
        if (!contains(element)) return;
        else remove(element, root, null);
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

    private void remove(T element, TNode<T> current, TNode<T> previous) {
        if (current == null) return;
//        System.out.println(current);

        if (current.get().equals(element)) {
            boolean hasLeftChild = current.getLeft() != null;
            boolean hasRightChild = current.getRight() != null;
            boolean currentIsOnLeftOfParent = previous == null ? false : previous.getLeft().equals(current);

            if (!hasLeftChild && !hasRightChild) {
                if (previous == null) root = null;
                else {
                    if (currentIsOnLeftOfParent) previous.setLeft(null);
                    else previous.setRight(null);
                }
            } else if (hasLeftChild && hasRightChild) {
                T leastInTree = getLeastInTree(current.getRight());
                remove(leastInTree);
                current.set(leastInTree);
            } else if (hasLeftChild) {
                if (previous == null) root = current.getLeft();
                else {
                    if (currentIsOnLeftOfParent) previous.setLeft(current.getLeft());
                    else previous.setRight(current.getLeft());
                }
            } else { // hasRightChild
                if (previous == null) root = current.getRight();
                else {
                    if (currentIsOnLeftOfParent) previous.setLeft(current.getRight());
                    else previous.setRight(current.getRight());
                }
            }

        } else if (current.get().compareTo(element) > 0)
            remove(element, current.getLeft(), current);
        else
            remove(element, current.getRight(), current);
    }

    public String toString() {
        return inOrderString(root);
    }

    public String inOrderString(TNode<T> root) {
        String txt = "";
        if (root == null) return "";

        txt += inOrderString(root.getLeft());
        txt += (root.get() + " ");
        txt += inOrderString(root.getRight());

        return txt;
    }
}