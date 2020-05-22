import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    public static final Color RED = new Color(220, 58, 58);
    public static final Color BLUE = new Color(51, 92, 208);
    public static final Color YELLOW = new Color(231, 204, 33);
    public static final Color GREEN = new Color(54, 208, 26);
    public static final int numBoxesPerSide = 16;
    public static final int numPawns = 4;
    public static final Point startZone = new Point(-1, -1);
    public static final Point safeZone = new Point(numBoxesPerSide, numBoxesPerSide);

    public Color playerColor;
    // Location map for Point() coordinates:
    // -1                        : in home
    // 0 to numSpacesInBoardSide : on board
    // numSpacesInBoardSide      : in safe zone
    public Point[] pawnLocations;
    public int latestPawnInSafeZone;

    public Player(Color playerColor) {
        this.playerColor = playerColor;

        this.pawnLocations = new Point[numPawns];
        for (int i = 0; i < 4; i++) pawnLocations[i] = new Point(-1, -1);

        this.latestPawnInSafeZone = -1;
    }

    @Override
    public String toString() {
        String pawns = "{ ";
        for (Point p : pawnLocations) pawns += (p.toString() + " ");
        pawns += "}";

        return "Color: " + playerColor + ", Pawns: " + pawns;
    }

    public int numPawnsAtStart() {
        int count = 0;
        for (int i = 0; i < 4; i++)
            if (pawnLocations[i].equals(startZone)) count++;
        return count;
    }

    public int numPawnsInSafeZone() {
        int count = 0;
        for (int i = 0; i < 4; i++)
            if (pawnLocations[i].equals(safeZone))
                count++;
        return count;
    }
}
