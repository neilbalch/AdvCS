import java.util.Scanner;

class Count implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Counting: " + (i + 1));

            // Pause for 1 second
            try {
                Thread.sleep(1000); // milliseconds
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
}

class CountingStars implements Runnable {
    @Override
    public void run() {
        int i = 0;
        while (true) {
            System.out.println("Counting: " + (i + 1));

            // Pause for 1 second
            try {
                Thread.sleep(1000); // milliseconds
            } catch (InterruptedException ex) {
                System.out.println("I am not done");
                Thread.currentThread().interrupt();
                return;
            }

            i++;
        }
    }
}

class AreaCircle implements Runnable {
    private final double radius;
    private double area;

    public AreaCircle(double radius) {
        this.radius = radius;
    }

    @Override
    public void run() {
        this.area = Math.PI * Math.pow(this.radius, 2);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }

    public double getArea() {
        return area;
    }
}

class VolCylinder implements Runnable {
    private double baseArea;
    private double height;
    private double volume;

    public VolCylinder(double baseArea, double height) {
        this.baseArea = baseArea;
        this.height = height;
    }

    @Override
    public void run() {
        this.volume = baseArea * height;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }

    public double getVolume() {
        return volume;
    }
}

public class Demo {
    public static void main(String[] args) {
        {
            Thread t1 = new Thread(new Count());
            Thread t2 = new Thread(new Count());
            Thread t3 = new Thread(new Count());
            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException ignored) {
            }
        }

        {
            Thread t1 = new Thread(new CountingStars());
            t1.start();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }

            t1.interrupt();
            System.out.println();
        }

        {
            Scanner sc = new Scanner(System.in);
            System.out.print("Radius? ");
            double radius = sc.nextDouble();
            System.out.print("Height? ");
            double height = sc.nextDouble();

            AreaCircle base = new AreaCircle(radius);
            Thread t1 = new Thread(base);
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException ignored) {
            }

            VolCylinder cyl = new VolCylinder(base.getArea(), height);
            Thread t2 = new Thread(cyl);
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException ignored) {
            }
            System.out.println("Volume of cylinder is: " + cyl.getVolume());
        }
    }
}
