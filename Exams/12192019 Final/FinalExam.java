import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FinalExam {
	private PriorityQueue<Item> itemList1 = new PriorityQueue<>();
	private HashMap<Item, Integer> itemList2 = new HashMap<>();
	
	public void addItem1(String name, double price) {
		itemList1.add(new Item(name, price));
	}
	
	public void addItem2(String name, double price) {
		Item item = new Item(name, price);
		//System.out.print("List:\t");
		//System.out.println(itemList2);
		//System.out.print("Item:\t");
		//System.out.println(item);
		
		boolean exists = false;
		int number = 0;
		Iterator it = itemList2.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			//System.out.print(((Item)entry.getKey()) + " : " + (item) + "\t");
			if(((Item)entry.getKey()).compareTo(item) != 0) {
				exists = true;
				number = (Integer)(entry.getValue());
				//System.out.println("exists");
			}
		}
		//System.out.println(exists);
		
		if(!exists) {
			//System.out.println("List doesn't have it.\t");
			//System.out.println(itemList2.get(item));
			itemList2.put(item, 1);
		} else itemList2.put(item, number + 1);
		
		//System.out.println();
	}
	
	public void printItems1() {
		//PriorityQueue<Item> duplicate = (PriorityQueue<Item>)(itemList1.clone());
		PriorityQueue<Item> duplicate = new PriorityQueue<>();
		Iterator it = itemList1.iterator();
		while(it.hasNext()) duplicate.add((Item)it.next());
		
		while(duplicate.size() > 0) System.out.println(duplicate.poll());
	}
	
	public void printItems2() {
		Iterator it = itemList2.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			System.out.println(entry.getKey().toString() + " : " + entry.getValue());
		}
	}
	
	public int getAmount(Item item) {
		Iterator it = itemList2.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			//System.out.print(((Item)entry.getKey()) + " : " + (item) + "\t");
			if(((Item)entry.getKey()).compareTo(item) == 0 
				&& ((Integer)entry.getValue()) != 0) {
				return (Integer)(entry.getValue());
				//System.out.println("exists");
			}
		}
		
		return 0;
		
		//if(itemList2.get(item) == null) return 0;
		//else return itemList2.get(item);
	}
}