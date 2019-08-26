public class ArrayTest {
    public static void main(String[] args) {
        int[] random = new int[100];

        for(int i = 0; i < random.length; i++) {
            random[i] = (int)(Math.random() * 99 + 1);
        }

        System.out.println("Array Elements:");
        for(int element : random) {
            System.out.println(element);
        }
    }
}