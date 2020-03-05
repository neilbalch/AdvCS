public class Main {
    public static void main(String[] args) {
        BTree<Integer> tree = new BTree<>();
        tree.add(90);
        tree.add(80);
        tree.add(100);
        tree.add(70);
        tree.add(85);
        tree.add(98);
        tree.add(120);

        System.out.print("InOrder: ");
        System.out.println(tree);
        System.out.print("PreOrder: ");
        System.out.println(tree.toStringPreOrder());

        System.out.print("Contains 85? ");
        System.out.println(tree.contains(85));
        System.out.print("Contains 86? ");
        System.out.println(tree.contains(86));

        System.out.println("\n------------ Part 2 -------------\n");

        tree.remove(70);
        tree.remove(120);

        System.out.print("InOrder: ");
        System.out.println(tree);
        System.out.print("PreOrder: ");
        System.out.println(tree.toStringPreOrder());

        System.out.println("\n------------ Part 4 -------------\n");

        tree = new BTree<>();
        tree.add(90);
        tree.add(80);
        tree.add(100);
        tree.add(98);
        tree.add(91);
        tree.add(99);

        tree.remove(100);

        System.out.print("InOrder: ");
        System.out.println(tree);
        System.out.print("PreOrder: ");
        System.out.println(tree.toStringPreOrder());

        System.out.println("\n------------ Part 5 -------------\n");

        tree = new BTree<>();
        tree.add(90);
        tree.add(100);
        tree.add(98);
        tree.add(110);

        tree.remove(90);

        System.out.print("InOrder: ");
        System.out.println(tree);
        System.out.print("PreOrder: ");
        System.out.println(tree.toStringPreOrder());

        System.out.println("\n------------ Part 6 -------------\n");

        tree = new BTree<>();
        tree.add(90);
        tree.add(80);
        tree.add(100);
        tree.add(98);
        tree.add(110);
        tree.add(91);
        tree.add(99);

        tree.remove(90);

        System.out.print("InOrder: ");
        System.out.println(tree);
        System.out.print("PreOrder: ");
        System.out.println(tree.toStringPreOrder());

        System.out.println("\n------------ Part 7 -------------\n");

        tree = new BTree<>();
        tree.add(90);

        tree.remove(90);

        System.out.print("InOrder: ");
        System.out.println(tree);
        System.out.print("PreOrder: ");
        System.out.println(tree.toStringPreOrder());
    }
}
