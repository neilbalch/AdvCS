class DLNode<T> {
    private T data;
    private DLNode next;
    private DLNode prev;

    public DLNode(T data, DLNode prev, DLNode next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

    public String toString() {
        return data == null ? "NULL" : data.toString();
    }

    public T get() {
        return data;
    }

    public DLNode next() {
        return next;
    }

    public DLNode prev() {
        return prev;
    }

    public void set(T data) {
        this.data = data;
    }

    public void setNext(DLNode next) {
        this.next = next;
    }

    public void setPrev(DLNode prev) {
        this.prev = prev;
    }
}

public class DLList<T> implements Comparable<DLList<T>> {
    private DLNode<T> head;
    private int size;

    public DLList() {
        this.head = new DLNode<T>(null, null, null);
        this.head.setNext(head);
        this.head.setPrev(head);
        this.size = 0;
    }

    private DLNode<T> getNode(int index) {
        if (index > size - 1) return null;
        else if (index < size / 2.0) {
            DLNode<T> tmpHead = head.next();
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }
            return tmpHead;
        } else {
            DLNode<T> tmpHead = head.prev();
            for (int i = size - 1; i > index; i--) {
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
        DLNode<T> tmpHead = head.next();
        DLNode<T> newDLNode = new DLNode<>(data, null, null);

//        while (tmpHead.get() != null) {
//            if (tmpHead.get().compareTo(data) > 0) {
//                tmpHead.prev().setNext(newDLNode);
//
//                newDLNode.setPrev(tmpHead.prev());
//                newDLNode.setNext(tmpHead);
//
//                tmpHead.setPrev(newDLNode);
//                size++;
//                return;
//            } else tmpHead = tmpHead.next();
//        }

        // New node needs to be added at end...
        newDLNode.setNext(head);
        newDLNode.setPrev(head.prev());
        head.prev().setNext(newDLNode);
        head.setPrev(newDLNode);
        size++;
    }

    public T remove(int index) {
        if (index > size - 1) return null;
        else if (index < size / 2.0) {
            DLNode<T> tmpHead = head.next();
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }
            tmpHead.prev().setNext(tmpHead.next());
            tmpHead.next().setPrev(tmpHead.prev());
            size--;
            return tmpHead.get();
        } else {
            DLNode<T> tmpHead = head.prev();
            for (int i = size; i >= index; i--) {
                tmpHead = tmpHead.prev();
            }
            tmpHead.prev().setNext(tmpHead.next());
            tmpHead.next().setPrev(tmpHead.prev());
            size--;
            return tmpHead.get();
        }
    }

    public T remove(T data) {
        DLNode<T> tmpHead = head.next();
        while (tmpHead.get() != null) {
            if (tmpHead.get().equals(data)) {
                tmpHead.prev().setNext(tmpHead.next());
                tmpHead.next().setPrev(tmpHead.prev());
                size--;
                return tmpHead.get();
            } else tmpHead = tmpHead.next();
        }
        return null;
    }

    public void set(int index, T data) {
        if (index > size - 1) return;
        else if (index < size / 2.0) {
            DLNode<T> tmpHead = head.next();
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }

            tmpHead.set(data);
        } else {
            DLNode<T> tmpHead = head.prev();
            for (int i = size - 1; i > index; i--) {
                tmpHead = tmpHead.prev();
            }

            tmpHead.set(data);
        }
//        this.remove(index);
//        this.add(data);
    }

    public String toString() {
        DLNode<T> tmpHead = head.next();
        String out = "";
        while (tmpHead.get() != null) {
            out += "\t" + tmpHead.get();
            tmpHead = tmpHead.next();
        }

        return out;
    }

    public int size() {
        return size;
    }

    public boolean contains(T data) {
        DLNode<T> tmpHead = head.next();
        while (tmpHead.get() != null) {
            if (tmpHead.get().equals(data)) return true;
            else tmpHead = tmpHead.next();
        }
        return false;
    }

    public int indexOf(T data) {
        DLNode<T> tmpHead = head.next();
        int index = 0;

        while (tmpHead.get() != null) {
            if (tmpHead.get().equals(data)) return index;
            else {
                tmpHead = tmpHead.next();
                index++;
            }
        }
        return -1;
    }

    @Override
    public int compareTo(DLList<T> o) {
        return 0;
    }
}