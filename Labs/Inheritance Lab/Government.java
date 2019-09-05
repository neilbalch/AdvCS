public class Government extends Employee {
    private String cityName;
    private double salary;

    public Government(String cityName, String name, String photoFile, String jobTitle, double salary) {
        super(name, photoFile, jobTitle);
        this.cityName = cityName;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public String toString() {
        return super.toString() + ", City Name: " + cityName;
    }
}