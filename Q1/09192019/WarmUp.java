import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class WarmUp {
    public static void main(String[] args) {
        ListIterator<Integer> lstIter;
        ArrayList<Integer> lst = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            lstIter = lst.listIterator();
            int num = (int) (Math.random() * 99 + 1);

//            if(lst.size() > 0 && lst.get(0) > num) {
//                lst.add(0, num);
//            } else {
//                while (lstIter.hasNext() && lstIter.next() < num) {
//                }
//
//                lstIter.previous();
//                lstIter.add(num);
//            }
            boolean added = false;
            while (lstIter.hasNext()) {
                if (lstIter.next() <= num) {
                    lstIter.previous();
                    lstIter.add(num);
                    added = true;
                    break;
                }
            }

            if (!added) lstIter.add(num);
        }

        // https://www.geeksforgeeks.org/collections-sort-java-examples/
//        Collections.sort(lst, Collections.reverseOrder());

        System.out.println(lst);
    }
}