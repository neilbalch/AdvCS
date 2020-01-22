import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String[] data = {"Cat", "Dog", "Bird", "Bear", "Pig"};
        DLList<String> list = new DLList<String>();

        for (String item : data) {
            list.add(item);
        }

        System.out.println(list);
        System.out.println();

        System.out.print("Adding animal... name? ");
        list.add(input.next());
        System.out.println(list);
        System.out.println();

        System.out.print("Adding animal... name? ");
        String animal_name = input.next();
        System.out.print("Adding animal... index? ");
        int animal_index = input.nextInt();
        list.add(animal_index, animal_name);
        System.out.println(list);
        System.out.println();

        System.out.print("Getting animal... index? ");
        list.get(input.nextInt());
        System.out.println();
    }
}