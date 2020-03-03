class Main {
    private static void printInOrder(Node<Integer> root) {
        if (root == null) return;

        printInOrder(root.getLeft());
        System.out.print(root.get() + " ");
        printInOrder(root.getRight());
    }

    private static void printPreOrder(Node<Integer> root) {
        if (root == null) return;

        System.out.print(root.get() + " ");
        printPreOrder(root.getLeft());
        printPreOrder(root.getRight());
    }

    private static void printPostOrder(Node<Integer> root) {
        if (root == null) return;

        printPostOrder(root.getLeft());
        printPostOrder(root.getRight());
        System.out.print(root.get() + " ");
    }

    private static void printReverseOrder(Node<Integer> root) {
        if (root == null) return;

        printReverseOrder(root.getRight());
        System.out.print(root.get() + " ");
        printReverseOrder(root.getLeft());
    }

    public static void main(String[] args) {
        Node<Integer> root = new Node(1);
        Node<Integer> a = new Node(2);
        Node<Integer> b = new Node(3);
        Node<Integer> c = new Node(4);
        Node<Integer> d = new Node(5);
        Node<Integer> e = new Node(6);
        Node<Integer> f = new Node(7);

        root.setLeft(a);
        root.setRight(b);
        a.setLeft(c);
        a.setRight(d);
        b.setLeft(e);
        b.setRight(f);

        System.out.println("in order print: ");
        printInOrder(root);
        System.out.println();

        System.out.println("\npre order print: ");
        printPreOrder(root);
        System.out.println();

        System.out.println("\npost order print: ");
        printPostOrder(root);
        System.out.println();

        System.out.println("\nreverse order print: ");
        printReverseOrder(root);
        System.out.println();
/*
    BTree<Integer> test = new BTree<Integer>();
    test.add(root);
    test.add(a);
    test.add(d);
    Node<Integer> g = new Node(-1);
    test.add(g);
    System.out.println(test.printInOrder());
    */
    }
}