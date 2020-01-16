import java.time.LocalDateTime;

public class Item implements Comparable<Item> {
    private String name;
    private double price;
    private LocalDateTime lastUpdated;

    public Item(String name, double price, LocalDateTime lastUpdated) {
        this.name = name;
        this.price = price;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String toString() {
        return name + ", $" + price + ", Time Updated: " + lastUpdated;
    }

    @Override
    public int compareTo(Item oth) {
        int result = name.compareToIgnoreCase(oth.getName());
        if (result != 0) return result;
        else if (price - oth.getPrice() != 0) return (int) (price - oth.getPrice());
        else return 0;
    }

    @Override
    public boolean equals(Object o) {
        Item oth = (Item) o;
        return oth.name.equalsIgnoreCase(name) && oth.price == price;
    }
}
