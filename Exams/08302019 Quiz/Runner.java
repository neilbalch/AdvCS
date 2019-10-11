public class Runner {
	public static void main(String[] args) {
		ArrayQuiz aq = new ArrayQuiz();
		
		aq.printArray();
		System.out.println();
		System.out.println();
		aq.printArrayList();
		aq.sortArrayList();
		System.out.println();
		System.out.println();
		aq.printArrayList();
		System.out.println();
		System.out.println();
		aq.print2DArray();
		aq.searchAndReplace2DArray(2, 9);
		System.out.println();
		System.out.println();
		aq.print2DArray();
	}
}