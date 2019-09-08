import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public abstract class Employee {
    private String name;
    private String photoFile;
    private String jobTitle;
    private Image image;

    public Employee(String name, String photoFile, String jobTitle) {
        this.name = name;
        this.photoFile = photoFile;
        this.jobTitle = jobTitle;
        try {
            this.image = ImageIO.read(new File(photoFile));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public abstract double getSalary();

    public String getJobTitle() {
        return jobTitle;
    }

    public String getName() {
        return name;
    }

    public void drawPhoto(Graphics g, Point pos, ImageObserver i) {
            g.drawImage(image, pos.x, pos.y, 85, 85, i);
            g.drawString(name, pos.x, pos.y + 105);
    }

    public String toString() {
        return "Name: " + name + ", Job Title: " + jobTitle + ", Salary: " + getSalary();
    }
}