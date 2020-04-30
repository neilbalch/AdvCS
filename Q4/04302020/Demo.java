class Manager {
    private int[] numberList;
    private int index;

    public Manager() {
        numberList = new int[5];
        for (int i = 0; i < numberList.length; i++) numberList[i] = (int) (Math.random() * 10 + 1);
        index = 0;
    }

    private synchronized void incrementIndex() {
        index++;
    }

    public void doubleIt() {
        System.out.println(Thread.currentThread().getName() + " starting.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        numberList[index] *= 2;
        incrementIndex();

        System.out.println(Thread.currentThread().getName() + " completed. index=" + index);
    }

    public String toString() {
        String out = "";
        for (int item : numberList) out += item + "\n";
        return out;
    }
}

class SimpleThread implements Runnable {
    private Manager manager;

    public SimpleThread(Manager manager) {
        this.manager = manager;
    }

    public void run() {
        manager.doubleIt();
    }
}

public class Demo {
    public static void main(String[] args) {
        Manager mgr = new Manager();
        System.out.println(mgr);
        SimpleThread st = new SimpleThread(mgr);

        Thread[] threadList = new Thread[5];
        for (int i = 0; i < threadList.length; i++) {
            threadList[i] = new Thread(st);
        }
        for (int i = 0; i < threadList.length; i++) {
            threadList[i].start();
        }
        for (int i = 0; i < threadList.length; i++) {
            try {
                threadList[i].join();
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println(mgr);
    }
}
