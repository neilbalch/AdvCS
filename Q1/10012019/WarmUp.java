import java.util.HashSet;

public class WarmUp {
    public static void main(String[] args) {
//        Given the Original List of 4 4 4 4 4 4 4 4
        int[] lst = {4, 4, 4, 4, 4, 4, 4, 4};
//        Output a list of ODDS without duplicates: []
//        Output a list of EVENS without duplicates: [4]
        HashSet<Integer> odds = new HashSet<>();
        HashSet<Integer> evens = new HashSet<>();
        for (int element : lst) {
            if (element % 2 == 0) evens.add(element);
            else odds.add(element);
        }

        System.out.println("Odds");
        System.out.println(odds);
        System.out.println("Evens");
        System.out.println(evens);
    }
}