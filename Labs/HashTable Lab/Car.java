public class Car implements Comparable<Car> {
    private String make;
    private String model;
    private double price;
    private int year;

    public Car(String make, String model, double price, int year) {
        this.make = make;
        this.model = model;
        this.price = price;
        this.year = year;
    }

    @Override
    public int hashCode() {
        String ltrs = make.substring(0, 3);
        int sum = 0;
        for (char c : ltrs.toCharArray()) {
            sum += c;
        }

        return sum / 5;
    }

    @Override
    public String toString() {
        return make + " " + model + " " + year + " $" + price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int compareTo(Car o) {
        int comparison = make.compareTo(o.getMake());
        if (comparison != 0) return comparison;
        else {
            comparison = model.compareTo(o.getModel());
            if (comparison != 0) return comparison;
            else {
                comparison = year - o.getYear();
                if (comparison != 0) return comparison;
                else {
                    comparison = (int) (price - o.getPrice());
                    if (comparison != 0) return comparison;
                    else return 0;
                }
            }
        }
    }
}
