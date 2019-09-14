import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Student {
    private String name;
    private Image image;

    public Student(String name, String imagePath) {
        this.name = name;

        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public void drawProfilePicture(Graphics g, Point location, ImageObserver i) {
        g.drawImage(image, location.x, location.y, 140, 140, i);
    }

    public String toString() {
        return name;
    }
}
