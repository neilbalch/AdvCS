public class Item implements Comparable<Item> {
	private String name;
	private String store;
	
	public Item(String name, String store) {
		this.name = name;
		this.store = store;
	}
	
	public String toString() { return name + " is in " + store; }
	public String getName() { return name; }
	public String getStore() { return store; }
	
	public boolean equals(Object o) {
		Item i = (Item)o;
		return i.getName().equalsIgnoreCase(name) && i.getStore().equalsIgnoreCase(store);
	}
	
	public int hashCode() {
		int hashCode = name.hashCode() * 7;
		hashCode += store.hashCode() * 11;
		return hashCode;
	}
	
	public int compareTo(Item i) {
		if(i.getName().compareToIgnoreCase(name) > 0) return -1;
		else if(i.getName().compareToIgnoreCase(name) < 0) return 1;
		else {
			if(i.getStore().compareToIgnoreCase(store) > 0) return -1;
			else if(i.getStore().compareToIgnoreCase(store) < 0) return 1;
			else {
				return 0;
			}
		}
	}
}