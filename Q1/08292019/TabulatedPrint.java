public class TabulatedPrint {
    public static void main(String[] args) {
        int[][] arr = new int[5][3];

        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[r].length; c++) {
                arr[r][c] = (int) (Math.random() * 50 + 1);
            }
        }

        for (int[] row : arr) {
            for (int element : row) {
                System.out.print(element + "\t");
            }
            System.out.println();
        }
    }
}
