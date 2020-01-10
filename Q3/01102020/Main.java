import java.util.Scanner;

class Main {
    private static void printList(Node<String> head) {
        int count = 0;
        System.out.println("List Items:");
        while (head != null && count < 50) {
            System.out.print("\"" + head.getItem() + "\", ");
            head = head.next();
            count++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Node<String> node1 = new Node<String>("pig");
        Node<String> node2 = new Node<String>("bear", node1);
        Node<String> node3 = new Node<String>("bird", node2);
        Node<String> node4 = new Node<String>("dog", node3);
        Node<String> node5 = new Node<String>("cat", node4);

        Node<String> head = node5;

        while (true) {
            printList(head);
            // Ask for name, check if exists
            System.out.println("name?");
            String name = input.next();
            Node<String> yonk = head;
            boolean found = false;
            while (yonk != null && !found) {
                if (yonk.getItem().equals(name)) {
                    System.out.println("found " + name + "!");
                    found = true;
                }
                yonk = yonk.next();
            }
            if (!found) {
                System.out.println(name + " is not in list.");
            }

            // Ask for name, remove item
            System.out.print("Item to remove? ");
            String queryName = input.next();
            Node<String> tempHead = head;
            if (tempHead.getItem().equals(queryName)) {
                head = head.next();
            } else {
                while (tempHead != null) {
                    // If the next item is the same as the query...
                    if (tempHead.next() != null) {
                        if (tempHead.next().getItem().equals(queryName)) {
                            tempHead.setNext(tempHead.next().next());
                        }
                    }

                    tempHead = tempHead.next();
                }
            }

            printList(head);

            // Ask for two names, replace 1 with 2
            System.out.println("name of item to replace?");
            String replace = input.next();

            System.out.println("name of new item?");
            String new_name = input.next();
            yonk = head;
            found = false;
            while (yonk != null && !found) {
                if (yonk.next() != null) {
                    if (yonk.next().getItem().equals(replace)) {
                        Node next_next = yonk.next().next();
                        yonk.setNext(new Node(new_name, next_next));
                        found = true;
                    }
                }
                yonk = yonk.next();
            }

            printList(head);

            int size = 0;
            {
                Node<String> headTemp = head;
                while (headTemp != null) {
                    headTemp = headTemp.next();
                    size++;
                }
            }

            for (int i = 0; i < size; i++) {
                Node<String> currentNode = head;
                Node<String> next = head.next();
                for (int j = 0; j < size - 1; j++) {
                    if (currentNode.getItem().compareTo(next.getItem()) > 0) {
                        String temp = currentNode.getItem();
                        currentNode.setItem(next.getItem());
                        next.setItem(temp);
                    }
                    currentNode = next;
                    next = next.next();
                }
            }
        }
    }
}