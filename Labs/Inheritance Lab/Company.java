public class Company extends Employee {
    private String companyName;
    private double salary;

    public Company(String companyName, String name, String photoFile, String jobTitle, double salary) {
        super(name, photoFile, jobTitle);
        this.companyName = companyName;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public String toString() {
        return super.toString() + ", Company Name: " + companyName;
    }
}
