import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.Buffer;

public class Client extends JPanel {
    private final String hostName = "localhost";
    private final int portNumber = 1024;
    private final int borderSize = 3;
    private final int boxSideLength = 40;

    // Non-pawn player colors
    // TODO: Try removing these in favor of black borders on pawns.
    private final Color RED = new Color(193, 0, 0);
    private final Color BLUE = new Color(0, 51, 186);
    private final Color YELLOW = new Color(210, 179, 0);
    private final Color GREEN = new Color(30, 179, 0);
    private HashMap<Message.Cards, String> cardDescriptions;

    private Socket sock;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private int playerNumber;
    private Message lastMsgReceived;
    private boolean showInstructions; // TODO: Implement!

    private JComboBox<String> pawnToMove;
    private JButton makeMoveBtn;
    private JButton instructionsBtn;

    public Client() {
        int windowSize = borderSize + Player.numBoxesInBoardSide * (borderSize + boxSideLength);
        setPreferredSize(new Dimension(windowSize, windowSize));
        setLayout(null);

        cardDescriptions = new HashMap<>();
        cardDescriptions.put(Message.Cards.ONE, "Move forward 1 spots any pawn on the board or in Start");
        cardDescriptions.put(Message.Cards.TWO, "Move forward 2 spots any pawn on the board or in Start");
        cardDescriptions.put(Message.Cards.THREE, "Move forward 3 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.FOUR, "Move *backward* 4 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.FIVE, "Move forward 5 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.SEVEN, "Move forward 7 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.EIGHT, "Move forward 8 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.TEN, "Move forward 10 spots any pawn on the board, or back one spot");
        cardDescriptions.put(Message.Cards.ELEVEN, "Move forward 11 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.TWELVE, "Move forward 12 spots any pawn on the board");
        cardDescriptions.put(Message.Cards.SORRY, "SORRY! Move a pawn from Start to replace another player's");

        int btnsOffset = 200;

        pawnToMove = new JComboBox<>();
        pawnToMove.setBounds(getPreferredSize().width / 2 - 105 - 105 - 155 + btnsOffset, 2 * getPreferredSize().height / 3, 100, 30);
        add(pawnToMove);

        makeMoveBtn = new JButton("Make Move");
        makeMoveBtn.setBounds(getPreferredSize().width / 2 - 105 - 155 + btnsOffset, 2 * getPreferredSize().height / 3, 100, 30);
        makeMoveBtn.addActionListener(e -> {

        });
        add(makeMoveBtn);

        instructionsBtn = new JButton("Show Instructions");
        instructionsBtn.setBounds(getPreferredSize().width / 2 - 155 + btnsOffset, 2 * getPreferredSize().height / 3, 150, 30);
        instructionsBtn.addActionListener(e -> {
            showInstructions = !showInstructions;
            if (showInstructions) instructionsBtn.setText("Hide Instructions");
            else instructionsBtn.setText("Show Instructions");

            repaint();
        });
        add(instructionsBtn);

        lastMsgReceived = null;

        try {
            sock = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());

            // Read player number from server.
            System.out.println("Waiting for player number...");
            playerNumber = (Integer) in.readObject();
            System.out.println("Received player number.");

            Thread msgWatcher = new Thread(() -> {
                while (true) {
                    try {
                        Message msg = (Message) in.readObject();
                        lastMsgReceived = msg;

                        if (msg.type == Message.Type.PlayerTurn) {
                            System.out.println("PlayerTurn message received");
                            // TODO: Enable dropdown field for which pawn to move and "Make Move" button.

                            repaint();
                        } else System.out.println("Watcher didn't receive PlayerTurn message");
                    } catch (IOException err) {
                        System.out.println("Watcher caught IOException: " + err.getMessage());
                    } catch (ClassNotFoundException err) {
                        System.out.println("Watcher couldn't convert received object to Message!");
                    }
                }
            });
            msgWatcher.start();
        } catch (UnknownHostException err) {
            System.out.println("Client caught UnknownHostException: " + err.getMessage());
            System.exit(1);
        } catch (IOException err) {
            System.out.println("Client caught IOException: " + err.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException err) {
            System.out.println("Client caught ClassNotFoundException: " + err.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBoard(g);

        if (lastMsgReceived == null) {
            g.drawString("Waiting for the game to start when at least two players join.",
                    getPreferredSize().width / 2 - 105 - 105 - 155 + 200,
                    2 * getPreferredSize().height / 3 - 13);
        }

        if (showInstructions) {
            Color transWhite = new Color(255, 255, 255, 178);
            g.setColor(transWhite);
            g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);

            // Display text.
            {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Calibri", Font.PLAIN, 14));

                Point startLocation = getCenterOfBox(Player.numBoxesInBoardSide - 1, Player.numBoxesInBoardSide - 5);
                g.drawString("Pawns start here -->",
                        startLocation.x - boxSideLength - 75 - boxSideLength,
                        startLocation.y - boxSideLength / 2 - boxSideLength - borderSize);

                Point safetyLocation = getCenterOfBox(((3 * Player.numBoxesInBoardSide) / 5) + 1, Player.numBoxesInBoardSide - 3);
                g.drawString("Move all " + Player.numPawns + " pawns here to win -->",
                        safetyLocation.x - boxSideLength - 150 - boxSideLength,
                        safetyLocation.y - boxSideLength / 2 - boxSideLength - borderSize);

                Point start = getCenterOfBox(Player.numBoxesInBoardSide - 1, 2);
                g.drawString("This (below) is a slide. Land on the triangle slide of a",
                        start.x,
                        start.y - boxSideLength);
                g.drawString("different color one and automatically slide to the end.",
                        start.x,
                        start.y - boxSideLength + 13);

                g.drawString("Select a pawn to move from the dropdown on your turn.",
                        getPreferredSize().width / 2 - 105 - 105 - 155 + 200,
                        2 * getPreferredSize().height / 3 - 13);
            }

            int boxAndBorder = borderSize + boxSideLength;
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
        }
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

        // Draw yellow board items
        {
            g.setColor(YELLOW);

            // Right slide
            {
                Point start = getCenterOfBox(0, Player.numBoxesInBoardSide - 3);
                Point end = getCenterOfBox(0, Player.numBoxesInBoardSide - 7);

                g.fillRect(end.x, end.y - (boxSideLength) / 5, start.x - end.x, (2 * boxSideLength) / 5);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x + boxSideLength / 2, end.x - boxSideLength / 3, end.x - boxSideLength / 3};
                int[] y_pts = {end.y, end.y - boxSideLength / 3, end.y + boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Left slide
            {
                Point start = getCenterOfBox(0, 4);
                Point end = getCenterOfBox(0, 1);

                g.fillRect(end.x, end.y - (boxSideLength) / 5, start.x - end.x, (2 * boxSideLength) / 5);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x + boxSideLength / 2, end.x - boxSideLength / 3, end.x - boxSideLength / 3};
                int[] y_pts = {end.y, end.y - boxSideLength / 3, end.y + boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Start zone
            Point startLocation = getCenterOfBox(0, 4);
            g.setColor(Color.BLACK);
            g.fillRect(startLocation.x - boxSideLength / 2 - borderSize, startLocation.y + boxSideLength / 2, boxSideLength + 2 * borderSize, boxSideLength);
            g.setColor(YELLOW);
            g.fillOval(startLocation.x - boxSideLength, startLocation.y + boxSideLength / 2 + borderSize, 2 * boxSideLength, 2 * boxSideLength);

            // Safety zone
            for (int row = 1; row < (2 * Player.numBoxesInBoardSide) / 5; row++) {
                g.setColor(Color.BLACK);
                g.fillRect(3 * boxAndBorder, row * boxAndBorder, borderSize, boxAndBorder);
                g.fillRect(2 * boxAndBorder, row * boxAndBorder, borderSize, boxAndBorder);
                g.fillRect(2 * boxAndBorder, (row + 1) * boxAndBorder, boxAndBorder + borderSize, borderSize);
                g.setColor(YELLOW);
                g.fillRect(2 * boxAndBorder + borderSize, row * boxAndBorder + borderSize, boxSideLength, boxSideLength);
            }
            Point safetyLocation = getCenterOfBox(5, 2);
            g.setColor(Color.BLACK);
            g.fillRect(safetyLocation.x - boxSideLength / 2 - borderSize, safetyLocation.y + boxSideLength / 2, boxSideLength + 2 * borderSize, boxSideLength);
            g.setColor(YELLOW);
            g.fillOval(safetyLocation.x - boxSideLength, safetyLocation.y + boxSideLength / 2 + borderSize, 2 * boxSideLength, 2 * boxSideLength);
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

        // Draw blue board items
        {
            g.setColor(BLUE);

            // Bottom slide
            {
                Point start = getCenterOfBox(Player.numBoxesInBoardSide - 5, 0);
                Point end = getCenterOfBox(Player.numBoxesInBoardSide - 2, 0);

                g.fillRect(start.x - (boxSideLength) / 5, start.y, (2 * boxSideLength) / 5, end.y - start.y);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x, end.x - boxSideLength / 3, end.x + boxSideLength / 3};
                int[] y_pts = {end.y - boxSideLength / 2, end.y + boxSideLength / 3, end.y + boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Top slide
            {
                Point start = getCenterOfBox(2, 0);
                Point end = getCenterOfBox(6, 0);

                g.fillRect(start.x - (boxSideLength) / 5, start.y, (2 * boxSideLength) / 5, end.y - start.y);
                int radius = (2 * boxSideLength) / 5;
                g.fillOval(start.x - radius, start.y - radius, 2 * radius, 2 * radius);
                int[] x_pts = {end.x, end.x - boxSideLength / 3, end.x + boxSideLength / 3};
                int[] y_pts = {end.y - boxSideLength / 2, end.y + boxSideLength / 3, end.y + boxSideLength / 3};
                g.fillPolygon(x_pts, y_pts, x_pts.length);
            }

            // Start zone
            Point startLocation = getCenterOfBox(Player.numBoxesInBoardSide - 5, 0);
            g.setColor(Color.BLACK);
            g.fillRect(startLocation.x + boxSideLength / 2, startLocation.y - boxSideLength / 2 - borderSize, boxSideLength, boxSideLength + 2 * borderSize);
            g.setColor(BLUE);
            g.fillOval(startLocation.x + boxSideLength / 2 + borderSize, startLocation.y - boxSideLength, 2 * boxSideLength, 2 * boxSideLength);

            // Safety zone
            for (int col = 1; col < (2 * Player.numBoxesInBoardSide) / 5; col++) {
                g.setColor(Color.BLACK);
                g.fillRect(col * boxAndBorder + borderSize, (Player.numBoxesInBoardSide - 2) * boxAndBorder, boxAndBorder, borderSize);
                g.fillRect(col * boxAndBorder + borderSize, (Player.numBoxesInBoardSide - 3) * boxAndBorder, boxAndBorder, borderSize);
                g.fillRect((col + 1) * boxAndBorder, (Player.numBoxesInBoardSide - 3) * boxAndBorder, borderSize, boxAndBorder);
                g.setColor(BLUE);
                g.fillRect(col * boxAndBorder + borderSize, (Player.numBoxesInBoardSide - 3) * boxAndBorder + borderSize, boxSideLength, boxSideLength);
            }
            Point safetyLocation = getCenterOfBox(Player.numBoxesInBoardSide - 3, 6);
            g.setColor(Color.BLACK);
            g.fillRect(safetyLocation.x - boxSideLength / 2, safetyLocation.y - boxSideLength / 2 - borderSize, boxSideLength, boxSideLength + 2 * borderSize);
            g.setColor(BLUE);
            g.fillOval(safetyLocation.x - boxSideLength / 2, safetyLocation.y - boxSideLength, 2 * boxSideLength, 2 * boxSideLength);
        }

        // Sorry! logo image
        try {
            double scaleFactor = 0.5;
            g.drawImage(ImageIO.read(new File("logo.png")),
                    getPreferredSize().width / 2 - (int) (scaleFactor * (420 / 2)),
                    getPreferredSize().height / 3 - (int) (scaleFactor * (420 / 2)),
                    (int) (420 * scaleFactor), (int) (420 * scaleFactor), this);
        } catch (IOException ignored) {
        }

        if (lastMsgReceived != null) {
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
        frame.setResizable(false);
    }
}
