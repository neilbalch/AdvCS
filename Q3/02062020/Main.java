public class Main {
    public static void main(String[] args) {
        HashTable<Item> table = new HashTable<Item>();
        table.add(new Item("Milk", 2.99));
        table.add(new Item("Apples", 0.50));
        table.add(new Item("Cereal", 2.99));
        table.add(new Item("Juice", 1.99));
        table.add(new Item("Onions", 0.50));

        System.out.println(table);
    }
}