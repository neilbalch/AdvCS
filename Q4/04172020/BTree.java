import java.awt.*;

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
            if (newRoot.getLeft() == null) {
                TNode<T> newNode = new TNode<>(data);
                newNode.setParent(newRoot);
                newRoot.setLeft(newNode);
            } else add(data, newRoot.getLeft());
        } else if (newRoot.get().compareTo(data) < 0) {
            if (newRoot.getRight() == null) {
                TNode<T> newNode = new TNode<>(data);
                newNode.setParent(newRoot);
                newRoot.setRight(newNode);
            } else add(data, newRoot.getRight());
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
        else remove(element, root);
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

    private void remove(T element, TNode<T> current) {
        if (current == null) return;

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

        } else if (current.get().compareTo(element) > 0)
            remove(element, current.getLeft());
        else
            remove(element, current.getRight());
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

    public int getHeight() {
        return getHeight(root);
    }

    public int getHeight(TNode<T> root) {
        if (root == null) return -1;
        else {
            int leftHeight = getHeight(root.getLeft());
            int rightHeight = getHeight(root.getRight());

            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    public int getHeight(T element) {
        if (!contains(element)) return -1;

        return getHeight(getNode(element, root));
    }

    private TNode<T> getNode(T element, TNode<T> root) {
        if (root == null) return null;
        else if (root.get().equals(element)) return root;
        else {
            TNode<T> result = getNode(element, root.getLeft());
            if (result != null) return result;
            else return getNode(element, root.getRight());
        }
    }

    public int getLevel() {
        return getHeight() + 1;
    }

    public void drawTree(Graphics g, Point location) {
        drawTree(g, location, root, 0);
    }

    public void drawTree(Graphics g, Point location, TNode<T> root, int level) {
        if (root == null) return;

        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.PLAIN, 18));

        g.drawString(root.get().toString(), location.x - 10, location.y + 5);

        Point leftLocation = (Point) location.clone();
        leftLocation.translate(-(100 - level * 40), 20);
        Point rightLocation = (Point) location.clone();
        rightLocation.translate(100 - level * 40, 20);
        drawTree(g, leftLocation, root.getLeft(), level + 1);
        drawTree(g, rightLocation, root.getRight(), level + 1);
    }
}