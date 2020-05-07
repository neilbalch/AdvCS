import java.io.Serializable;

public class Player implements Serializable {
    private final int id;
    private int x;
    private int y;

    public Player(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft() {
        x -= 5;
    }

    public void moveRight() {
        x += 5;
    }

    public void moveUp() {
        y -= 5;
    }

    public void moveDown() {
        y += 5;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "I am a sprite.";
    }

}
