public class Runner {
	public static void main(String[] args) {
		Profile p1 = new Profile("Jennifer", 16);
		
		System.out.println(p1);
		
		Manager mgr = new Manager();
		mgr.changeProfile(p1, "Jen", 17);
		
		System.out.println(p1);
	}
}