public class DLList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public DLList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node<T> getNode(int index) {
        if (index <= size / 2.0) {
            Node<T> tmpHead = head;
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }
            return tmpHead;
        } else if (index > size / 2.0 && index < size) {
            Node<T> tmpHead = tail;
            for (int i = size - 1; i > index; i--) {
                tmpHead = tmpHead.prev();
            }
            return tmpHead;
        } else {
            return null;
        }
    }

    public T get(int index) {
        if (index < size) {
            return getNode(index).get();
        } else {
            return null;
        }
    }

    public void add(int index, T element) {
        //System.out.println("index: " + index + ", size: " + size + ", element: " + element);

        if (size == 0) {
            //System.out.println("Adding new element: size = 0");
            Node<T> node = new Node<T>(element, null, null);
            head = node;
            tail = node;
            size++;
        } else if (index <= size / 2.0) {
            //System.out.println("Adding new element: from head");
            Node<T> tmpHead = head;
            for (int i = 0; i < index - 1; i++) {
                tmpHead = tmpHead.next();
            }
            Node<T> tmp = new Node<T>(element, tmpHead.prev(), tmpHead.next());
            if (tmpHead.next() != null) {
                tmpHead.next().setPrev(tmp);
            } else {
                tail = tmp;
            }
            tmpHead.setNext(tmp);
            size++;
        } else if (index > size / 2.0) {
            //System.out.println("Adding new element: from tail");
            Node<T> tmpTail = tail;
            for (int i = size - 1; i >= index; i--) {
                tmpTail = tmpTail.prev();
            }
            Node<T> node = new Node(element, tmpTail, tmpTail.next());
            if (tmpTail.next() == null) {
                tail = node;
            } else {
                tmpTail.next().setPrev(node);
            }
            tmpTail.setNext(node);
            size++;
        }
    }

    public void add(T element) {
        if (size != 0) {
            add(size, element);
        } else {
            add(0, element);
        }
    }

    public void set(int index, T element) {
        //System.out.println("index: " + index + ", size: " + size + ", element: " + element);

        if (size == 0) {
            //System.out.println("Setting element: size = 0");
            Node<T> node = new Node<T>(element, null, null);
            head = node;
            tail = node;
            size++;
        } else if (index <= size / 2.0) {
            //System.out.println("Setting element: from head");
            Node<T> tmpHead = head;
            for (int i = 0; i < index - 1; i++) {
                tmpHead = tmpHead.next();
            }
            tmpHead.set(element);
        } else if (index > size / 2.0) {
            //System.out.println("Setting element: from tail");
            Node<T> tmpTail = tail;
            for (int i = size - 1; i >= index; i--) {
                tmpTail = tmpTail.prev();
            }
            tmpTail.set(element);
        }
    }

    public void remove(int index) {
        //System.out.println("index: " + index + ", size: " + size);

        if (size == 0) {
            return;
        } else if (index == 0) {
            head = head.next();
            size--;
        } else if (index <= size / 2.0) {
            //System.out.println("Removing element: from head");
            Node<T> tmpHead = head;
            for (int i = 0; i < index; i++) {
                tmpHead = tmpHead.next();
            }

            tmpHead.next().setPrev(tmpHead.prev());
            tmpHead.prev().setNext(tmpHead.next());
            size--;
        } else if (index > size / 2.0) {
            //System.out.println("Removing element: from tail");
            Node<T> tmpTail = tail;
            for (int i = size - 1; i >= index; i--) {
                tmpTail = tmpTail.prev();
            }

            tmpTail.prev().setNext(tmpTail.next());
            size--;
        }
    }

    public int size() {
        return size;
    }

    public String toString() {
        if (size == 0) return "Size: 0 [ ]";
        //if(size == 1) return "[ " + head.get().toString() + " ]";

        String out = "Size: " + size + " [";
        for (int i = 0; i <= size - 2; i++) {
            out += get(i) + ", ";
        }
        out += get(size - 1) + "]";
        return out;
    }

    public boolean contains(T t) {
        Node<T> tmpHead = head;
        for (int i = 0; i < size; i++) {
            if (tmpHead.get().equals(t)) {
                return true;
            }
            tmpHead = tmpHead.next();
        }
        return false;
    }
}