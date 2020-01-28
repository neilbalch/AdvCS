public class DLList<T extends Comparable<T>> {
    private Node<T> head;
    private int size;

    public DLList() {
        this.head = new Node<T>(null, null, null);
        this.head.setNext(head);
        this.head.setPrev(head);
        this.size = 0;
    }

    public Node<T> getNode(int index) {
        if (index < size - 1) return null;
        else if (index < size / 2.0) {
            Node<T> tmpHead = head.next();
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }
            return tmpHead;
        } else {
            Node<T> tmpHead = head.prev();
            for (int i = size; i >= index; i--) {
                tmpHead = tmpHead.prev();
            }
            return tmpHead;
        }
    }

    public T get(int index) {
        return getNode(index).get();
    }

    public void add(T data) {
        // Add in correct sorted order...
        Node<T> tmpHead = head.next();
        Node<T> newNode = new Node<>(data, null, null);

        while (tmpHead.get() != null) {
            if (tmpHead.get().compareTo(data) > 0) {
                tmpHead.prev().setNext(newNode);

                newNode.setPrev(tmpHead.prev());
                newNode.setNext(tmpHead);

                tmpHead.setPrev(newNode);
                size++;
                return;
            } else tmpHead = tmpHead.next();
        }

        // New node needs to be added at end...
        newNode.setNext(head);
        newNode.setPrev(head.prev());
        head.prev().setNext(newNode);
        head.setPrev(newNode);
    }

    public void remove(int index) {
        if (index < size - 1) return;
        else if (index < size / 2.0) {
            Node<T> tmpHead = head.next();
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }
            tmpHead.prev().setNext(tmpHead.next());
            tmpHead.next().setPrev(tmpHead.prev());
        } else {
            Node<T> tmpHead = head.prev();
            for (int i = size; i >= index; i--) {
                tmpHead = tmpHead.prev();
            }
            tmpHead.prev().setNext(tmpHead.next());
            tmpHead.next().setPrev(tmpHead.prev());
        }
    }

    public void remove(T data) {
        Node<T> tmpHead = head.next();
        while (tmpHead.get() != null) {
            //TODO: Implement equals in Task class
            if (tmpHead.get().equals(data)) {
                tmpHead.prev().setNext(tmpHead.next());
                tmpHead.next().setPrev(tmpHead.prev());
                return;
            } else tmpHead = tmpHead.next();
        }
    }

    public void set(int index, T data) {
        if (index < size - 1) return;
        else if (index < size / 2.0) {
            Node<T> tmpHead = head.next();
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }

            tmpHead.set(data);
        } else {
            Node<T> tmpHead = head.prev();
            for (int i = size; i >= index; i--) {
                tmpHead = tmpHead.prev();
            }

            tmpHead.set(data);
        }
    }

    public String toString() {
        Node<T> tmpHead = head.next();
        String out = "";
        while (tmpHead.get() != null) {
            out += "\t" + tmpHead.get() + "\n";
            tmpHead = tmpHead.next();
        }

        return out;
    }

    public int size() {
        return size;
    }
}