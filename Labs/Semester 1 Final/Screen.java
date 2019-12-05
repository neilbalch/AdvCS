import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Screen extends JPanel {
    private final int moveMagnitude = 3;
    private final boolean amIPlayer1;

    private HashMap<Point, Tile> board;

    private Stack<Integer> player1Health;
    private Stack<Integer> player2Health;

    private int player1Items;
    private int player2Items;
    private boolean instanceIsServer;

    private Point player1Location;
    private Point player2Location;

    public Screen(boolean server) {
        this.setLayout(null);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(600, 600));
        this.amIPlayer1 = server;

        board = new HashMap<Point, Tile>();
        instanceIsServer = server;

        player1Health = new Stack<Integer>();
        player2Health = new Stack<Integer>();
        for (int i = 0; i < 3; i++) {
            player1Health.push(0);
            player2Health.push(0);
        }

        player1Items = 0;
        player2Items = 0;

        player1Location = new Point(300, 300);
        player2Location = new Point(300, 300);

        if (instanceIsServer) {
            // Generate new board.
            int numWaterAreas = 3;
            int numMountainAreas = 8;
            int numGems = 11;
            ArrayList<Point> waterList = new ArrayList<>();
            ArrayList<Point> mountainList = new ArrayList<>();
            ArrayList<Point> gemsList = new ArrayList<>();
            for (int i = 0; i < numWaterAreas; i++) {
                Point temp;
                do {
                    temp = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
                } while (waterList.contains(temp));
                waterList.add(temp);
            }
            for (int i = 0; i < numMountainAreas; i++) {
                Point temp;
                do {
                    temp = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
                } while (waterList.contains(temp) || mountainList.contains(temp));
                mountainList.add(temp);
            }
            for (int i = 0; i < numGems; i++) {
                Point temp;
                do {
                    temp = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
                } while (waterList.contains(temp) || mountainList.contains(temp));
                gemsList.add(temp);
            }

            for (int r = 0; r < 10; r++) {
                for (int c = 0; c < 10; c++) {
//                    System.out.println("r: " + r + ", c: " + c);
                    if (mountainList.contains(new Point(r, c))) {
                        board.put(new Point(r, c), Tile.createTile(Tile.Type.MOUNTAIN, gemsList.contains(new Point(r, c))));
                    } else if (waterList.contains(new Point(r, c))) {
                        board.put(new Point(r, c), Tile.createTile(Tile.Type.WATER, gemsList.contains(new Point(r, c))));
                    } else {
                        board.put(new Point(r, c), Tile.createTile(Tile.Type.LAND, gemsList.contains(new Point(r, c))));
                    }
                }
            }

            // TODO: Open socket and send first board object.
        } else {
            // TODO: Open socket and receive first board object.
        }

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
//                System.out.println("triggered");
                if (e.getKeyChar() == 'w') {
                    if (amIPlayer1) player1Location.translate(0, -moveMagnitude);
                    else player2Location.move(0, -moveMagnitude);
                } else if (e.getKeyChar() == 's') {
                    if (amIPlayer1) player1Location.translate(0, moveMagnitude);
                    else player2Location.move(0, moveMagnitude);
                } else if (e.getKeyChar() == 'a') {
                    if (amIPlayer1) player1Location.translate(-moveMagnitude, 0);
                    else player2Location.move(-moveMagnitude, 0);
                } else if (e.getKeyChar() == 'd') {
                    if (amIPlayer1) player1Location.translate(moveMagnitude, 0);
                    else player2Location.move(moveMagnitude, 0);
                }

                repaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println("pressed");
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("released");
            }
        });

        // TODO: Start thread to poll receiving messages.
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension windowSize = this.getPreferredSize();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, windowSize.width, windowSize.height);

        int squareSize = windowSize.height / 10;
        int x = 0;
        int y = 0;
        g.setColor(Color.BLACK);
        g.fillRect(x, 0, 1, windowSize.height);
        g.fillRect(0, y, windowSize.width, 1);
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
//                System.out.println("r: " + r + ", c: " + c + ", x: " + x + ", y: " + y);

                board.get(new Point(r, c)).draw(g, new Point(x + 1, y + 1), squareSize);

                g.setColor(Color.BLACK);
                g.fillRect(x + squareSize, 0, 1, windowSize.height);
                x += squareSize;
            }
            g.setColor(Color.BLACK);
            g.fillRect(0, y + squareSize, windowSize.width, 1);
            y += squareSize;
            x = 0;

            // TODO: Draw players!
            g.setColor(new Color(0, 10, 162));
            g.fillRect(player1Location.x + 1 - 5, player1Location.y, 8, 15);
            g.fillRect(player2Location.x + 1 - 5, player2Location.y, 8, 15);
            g.setColor(new Color(203, 174, 108));
            g.fillOval(player1Location.x - 5, player1Location.y - 5, 10, 10);
            g.fillOval(player2Location.x - 5, player2Location.y - 5, 10, 10);
        }
    }

    public void deductHealthPoint(int player) {
        if (player == 1) {
            player1Health.pop();
            Message msg = Message.createMessage(1, Message.Action.PlayerLostHealth, null);
            // TODO: Send message.
        } else if (player == 2) {
            player2Health.pop();
            Message msg = Message.createMessage(2, Message.Action.PlayerLostHealth, null);
            // TODO: Send message.
        }
    }

    public void movePlayer(int player, Point direction) {
        if (direction.x == -1) {
            if (player == 1) player1Location.setLocation(player1Location.x - 1, player1Location.y);
            else if (player == 2)
                player2Location.setLocation(player2Location.x - 1, player2Location.y);
        } else if (direction.x == 1) {
            if (player == 1) player1Location.setLocation(player1Location.x + 1, player1Location.y);
            else if (player == 2)
                player2Location.setLocation(player2Location.x + 1, player2Location.y);
        } else if (direction.y == -1) {
            if (player == 1) player1Location.setLocation(player1Location.x, player1Location.y - 1);
            else if (player == 2)
                player2Location.setLocation(player2Location.x, player2Location.y - 1);
        } else if (direction.y == 1) {
            if (player == 1) player1Location.setLocation(player1Location.x, player1Location.y + 1);
            else if (player == 2)
                player2Location.setLocation(player2Location.x, player2Location.y + 1);
        }

        Message msg = Message.createMessage(player, Message.Action.PlayerMoved, player == 1 ? player1Location : player2Location);
        // TODO: Send message.
    }
}
