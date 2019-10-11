public class Runner {
	public static void main(String[] args) {
		Item<ToDo> todo1 = new Item<ToDo>(new ToDo("Thing 1", 1));
		Item<ToDo> todo2 = new Item<ToDo>(new ToDo("Thing 2", 2));
		
		Item<String> person1 = new Item<String>("Jeff");
		Item<String> person2 = new Item<String>("Morgan");
		
		Item<Integer> num1 = new Item<Integer>((int)(Math.random() * 10 + 1));
		Item<Integer> num2 = new Item<Integer>((int)(Math.random() * 10 + 1));
		
		System.out.println(todo1.get());
		System.out.println(todo2.get());
		
		System.out.println(person1.get());
		System.out.println(person2.get());
		
		System.out.println(num1.get());
		System.out.println(num2.get());
	}
}