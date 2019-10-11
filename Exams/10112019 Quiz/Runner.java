import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;

public class Runner {
	public static void main(String[] args) {
		HashMap<Item, Double> hm = new HashMap<Item, Double>();
		TreeMap<Item, Double> tm = new TreeMap<Item, Double>();

		hm.put(new Item("apples", "ABC Store"), 2.01);
		tm.put(new Item("apples", "ABC Store"), 2.01);
		
		hm.put(new Item("grapes", "ABC Store"), 2.50);
		tm.put(new Item("grapes", "ABC Store"), 2.50);
		
		hm.put(new Item("apples", "ABC Store"), 3.01);
		tm.put(new Item("apples", "ABC Store"), 3.01);
		
		hm.put(new Item("honey", "XYZ Store"), 4.50);
		tm.put(new Item("honey", "XYZ Store"), 4.50);
		
		hm.put(new Item("honey", "ABC Store"), 4.25);
		tm.put(new Item("honey", "ABC Store"), 4.25);
		
		hm.put(new Item("bananas", "XYZ Store"), 3.40);
		tm.put(new Item("bananas", "XYZ Store"), 3.40);

		System.out.println("HashMap:");
		for (Map.Entry<Item, Double> entry : hm.entrySet()) {  
            System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		System.out.println("\nTreeMap:");
		for (Map.Entry<Item, Double> entry : tm.entrySet()) {  
            System.out.println(entry.getKey() + " : $" + entry.getValue());
		}
	}
}