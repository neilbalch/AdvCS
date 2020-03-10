import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BTree<Animal> tree = new BTree<>();
        tree.add(new Animal("monkey", 5));
        tree.add(new Animal("bear", 7));
        tree.add(new Animal("octopus", 4));
        tree.add(new Animal("bear", 8));

        System.out.println(tree);

        {
            System.out.print("Add: Animal name? ");
            String name = sc.next();
            System.out.print("Add: Animal age? ");
            int age = sc.nextInt();
            tree.add(new Animal(name, age));
        }

        System.out.println(tree);

        {
            System.out.print("Remove: Animal name? ");
            String name = sc.next();
            System.out.print("Remove: Animal age? ");
            int age = sc.nextInt();
            tree.remove(new Animal(name, age));
        }

        System.out.println(tree);
    }
}