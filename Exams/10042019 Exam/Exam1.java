import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

public class Exam1 {
	private ArrayList<Item<Profile, Integer>> itemList;
	private ArrayList<Profile> profileList;
	private HashSet<Profile> hashList;
	
	public Exam1() {
		itemList = new ArrayList<>();
		profileList = new ArrayList<>();
		hashList = new HashSet<>();
	}
	
	public void addItemList(String name, int birthYear, int num) {
		itemList.add(new Item<Profile, Integer>(new Profile(name, birthYear), num));
	}
	
	public void addProfileList(String name, int birthYear) {
		ListIterator<Profile> iter = profileList.listIterator();
		Profile newProfile = new Profile(name, birthYear);
		
		while(iter.hasNext()) {
			if(iter.next().getBirthYear() > birthYear) {
				iter.previous();
				iter.add(newProfile);
				iter.next();
				return;
			}
		}
		
		iter.add(newProfile);
	}
	
	public void addHashList(String name, int birthYear) {
		hashList.add(new Profile(name, birthYear));
	}
	
	public void printItemList() {
		ListIterator<Item<Profile, Integer>> iter = itemList.listIterator();
		while(iter.hasNext())
			System.out.println(iter.next());
	}
	
	public void printProfileList() {
		ListIterator<Profile> iter = profileList.listIterator();
		while(iter.hasNext())
			System.out.println(iter.next());
	}
	
	public void printHashList() {
		Iterator<Profile> iter = hashList.iterator();
		while(iter.hasNext())
			System.out.println(iter.next());
	}
}