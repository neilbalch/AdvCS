import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (false) {
            BTree<Integer> tree = new BTree<>();
            tree.add((new Integer[]{2, 1, 3, 4, 5}));
            System.out.println("inOrder: " + tree.toString());
//            System.out.println("preOrder: " + tree.toStringPreOrder());
//            System.out.println("getHeight: " + tree.getHeight());
//            System.out.println("getLevel: " + tree.getLevel());
//            System.out.println("getHeight(100): " + tree.getHeight(100));
//            System.out.println("getHeight(70): " + tree.getHeight(70));
            System.out.println(tree.isBalanced());
        }

        Scanner sc = new Scanner(System.in);
        BTree<Integer> tree = new BTree<>();

        while (true) {
            System.out.print("Integer to add? ");
            tree.add(sc.nextInt());
            System.out.println("inOrder: " + tree.toString());
            System.out.println("preOrder: " + tree.toStringPreOrder());
            System.out.println("isBalanced: " + tree.isBalanced());
            System.out.println();
        }
    }
}