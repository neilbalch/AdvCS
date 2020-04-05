public class Profile implements Comparable<Profile> {
	private String name;
	private int age;
	
	public Profile(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String toString() { return name + " : " + age; }
	public String getName() { return name; }
	public int getAge() { return age; }
	
	public int compareTo(Profile p) {
		if(p.getAge() < age) return -1;
		else if(p.getAge() > age) return 1;
		else if(p.getAge() == age) return 0;

		return 23456;
	}
}