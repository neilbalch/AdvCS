import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JPanel {
    private final String hostName = "localhost";
    private final int portNumber = 1024;
    private final boolean testingMode = false;
    //    private final boolean testingMode = true;
    private final int borderSize = 3;
    private final int boxSideLength = 40;
    private final int boxAndBorder = borderSize + boxSideLength;
    private final int btnsOffset = 200;
    private final int pawnBorderSize = 3;
    private final int pawnRadius = boxSideLength / 3;

    // Non-pawn player colors
    private final Color RED = new Color(193, 0, 0);
    private final Color BLUE = new Color(0, 51, 186);
    private final Color YELLOW = new Color(210, 179, 0);
    private final Color GREEN = new Color(30, 179, 0);
    private HashMap<Message.Card, String> cardDescriptions;

    private Socket sock;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private int playerNumber;
    private Message lastMsg;
    private boolean showInstructions;

    private JComboBox<String> pawnToMove;
    private JButton makeMoveBtn;
    private JButton instructionsBtn;

    public Client() {
        int windowSize = borderSize + Player.numBoxesInBoardSide * (borderSize + boxSideLength);
        setPreferredSize(new Dimension(windowSize, windowSize));
        setLayout(null);

        cardDescriptions = new HashMap<>();
        cardDescriptions.put(Message.Card.ONE, "Move forward 1 spots any pawn on the board or in Start");
        cardDescriptions.put(Message.Card.TWO, "Move forward 2 spots any pawn on the board or in Start");
        cardDescriptions.put(Message.Card.THREE, "Move forward 3 spots any pawn on the board");
        cardDescriptions.put(Message.Card.FOUR, "Move *backward* 4 spots any pawn on the board");
        cardDescriptions.put(Message.Card.FIVE, "Move forward 5 spots any pawn on the board");
        cardDescriptions.put(Message.Card.SEVEN, "Move forward 7 spots any pawn on the board");
        cardDescriptions.put(Message.Card.EIGHT, "Move forward 8 spots any pawn on the board");
        cardDescriptions.put(Message.Card.TEN, "Move forward 10 spots any pawn on the board, or back one spot");
        cardDescriptions.put(Message.Card.ELEVEN, "Move forward 11 spots any pawn on the board");
        cardDescriptions.put(Message.Card.TWELVE, "Move forward 12 spots any pawn on the board");
        cardDescriptions.put(Message.Card.SORRY, "SORRY! Move a pawn from Start to replace another player's");

        initPawnToChoose(null, false);

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

        lastMsg = null;

        // If we're testing, setup a test game board.
        if (testingMode) {
            lastMsg = new Message();
            lastMsg.type = Message.Type.PlayerTurn;
            lastMsg.playerNum = 0;
            lastMsg.players = new Player[]{
                    new Player(Player.RED),
                    new Player(Player.BLUE),
                    new Player(Player.YELLOW),
                    new Player(Player.GREEN)
            };
            lastMsg.card = Message.Card.ONE;

            lastMsg.players[0].pawnLocations[0].setLocation(Player.numBoxesInBoardSide, Player.numBoxesInBoardSide);
            lastMsg.players[0].latestPawnInSafeZone = 0;
            lastMsg.players[0].pawnLocations[1].setLocation(0, 4);

            lastMsg.players[1].pawnLocations[0].setLocation(Player.numBoxesInBoardSide, Player.numBoxesInBoardSide);
            lastMsg.players[1].latestPawnInSafeZone = 0;
            lastMsg.players[1].pawnLocations[1].setLocation(2, 0);

            lastMsg.players[2].pawnLocations[0].setLocation(Player.numBoxesInBoardSide, Player.numBoxesInBoardSide);
            lastMsg.players[2].latestPawnInSafeZone = 0;
            lastMsg.players[2].pawnLocations[1].setLocation(0, 5);

            lastMsg.players[3].pawnLocations[0].setLocation(Player.numBoxesInBoardSide, Player.numBoxesInBoardSide);
            lastMsg.players[3].latestPawnInSafeZone = 0;
            lastMsg.players[3].pawnLocations[1].setLocation(8, 0);
        } else {
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
                            lastMsg = msg;

                            if (msg.type == Message.Type.PlayerTurn) {
                                System.out.println("PlayerTurn message received");

                                if (lastMsg.playerNum == playerNumber) {
                                    System.out.println("PlayerTurn message received for me");
                                    // Declare list of player choices
                                    DLList<String> choicesList = new DLList<>();
                                    Player me = lastMsg.players[playerNumber];

                                    // If there aren't any pawns on the board, only 1 or 2 can move a pawn off start.
                                    //  If so, add first pawn in start to list
                                    if (me.numPawnsAtStart() - me.numPawnsInSafeZone() == 0) {
                                        if (lastMsg.card == Message.Card.ONE || lastMsg.card == Message.Card.TWO) {
                                            choicesList.add(Integer.valueOf(Player.numPawns - me.numPawnsAtStart() + 1).toString());
                                        }
                                        // Else if there are pawns on the board:
                                        //  Add them to the list
                                    } else if (Player.numPawns - (me.numPawnsInSafeZone() + me.numPawnsAtStart()) > 0) {
                                        for (int i = 0; i < me.pawnLocations.length; i++) {
                                            if (!me.pawnLocations[i].equals(Player.startZone) && !me.pawnLocations[i].equals(Player.safeZone)) {
                                                choicesList.add(Integer.valueOf(i + 1).toString());
                                            }
                                        }
                                        // Else
                                        //  Add no move possible to list
                                    } else choicesList.add("Skip Turn");

                                    // Enable dropdown menu, add items to menu
                                    String[] choicesArr = new String[choicesList.size()];
                                    for (int i = 0; i < choicesList.size(); i++) {
                                        System.out.println("choicesArr[" + i + "]: " + choicesList.get(i));
                                        choicesArr[i] = choicesList.get(i);
                                    }

                                    initPawnToChoose(choicesArr, true);
                                }

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
    }

    private void initPawnToChoose(String[] choices, boolean enabled) {
        // If it's already on the screen, remove the old one.
        if (pawnToMove != null) remove(pawnToMove);

        if (choices != null) pawnToMove = new JComboBox<>(choices);
        else pawnToMove = new JComboBox<>();
        pawnToMove.setBounds(getPreferredSize().width / 2 - 105 - 105 - 155 + btnsOffset, 2 * getPreferredSize().height / 3, 100, 30);
        pawnToMove.setEnabled(enabled);
        add(pawnToMove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        g.setFont(new Font("Calibri", Font.PLAIN, 14));

        if (lastMsg == null) {
            g.drawString("Waiting for the game to start when at least two players join.",
                    getPreferredSize().width / 2 - 105 - 105 - 155 + 200,
                    2 * getPreferredSize().height / 3 - 13 - 15);
        }

        if (showInstructions) {
            Color transWhite = new Color(255, 255, 255, 178);
            g.setColor(transWhite);
            g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);

            // Display text.
            {
                g.setColor(Color.BLACK);

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

                drawStringArr(g, new String[]{
                        "Objective: Move pawns from the start to the safe zone. First to move all " + Player.numPawns + " to the safe zone wins!",
                        "Each player takes turns drawing numbered cards.",
                        "Only 1 or 2 can move out of start, 4 moves backwards.",
                        "2 card results in a second turn.",
                        "SORRY! card moves to the location of the next pawn owned by another player, bumping them back to start."
                }, new Point(50, 50));
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
        }
    }

    private void drawBoard(@NotNull Graphics g) {
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

        // What color is the player?
        String color = "";
        switch (playerNumber) {
            case 0:
                color = "Red";
                break;
            case 1:
                color = "Blue";
                break;
            case 2:
                color = "Yellow";
                break;
            case 3:
                color = "Green";
                break;
        }
        g.drawString("You are the " + color + " player.", getPreferredSize().width / 2 - 105 - 105 - 155 + btnsOffset, 2 * getPreferredSize().height / 3 + 45);

        if (lastMsg != null) {
            // Draw Red player's pawns
            {
                int playerIndex = 0;
                int topPawnInStart = Player.numPawns - lastMsg.players[playerIndex].numPawnsAtStart();
                boolean anyPawnsInStart = topPawnInStart < Player.numPawns;
                boolean anyPawnsInSafeZone = lastMsg.players[playerIndex].latestPawnInSafeZone != -1;

                if (anyPawnsInStart) {
                    Point startLocation = getCenterOfBox(Player.numBoxesInBoardSide - 1, Player.numBoxesInBoardSide - 5);
                    Point pawnCenter = new Point(startLocation.x, startLocation.y - boxSideLength / 2 - boxSideLength - borderSize);
                    drawPawn(g, pawnCenter, Player.RED, topPawnInStart);
                }

                if (anyPawnsInSafeZone) {
                    Point safetyLocation = getCenterOfBox(((3 * Player.numBoxesInBoardSide) / 5) + 1, Player.numBoxesInBoardSide - 3);
                    Point pawnCenter = new Point(safetyLocation.x, safetyLocation.y - boxSideLength / 2 - boxSideLength - borderSize);
                    drawPawn(g, pawnCenter, Player.RED, lastMsg.players[playerIndex].latestPawnInSafeZone);
                }

                int count = 0;
                for (Point pawn : lastMsg.players[playerIndex].pawnLocations) {
                    if (!pawn.equals(Player.startZone) && !pawn.equals(Player.safeZone)) {
                        drawPawn(g, getCenterOfBox(pawn.y, pawn.x), Player.RED, count);
                    }
                    count++;
                }
            }

            // Draw Blue player's pawns
            {
                int playerIndex = 1;
                int topPawnInStart = Player.numPawns - lastMsg.players[playerIndex].numPawnsAtStart();
                boolean anyPawnsInStart = topPawnInStart < Player.numPawns;
                boolean anyPawnsInSafeZone = lastMsg.players[playerIndex].latestPawnInSafeZone != -1;

                if (anyPawnsInStart) {
                    Point startLocation = getCenterOfBox(Player.numBoxesInBoardSide - 5, 0);
                    Point pawnCenter = new Point(startLocation.x + boxSideLength / 2 + boxSideLength + borderSize, startLocation.y);
                    drawPawn(g, pawnCenter, Player.BLUE, topPawnInStart);
                }

                if (anyPawnsInSafeZone) {
                    Point safetyLocation = getCenterOfBox(Player.numBoxesInBoardSide - 3, 6);
                    Point pawnCenter = new Point(safetyLocation.x + boxSideLength / 2 + borderSize, safetyLocation.y);
                    drawPawn(g, pawnCenter, Player.BLUE, lastMsg.players[playerIndex].latestPawnInSafeZone);
                }

                int count = 0;
                for (Point pawn : lastMsg.players[playerIndex].pawnLocations) {
                    if (!pawn.equals(Player.startZone) && !pawn.equals(Player.safeZone)) {
                        drawPawn(g, getCenterOfBox(pawn.y, pawn.x), Player.BLUE, count);
                    }
                    count++;
                }
            }

            // Draw Yellow player's pawns
            if (lastMsg.players.length > 2) {
                int playerIndex = 2;
                int topPawnInStart = Player.numPawns - lastMsg.players[playerIndex].numPawnsAtStart();
                boolean anyPawnsInStart = topPawnInStart < Player.numPawns;
                boolean anyPawnsInSafeZone = lastMsg.players[playerIndex].latestPawnInSafeZone != -1;

                if (anyPawnsInStart) {
                    Point startLocation = getCenterOfBox(0, 4);
                    Point pawnCenter = new Point(startLocation.x, startLocation.y + boxSideLength / 2 + boxSideLength + borderSize);
                    drawPawn(g, pawnCenter, Player.YELLOW, topPawnInStart);
                }

                if (anyPawnsInSafeZone) {
                    Point safetyLocation = getCenterOfBox(5, 2);
                    Point pawnCenter = new Point(safetyLocation.x, safetyLocation.y + boxSideLength / 2 + boxSideLength + borderSize);
                    drawPawn(g, pawnCenter, Player.YELLOW, lastMsg.players[playerIndex].latestPawnInSafeZone);
                }

                int count = 0;
                for (Point pawn : lastMsg.players[playerIndex].pawnLocations) {
                    if (!pawn.equals(Player.startZone) && !pawn.equals(Player.safeZone)) {
                        drawPawn(g, getCenterOfBox(pawn.y, pawn.x), Player.YELLOW, count);
                    }
                    count++;
                }
            }

            // Draw Green player's pawns
            if (lastMsg.players.length > 3) {
                int playerIndex = 3;
                int topPawnInStart = Player.numPawns - lastMsg.players[playerIndex].numPawnsAtStart();
                boolean anyPawnsInStart = topPawnInStart < Player.numPawns;
                boolean anyPawnsInSafeZone = lastMsg.players[playerIndex].latestPawnInSafeZone != -1;

                if (anyPawnsInStart) {
                    Point startLocation = getCenterOfBox(4, Player.numBoxesInBoardSide - 1);
                    Point pawnCenter = new Point(startLocation.x - boxSideLength / 2 - boxSideLength - borderSize, startLocation.y);
                    drawPawn(g, pawnCenter, Player.GREEN, topPawnInStart);
                }

                if (anyPawnsInSafeZone) {
                    Point safetyLocation = getCenterOfBox(2, ((3 * Player.numBoxesInBoardSide) / 5) + 1);
                    Point pawnCenter = new Point(safetyLocation.x - boxAndBorder - boxSideLength / 2 - borderSize, safetyLocation.y);
                    drawPawn(g, pawnCenter, Player.GREEN, lastMsg.players[playerIndex].latestPawnInSafeZone);
                }

                int count = 0;
                for (Point pawn : lastMsg.players[playerIndex].pawnLocations) {
                    if (!pawn.equals(Player.startZone) && !pawn.equals(Player.safeZone)) {
                        drawPawn(g, getCenterOfBox(pawn.y, pawn.x), Player.GREEN, count);
                    }
                    count++;
                }
            }

        }
    }

    private void drawPawn(Graphics g, Point center, Color color, int index) {
        g.setColor(Color.BLACK);
        g.fillOval(
                center.x - pawnRadius - pawnBorderSize,
                center.y - pawnRadius - pawnBorderSize,
                2 * pawnRadius + 2 * pawnBorderSize,
                2 * pawnRadius + 2 * pawnBorderSize);
        g.setColor(color);
        g.fillOval(center.x - pawnRadius, center.y - pawnRadius, 2 * pawnRadius, 2 * pawnRadius);
        // Print pawn number
        g.setFont(new Font("Calibri", Font.PLAIN, 24));
        g.setColor(Color.BLACK);
        g.drawString((Integer.valueOf(index + 1)).toString(),
                center.x - 6,
                center.y + 7);
    }

    @Contract(value = "_, _ -> new", pure = true)
    private @NotNull Point getCenterOfBox(int row, int col) {
        return new Point(
                borderSize + boxSideLength / 2 + col * (borderSize + boxSideLength),
                borderSize + boxSideLength / 2 + row * (borderSize + boxSideLength));
    }

    private void drawStringArr(@NotNull Graphics g, String @NotNull [] text, @NotNull Point topLeft) {
        int x = topLeft.x;
        int y = topLeft.y + 7;
        for (String line : text) {
            g.drawString(line, x, y);
            y += 15;
        }
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
