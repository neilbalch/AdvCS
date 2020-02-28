import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

public class ImageContainer implements Comparable<ImageContainer>, Serializable {
    private String name;
    private URL url;
    // https://stackoverflow.com/a/13687603/3339274
    private transient BufferedImage img;

    public void loadImage() {
        Thread loader = new Thread(() -> {
            try {
                System.out.println("Attempting to load image from URL: \"" + url.toString() + "\"...");
                InputStream in = url.openStream();
                img = ImageIO.read(in);
                in.close();
                System.out.println("Image loaded from URL: \"" + url.toString() + "\".");
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            }
        });
        loader.start();
    }

    public ImageContainer(String name, URL url) {
        this.name = name;
        this.url = url;
        this.img = null;
        loadImage();
    }

    public String toString() {
        return name + ", " + url.toString();
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public BufferedImage getImg() {
        return img;
    }

    @Override
    public int compareTo(ImageContainer o) {
        return 0;
    }
}
