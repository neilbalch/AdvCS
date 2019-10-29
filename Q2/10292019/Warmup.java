import java.util.Queue;
import java.util.LinkedList;

class Customer {
    private String name;
    private String phoneNum;

    public Customer(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String toString() {
        return name + " : " + phoneNum;
    }
}

public class Warmup {
    public static void main(String[] args) {
        Queue<Customer> queue = new LinkedList<Customer>();
        queue.add(new Customer("Jane Doe", "123-457-890"));
        queue.add(new Customer("Jack Manning", "098-754-321"));
        queue.add(new Customer("White Lightning", "346-523-4567"));

        while (!queue.isEmpty())
            System.out.println("Polling: " + queue.poll());
    }
}