import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Iterator;
import java.lang.Double;

public class Exam2 {
	private PriorityQueue<Profile> profileList = new PriorityQueue<Profile>();
	
	public double postFix(String[] input) {
		Stack<String> stk = new Stack<String>();
		for(String character : input) {
			if(character.equals("+")) { 
				double operand1 = Double.parseDouble(stk.pop());
				double operand2 = Double.parseDouble(stk.pop());
				
				//System.out.println(operand1 + " + " + operand2 + " = " + new Double(operand1 + operand2));
				stk.add((new Double(operand1 + operand2)).toString());
			}
			else if(character.equals("-")) {
				double operand2 = Double.parseDouble(stk.pop());
				double operand1 = Double.parseDouble(stk.pop());
				
				//System.out.println(operand1 + " - " + operand2 + " = " + new Double(operand1 - operand2));
				stk.add((new Double(operand1 - operand2)).toString());
			}
			else if(character.equals("*")) {
				double operand1 = Double.parseDouble(stk.pop());
				double operand2 = Double.parseDouble(stk.pop());
				
				//System.out.println(operand1 + " * " + operand2 + " = " + new Double(operand1 * operand2));
				stk.add((new Double(operand1 * operand2)).toString());
			}
			else if(character.equals("/")) {
				double operand2 = Double.parseDouble(stk.pop());
				double operand1 = Double.parseDouble(stk.pop());
				
				//System.out.println(operand1 + " / " + operand2 + " = " + new Double(operand1 / operand2));
				stk.add((new Double(operand1 / operand2)).toString());
			}
			else stk.add(character);
		}
		
		return Double.parseDouble(stk.pop());
	}
	
	public void addProfile(String name, int age) {
		profileList.add(new Profile(name, age));
	}
	public void removeTop() {
		System.out.println(profileList.poll());
	}
	public void peekTop() {
		System.out.println(profileList.peek());
	}
	public void printProfiles() {
		// Create a new list so that we can DESTROY IT!!!!!!!!!!!!!!!!!
		PriorityQueue<Profile> profiles = new PriorityQueue<Profile>();
		Iterator iter = profileList.iterator();
		while(iter.hasNext()) {
			profiles.add((Profile)iter.next());
		}
		
		// HULK DESTROY!!!!!!!!!!!!!
		while(profiles.size() > 0) {
			System.out.println(profiles.poll());
		}
	}
}