import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public abstract class Employee {
    private String name;
    private String photoFile;
    private String jobTitle;

    public Employee(String name, String photoFile, String jobTitle) {
        this.name = name;
        this.photoFile = photoFile;
        this.jobTitle = jobTitle;
    }

    public abstract double getSalary();

    public void drawPhoto(Graphics g, Point pos, ImageObserver i) {
        try {
            Image image = ImageIO.read(new File(photoFile));
            g.drawImage(image, pos.x, pos.y, 85, 85, i);
            g.drawString(name, pos.x, pos.y + 105);
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public String toString() {
        return "Name: " + name + ", Job Title: " + jobTitle + ", Salary: " + getSalary();
    }
}