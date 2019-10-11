public class Item<T> {
	private T o;
	
	public Item(T o) {
		this.o = o;
	}
	
	public T get() {
		return o;
	}
}