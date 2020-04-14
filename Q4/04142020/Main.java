public class Main {
    public static void main(String[] args) {
        {
            BTree<Integer> tree = new BTree<>();
            tree.add(90);
            tree.add(80);
            tree.add(100);
            tree.add(70);
            tree.add(85);
            tree.add(98);
            tree.add(120);
            System.out.println("inOrder: " + tree.toString());
            System.out.println("preOrder: " + tree.toStringPreOrder());
            System.out.println("getHeight: " + tree.getHeight());
            System.out.println("getLevel: " + tree.getLevel());
        }

        System.out.println();

        {
            BTree<Integer> tree = new BTree<>();
            tree.add(90);
            tree.add(91);
            tree.add(92);
            tree.add(93);
            System.out.println("inOrder: " + tree.toString());
            System.out.println("preOrder: " + tree.toStringPreOrder());
            System.out.println("getHeight: " + tree.getHeight());
            System.out.println("getLevel: " + tree.getLevel());
        }
    }
}