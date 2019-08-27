import java.util.ArrayList;

public class ArrayListTest {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            list.add((int)(Math.random() * 999 + 1));
        }

        for(int element : list) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}