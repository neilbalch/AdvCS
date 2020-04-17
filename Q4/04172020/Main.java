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
            System.out.println("getHeight(100): " + tree.getHeight(100));
            System.out.println("getHeight(70): " + tree.getHeight(70));
        }

        System.out.println();

        {
            BTree<Integer> tree = new BTree<>();
            tree.add(90);
            tree.add(150);
            tree.add(170);
            tree.add(160);
            tree.add(171);
            tree.add(151);
            System.out.println("inOrder: " + tree.toString());
            System.out.println("preOrder: " + tree.toStringPreOrder());
            System.out.println("getHeight: " + tree.getHeight());
            System.out.println("getLevel: " + tree.getLevel());
            System.out.println("getHeight(150): " + tree.getHeight(150));
            System.out.println("getHeight(151): " + tree.getHeight(151));
        }
    }
}