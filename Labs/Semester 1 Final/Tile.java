import java.awt.*;
import java.io.Serializable;

public class Tile implements Serializable {
    public enum Type {LAND, MOUNTAIN, WATER}

    public Type type;

    public boolean hasItem;

    public void draw(Graphics g, Point topLeft, int squareSize) {
//        System.out.println("Drawing Tile @ " + topLeft + " of size: " + squareSize);
//        g.setColor(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255), 100));

        if (this.type == Type.LAND) {
            g.setColor(new Color(75, 145, 41));
            g.fillRect(topLeft.x, topLeft.y, squareSize, squareSize);

            if (hasItem) {
                g.setColor(new Color(143, 41, 86));
                int[] x_pos = {topLeft.x + 20, topLeft.x + squareSize / 2, topLeft.x + squareSize - 20};
                int[] y_pos = {topLeft.y + 20, topLeft.y + squareSize - 20, topLeft.y + 20};
                g.fillPolygon(x_pos, y_pos, x_pos.length);
            }
        } else if (this.type == Type.WATER) {
            g.setColor(new Color(94, 166, 163));
            g.fillRect(topLeft.x, topLeft.y, squareSize, squareSize);
        } else if (this.type == Type.MOUNTAIN) {
            g.setColor(new Color(138, 138, 138));
            g.fillRect(topLeft.x, topLeft.y, squareSize, squareSize);

            g.setColor(new Color(90, 90, 90));
            int[] x_pos = {topLeft.x + 5, topLeft.x + squareSize / 2, topLeft.x + squareSize - 5};
            int[] y_pos = {topLeft.y + squareSize - 5, topLeft.y + 5, topLeft.y + squareSize - 5};
            g.fillPolygon(x_pos, y_pos, x_pos.length);
        }
    }

    public static Tile createTile(Type type, boolean hasItem) {
        Tile tile = new Tile();
        tile.type = type;
        tile.hasItem = hasItem;
        return tile;
    }
}
