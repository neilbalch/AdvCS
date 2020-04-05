public class Item implements Comparable {
	private String name;
	private double price;
	
	public Item(String name, double price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() { return name; }
	public double getPrice() { return price; }
	public String toString() { return name + " : $" + price; }
	
	public boolean equals(Item item) {
		//System.out.println("EQ " + item.getName() +  " == " + name
		//+ item.getName().equals(name) 
		//+ item.getPrice() + " == " + price 
		//+ (item.getPrice() == price));
		return item.getName().equals(name) && item.getPrice() == price;
	}
	
	public int compareTo(Object o) {
		Item item = (Item)o;
		
		//System.out.println("CT " + item.getName() +  " == " + name
		//+ item.getName().equals(name) 
		//+ item.getPrice() + " == " + price 
		//+ (item.getPrice() == price));
		
		int result = item.getName().compareTo(name);
		if(result != 0) return -result;
		else {
			double result2 = 100 * (item.getPrice() - price);
			if(result2 != 0) return -(int)result2;
			else return 0;
		}
	}
}