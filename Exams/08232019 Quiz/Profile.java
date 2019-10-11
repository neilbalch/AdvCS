public class Profile {
	private String name;
	private int age;
	
	public Profile(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String toString() {
		return name + " is of age " + age;
	}
	
	public void setName(String name) { this.name = name; }
	
	public void setAge(int age) { this.age = age; }
}