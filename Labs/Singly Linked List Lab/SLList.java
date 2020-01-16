import java.time.LocalDateTime;

public class SLList {
    private Node<ItemPair> head;
    private int size;

    public enum SortingMethods {NAME, PRICE, TIME}

    ;
    private SortingMethods currentSortMethod = SortingMethods.TIME;

    public SLList() {
        this.head = null;
        this.size = 0;
    }

    public String toString() {
        String out = "[";
        Node<ItemPair> tmpHead = head;

        while (tmpHead != null) {
            out += tmpHead.getItem() + (tmpHead.next() != null ? ", " : "");
            tmpHead = tmpHead.next();
        }

        out += "]";
        return out;
    }

    public void add(int index, ItemPair item) {
        if (index > (size - 1)) return;
        size++;
        Node<ItemPair> newItem = new Node<ItemPair>(item);

        Node<ItemPair> headTmp = head;
        for (int i = 0; i < index - 1; i++) {
            headTmp = headTmp.next();
        }

        Node<ItemPair> next = headTmp.next();
        headTmp.setNext(newItem);
        newItem.setNext(next);
    }

    public void add(ItemPair item) {
        size++;
        Node<ItemPair> newItem = new Node<ItemPair>(item);
        Node<ItemPair> headTmp = head;
        if (headTmp != null) {
            while (headTmp.next() != null) {
                headTmp = headTmp.next();
            }
            headTmp.setNext(newItem);
        } else {
            head = newItem;
        }
    }

    public void remove(int remove) {
        Node<ItemPair> headTmp = head;
        remove--;
        while (remove > 0) {
            headTmp = headTmp.next();
            remove--;
        }
        headTmp.setNext(headTmp.next().next());
        size--;
    }

    public void remove(ItemPair item) {
        Node<ItemPair> headTmp = head;
        if (headTmp != null) {
            while (headTmp.next() != null) {
                if (headTmp.next().getItem().equals(item)) {
                    headTmp.setNext(headTmp.next().next());
                    size--;
                    return;
                }
                headTmp = headTmp.next();
            }
        }
    }

    public boolean contains(ItemPair item) {
        Node<ItemPair> tmpHead = head;
        while (tmpHead != null) {
            if (tmpHead.getItem().equals(item)) return true;
            else tmpHead = tmpHead.next();
        }

        return false;
    }

    public ItemPair get(int index) {
        Node<ItemPair> headTmp = head;
        while (index > 0 && headTmp != null) {
            headTmp = headTmp.next();
            index--;
        }

        if (headTmp != null) {
            return headTmp.getItem();
        } else {
            return null;
        }
    }

    public void set(int index, ItemPair item) {
        if (index > size - 1) return;

        Node<ItemPair> tmpHead = head;
        for (int i = 0; i < index; i++) {
            tmpHead = tmpHead.next();
        }
        tmpHead.setItem(item);
        tmpHead.getItem().item.setLastUpdated(LocalDateTime.now());
    }

    public int indexOf(ItemPair item) {
        int index = 0;
        Node<ItemPair> headTmp = head;
        if (headTmp.getItem() != null) {
            while (headTmp.next() != null) {
                if (headTmp.getItem().compareTo(item) == 0) {
                    return index;
                }
                headTmp = headTmp.next();
                index++;
            }
        }
        return -1;
    }

    public void setCurrentSortMethod(SortingMethods currentSortMethod) {
        this.currentSortMethod = currentSortMethod;
    }

    public int getSize() {
        return size;
    }

    public void sortList() {
        for (int i = 0; i < size; i++) {
            Node<ItemPair> currentNode = head;
            Node<ItemPair> next = head.next();

            for (int j = 0; j < size - 1; j++) {
                boolean toChange = false;
                switch (currentSortMethod) {
                    case NAME:
                        if (currentNode.getItem().item.getName().compareTo(next.getItem().item.getName()) > 0)
                            toChange = true;
                        break;
                    case PRICE:
                        if (currentNode.getItem().item.getPrice() > next.getItem().item.getPrice())
                            toChange = true;
                        break;
                    case TIME:
                        //TODO: This is broken.
                        if (currentNode.getItem().item.getLastUpdated().isBefore(next.getItem().item.getLastUpdated()))
                            toChange = true;
                        break;
                }

                if (toChange) {
                    ItemPair temp = currentNode.getItem();
                    currentNode.setItem(next.getItem());
                    next.setItem(temp);
                }

                currentNode = next;
                next = next.next();
            }
        }
    }
}