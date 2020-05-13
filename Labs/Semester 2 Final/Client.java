import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JPanel {
    private final String hostName = "localhost";
    private final int portNumber = 1024;
    private final int borderSize = 3;
    private final int boxSideLength = 40;

    // Non-pawn player colors
    // TODO: Try removing these in favor of black borders on pawns.
    public final Color RED = new Color(193, 0, 0);
    public final Color BLUE = new Color(0, 51, 186);
    public final Color YELLOW = new Color(210, 179, 0);
    public final Color GREEN = new Color(30, 179, 0);

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;

    private int playerNumber;
    private Player[] currentBoard;

    public Client() {
        int windowSize = borderSize + Player.numBoxesInBoardSide * (borderSize + boxSideLength);
        setPreferredSize(new Dimension(windowSize, windowSize));

//        try {
//            sock = new Socket(hostName, portNumber);
//            in = new DataInputStream(sock.getInputStream());
//            out = new DataOutputStream(sock.getOutputStream());
//
//            // Read player number from server.
//            playerNumber = in.readInt();
//        } catch (UnknownHostException err) {
//            System.out.println("Client caught UnknownHostException: " + err.getMessage());
//            System.exit(1);
//        } catch (IOException err) {
//            System.out.println("Client caught IOException: " + err.getMessage());
//            System.exit(1);
//        }

        currentBoard = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBoard(g);

        // TODO: If game hasn't started yet, aka currentBoard == null, display message on screen.
    }

    private void drawBoard(@NotNull Graphics g) {
        int boxAndBorder = borderSize + boxSideLength;
        // Draw Grid
        {
            g.setColor(Color.BLACK);
            // First row...
            g.fillRect(0, 0, getPreferredSize().width, borderSize);
            for (int col = 0; col < Player.numBoxesInBoardSide + 1; col++) {
                g.fillRect(col * boxAndBorder, 0, borderSize, boxAndBorder);
            }
            g.fillRect(0, boxAndBorder, getPreferredSize().width, borderSize);
            // Middle rows...
            for (int row = 1; row < Player.numBoxesInBoardSide - 1; row++) {
                g.fillRect(0, row * boxAndBorder, boxSideLength + borderSize, borderSize);
                g.fillRect(getPreferredSize().width - (boxSideLength + borderSize), row * boxAndBorder, boxSideLength + borderSize, borderSize);
                for (int col = 0; col < Player.numBoxesInBoardSide + 1; col++) {
                    if (col > 1 && col < Player.numBoxesInBoardSide - 1) continue;
                    g.fillRect(col * boxAndBorder, row * boxAndBorder, borderSize, boxAndBorder);
                }
            }
            // Last row...
            g.fillRect(0, boxAndBorder * (Player.numBoxesInBoardSide - 1), getPreferredSize().width, borderSize);
            for (int col = 0; col < Player.numBoxesInBoardSide + 1; col++) {
                g.fillRect(col * boxAndBorder, boxAndBorder * (Player.numBoxesInBoardSide - 1), borderSize, boxAndBorder);
            }
            g.fillRect(0, boxAndBorder * Player.numBoxesInBoardSide, getPreferredSize().width, borderSize);
        }

        // Draw red board items
        {
            g.setColor(RED);

            // Left slide
            {
                Point start = getCenterOfBox(Player.numBoxesInBoardSide - 1, 2);
                Point end = getCenterOfBox(Player.numBoxesInBoardSide - 1, 2 + 4);

                g.fillRect(start.x, start.y - (boxSideLength) / 5, end.x - start.x, (2 * boxSideLength) / 5);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x - boxSideLength / 2, end.x + boxSideLength / 3, end.x + boxSideLength / 3};
                int[] y_pts = {end.y, end.y - boxSideLength / 3, end.y + boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Right slide
            {
                Point start = getCenterOfBox(Player.numBoxesInBoardSide - 1, Player.numBoxesInBoardSide - 5);
                Point end = getCenterOfBox(Player.numBoxesInBoardSide - 1, Player.numBoxesInBoardSide - 2);

                g.fillRect(start.x, start.y - (boxSideLength) / 5, end.x - start.x, (2 * boxSideLength) / 5);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x - boxSideLength / 2, end.x + boxSideLength / 3, end.x + boxSideLength / 3};
                int[] y_pts = {end.y, end.y - boxSideLength / 3, end.y + boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Start zone
            Point startLocation = getCenterOfBox(Player.numBoxesInBoardSide - 1, Player.numBoxesInBoardSide - 5);
            g.setColor(Color.BLACK);
            g.fillRect(startLocation.x - boxSideLength / 2 - borderSize, startLocation.y - 3 * boxSideLength / 2, boxSideLength + 2 * borderSize, boxSideLength);
            g.setColor(RED);
            g.fillOval(startLocation.x - boxSideLength, startLocation.y - boxSideLength / 2 - 2 * boxSideLength - borderSize, 2 * boxSideLength, 2 * boxSideLength);

            // Safety zone
            for (int row = Player.numBoxesInBoardSide - 2; row > (3 * Player.numBoxesInBoardSide) / 5; row--) {
                g.setColor(Color.BLACK);
                g.fillRect((Player.numBoxesInBoardSide - 3) * boxAndBorder, row * boxAndBorder, borderSize, boxAndBorder);
                g.fillRect((Player.numBoxesInBoardSide - 2) * boxAndBorder, row * boxAndBorder, borderSize, boxAndBorder);
                g.fillRect((Player.numBoxesInBoardSide - 3) * boxAndBorder, row * boxAndBorder, boxAndBorder, borderSize);
                g.setColor(RED);
                g.fillRect((Player.numBoxesInBoardSide - 3) * boxAndBorder + borderSize, row * boxAndBorder + borderSize, boxSideLength, boxSideLength);
            }
            Point safetyLocation = getCenterOfBox(((3 * Player.numBoxesInBoardSide) / 5) + 1, Player.numBoxesInBoardSide - 3);
            g.setColor(Color.BLACK);
            g.fillRect(safetyLocation.x - boxSideLength / 2 - borderSize, safetyLocation.y - 3 * boxSideLength / 2, boxSideLength + 2 * borderSize, boxSideLength);
            g.setColor(RED);
            g.fillOval(safetyLocation.x - boxSideLength, safetyLocation.y - boxSideLength / 2 - 2 * boxSideLength - borderSize, 2 * boxSideLength, 2 * boxSideLength);
        }

        // Draw green board items
        {
            g.setColor(GREEN);

            // Bottom slide
            {
                Point start = getCenterOfBox(Player.numBoxesInBoardSide - 3, Player.numBoxesInBoardSide - 1);
                Point end = getCenterOfBox(Player.numBoxesInBoardSide - 7, Player.numBoxesInBoardSide - 1);

                g.fillRect(end.x - (boxSideLength) / 5, end.y, (2 * boxSideLength) / 5, start.y - end.y);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x, end.x - boxSideLength / 3, end.x + boxSideLength / 3};
                int[] y_pts = {end.y + boxSideLength / 2, end.y - boxSideLength / 3, end.y - boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Top slide
            {
                Point start = getCenterOfBox(4, Player.numBoxesInBoardSide - 1);
                Point end = getCenterOfBox(1, Player.numBoxesInBoardSide - 1);

                g.fillRect(end.x - (boxSideLength) / 5, end.y, (2 * boxSideLength) / 5, start.y - end.y);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x, end.x - boxSideLength / 3, end.x + boxSideLength / 3};
                int[] y_pts = {end.y + boxSideLength / 2, end.y - boxSideLength / 3, end.y - boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Start zone
            Point startLocation = getCenterOfBox(4, Player.numBoxesInBoardSide - 1);
            g.setColor(Color.BLACK);
            g.fillRect(startLocation.x - 3 * boxSideLength / 2, startLocation.y - boxSideLength / 2 - borderSize, boxSideLength, boxSideLength + 2 * borderSize);
            g.setColor(GREEN);
            g.fillOval(startLocation.x - boxSideLength / 2 - 2 * boxSideLength - borderSize, startLocation.y - boxSideLength, 2 * boxSideLength, 2 * boxSideLength);

            // Safety zone
            for (int col = Player.numBoxesInBoardSide - 2; col > (3 * Player.numBoxesInBoardSide) / 5; col--) {
                g.setColor(Color.BLACK);
                g.fillRect(col * boxAndBorder, 2 * boxAndBorder, boxAndBorder, borderSize);
                g.fillRect(col * boxAndBorder, 3 * boxAndBorder, boxAndBorder, borderSize);
                g.fillRect(col * boxAndBorder, 2 * boxAndBorder, borderSize, boxAndBorder);
                g.setColor(GREEN);
                g.fillRect(col * boxAndBorder + borderSize, 2 * boxAndBorder + borderSize, boxSideLength, boxSideLength);
            }
            Point safetyLocation = getCenterOfBox(2, ((3 * Player.numBoxesInBoardSide) / 5) + 1);
            g.setColor(Color.BLACK);
            g.fillRect(safetyLocation.x - 3 * boxSideLength / 2, safetyLocation.y - boxSideLength / 2 - borderSize, boxSideLength, boxSideLength + 2 * borderSize);
            g.setColor(GREEN);
            g.fillOval(safetyLocation.x - boxSideLength / 2 - 2 * boxSideLength - borderSize, safetyLocation.y - boxSideLength, 2 * boxSideLength, 2 * boxSideLength);
        }

        if (currentBoard != null) {
            // TODO: Draw player pawns and stuff.
        }
    }

    @Contract(value = "_, _ -> new", pure = true)
    private @NotNull Point getCenterOfBox(int row, int col) {
        return new Point(
                borderSize + boxSideLength / 2 + col * (borderSize + boxSideLength),
                borderSize + boxSideLength / 2 + row * (borderSize + boxSideLength));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorry!");
        frame.add(new Client());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
