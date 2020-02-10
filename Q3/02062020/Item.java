public class Item implements Comparable<Item> {
    private double price;
    private String name;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public int hashCode() {
        return (int) price;
    }

    @Override
    public int compareTo(Item o) {
        return (int) (price - o.getPrice());
    }
}