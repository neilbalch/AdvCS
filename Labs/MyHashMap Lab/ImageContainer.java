import java.net.URL;

public class ImageContainer implements Comparable<ImageContainer> {
    private String name;
    private URL url;

    public ImageContainer(String name, URL url) {
        this.name = name;
        this.url = url;
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

    @Override
    public int compareTo(ImageContainer o) {
        return 0;
    }
}
