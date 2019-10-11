import java.util.ArrayList;

public class ArrayQuiz {
	private int[] a1;
	private ArrayList<Integer> a2;
	private int[][] a3;
	
	public ArrayQuiz() {
		a1 = new int[5];
		for(int i = 0; i < a1.length; i++)
			a1[i] = (int)(Math.random() * 10 + 1);
		
		a2 = new ArrayList<Integer>(10);
		for(int i = 0; i < a1.length; i++)
			a2.add((int)(Math.random() * 5 + 1));
		
		a3 = new int[3][2];
		for(int r = 0; r < a3.length; r++) {
			for(int c = 0; c < a3[r].length; c++)
				a3[r][c] = (int)(Math.random() * 5 + 1);
		}
	}
	
	public void printArray() {
		for(int element : a1)
			System.out.print(element + "\t");
		System.out.println();
	}
	
	public void printArrayList() {
		for(int element : a2)
			System.out.print(element + "\t");
		System.out.println();
	}
	
	public void print2DArray() {
		for(int[] line : a3) {
			for(int element : line)
				System.out.print(element + "\t");
			System.out.println();
		}
	}
	
	public void sortArrayList() {
		for(int i = 0; i < a2.size() - 1; i++) {
			for(int j = i + 1; j < a2.size(); j++) {
				if(a2.get(i) > a2.get(j)) {
					int temp = a2.get(j);
					a2.set(j, a2.get(i));
					a2.set(i, temp);
				}
			}
		}
	}
	
	public void searchAndReplace2DArray(int search, int replace) {
		for(int r = 0; r < a3.length; r++) {
			for(int c = 0; c < a3[r].length; c++)
				if(a3[r][c] == search) a3[r][c] = replace;
		}
	}
}