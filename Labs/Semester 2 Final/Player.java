import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    public static final Color RED = new Color(220, 58, 58);
    public static final Color BLUE = new Color(51, 92, 208);
    public static final Color YELLOW = new Color(231, 204, 33);
    public static final Color GREEN = new Color(54, 208, 26);
    public static final int numBoxesInBoardSide = 16;

    public Color playerColor;
    // Location map for Point() coordinates:
    // -1                        : in home
    // 0 to numSpacesInBoardSide : on board
    // numSpacesInBoardSide      : in safe zone
    public Point[] pawnLocations;

    public Player(Color playerColor) {
        this.playerColor = playerColor;

        this.pawnLocations = new Point[4];
        for (int i = 0; i < 4; i++) pawnLocations[i] = new Point(-1, -1);
    }

    public int numPawnsAtStart() {
        int count = 0;
        for (int i = 0; i < 4; i++)
            if (pawnLocations[i].equals(new Point(-1, -1))) count++;
        return count;
    }

    public int numPawnsInSafeZone() {
        int count = 0;
        for (int i = 0; i < 4; i++)
            if (pawnLocations[i].equals(new Point(numBoxesInBoardSide, numBoxesInBoardSide)))
                count++;
        return count;
    }
}
