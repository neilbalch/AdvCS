public class Runner {
    public static void main(String[] args) {
        
        FinalExam ex = new FinalExam();

        //20 Points - Adding to itemList1
        ex.addItem1("Pen", 1.06);
        ex.addItem1("Paper",.15);
        ex.addItem1("Paper",.11);
        ex.addItem1("Keyboard", 19.99);
        ex.addItem1("Keyboard", 5.99);
        ex.addItem1("TShirt", 20.01);

        //20 Points
        //print each item in the order specified by the Item class
        System.out.println();
        ex.printItems1();
		System.out.println();

        
        //20 Points
        //Add values to itemList2
        ex.addItem2("Pencil", 1.05);
        ex.addItem2("Laptop", 599.99);
        ex.addItem2("Laptop", 599.99);
        ex.addItem2("Apple", 0.45);
        ex.addItem2("Apple", 0.45);
        ex.addItem2("Apple", 0.45);


        //20 Points
        //print itemList2
        System.out.println();
        ex.printItems2();


        //20 Points
        //Get amount
        System.out.println();
        System.out.println( ex.getAmount(new Item("Laptop",1.05)) + " Laptop(s) at $1.05");
        System.out.println( ex.getAmount(new Item("Apple",0.46)) + " Apple(s) at $0.46");
        System.out.println( ex.getAmount(new Item("Apple",0.45)) + " Apple(s) at $0.45");
    
    }
}
