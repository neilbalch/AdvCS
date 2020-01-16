class ItemPair implements Comparable<ItemPair> {
    public Item item;
    public int count;

    public ItemPair(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public String toString() {
        return item.toString() + ", Qty: " + count;
    }

    @Override
    public int compareTo(ItemPair oth) {
        return item.compareTo(oth.item);
    }

    @Override
    public boolean equals(Object o) {
        ItemPair oth = (ItemPair) o;
        return item.equals(oth.item);
    }
}
