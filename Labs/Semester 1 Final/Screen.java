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

    private Stack<Integer>[] playerHealth;
    private Point[] playerPosition;
    private int[] playerItems;

    private boolean instanceIsServer;

    public Screen(boolean server) {
        this.setLayout(null);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(600, 600));
        this.amIPlayer1 = server;

        board = new HashMap<Point, Tile>();
        instanceIsServer = server;

        playerHealth = new Stack[2];
        playerHealth[0] = new Stack<Integer>();
        playerHealth[1] = new Stack<Integer>();
        for (int i = 0; i < 3; i++) {
            playerHealth[0].push(0);
            playerHealth[1].push(0);
        }

        playerItems = new int[2];

        playerPosition = new Point[2];
        playerPosition[0] = new Point(300, 300);
        playerPosition[1] = new Point(300, 300);


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
//                    if (amIPlayer1) player1Location.translate(0, -moveMagnitude);
//                    else player2Location.move(0, -moveMagnitude);
                    movePlayer(amIPlayer1 ? 1 : 2, new Point(0, 1));
                } else if (e.getKeyChar() == 's') {
//                    if (amIPlayer1) player1Location.translate(0, moveMagnitude);
//                    else player2Location.move(0, moveMagnitude);
                    movePlayer(amIPlayer1 ? 1 : 2, new Point(0, -1));
                } else if (e.getKeyChar() == 'a') {
//                    if (amIPlayer1) player1Location.translate(-moveMagnitude, 0);
//                    else player2Location.move(-moveMagnitude, 0);
                    movePlayer(amIPlayer1 ? 1 : 2, new Point(-1, 0));
                } else if (e.getKeyChar() == 'd') {
//                    if (amIPlayer1) player1Location.translate(moveMagnitude, 0);
//                    else player2Location.move(moveMagnitude, 0);
                    movePlayer(amIPlayer1 ? 1 : 2, new Point(1, 0));
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

                g.setColor(Color.WHITE);
                g.setFont(new Font("Calibri", Font.BOLD, 11));
                g.drawString(r + ", " + c, x + 1, y + 10);

                g.setColor(Color.BLACK);
                g.fillRect(x + squareSize, 0, 1, windowSize.height);
                x += squareSize;
            }
            g.setColor(Color.BLACK);
            g.fillRect(0, y + squareSize, windowSize.width, 1);
            y += squareSize;
            x = 0;

            g.setColor(new Color(0, 10, 162));
            g.fillRect(playerPosition[0].x + 1 - 5, playerPosition[0].y, 8, 15);
            g.fillRect(playerPosition[1].x + 1 - 5, playerPosition[1].y, 8, 15);
            g.setColor(new Color(203, 174, 108));
            g.fillOval(playerPosition[0].x - 5, playerPosition[0].y - 5, 10, 10);
            g.fillOval(playerPosition[1].x - 5, playerPosition[1].y - 5, 10, 10);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 18));
        g.drawString("Player 1 Items: " + playerItems[0] + ", Health: " + playerHealth[0].size(), 10, 20);
        g.drawString("Player 2 Items: " + playerItems[1] + ", Health: " + playerHealth[1].size(), 10, 40);

        g.drawString("Player1Loc: " + playerPosition[0].toString(), 10, 60);
    }

    public void deductHealthPoint(int player) {
        playerHealth[player - 1].pop();
        Message msg = Message.createMessage(player, Message.Action.PlayerLostHealth, null);
        // TODO: Send message.
    }

    private boolean coordsWithin(Point location, Point topLeft, Point bottomRight) {
        if (location.x > topLeft.x && location.x < bottomRight.x &&
                location.y > topLeft.y && location.y < bottomRight.y) return true;
        else return false;
    }

    public void movePlayer(int player, Point direction) {
        Dimension move = null;
        if (direction.x == -1) {
//            if (player == 1) {
            if (playerPosition[player - 1].x < 60) return;

            int r = Math.round((float) playerPosition[player - 1].y / 60);
            int c = Math.round((float) playerPosition[player - 1].x / 60);
            System.out.println(r + "\t" + c);

            if (board.get(new Point(r, c - 1)).type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (board.get(new Point(r, c - 1)).type == Tile.Type.WATER) {
            } else move = new Dimension(-moveMagnitude, 0);
//            } else {
//                if(playerPosition[0].x < 60) return;
//
//                int r = Math.round((float) playerPosition[1].y / 60);
//                int c = Math.round((float) playerPosition[1].x / 60);
//                System.out.println(r + "\t" + c);
//
//                if (board.get(new Point(r - 1, c)).type == Tile.Type.MOUNTAIN) {
//                    if(playerHealth[player].size() != 0)
//                        deductHealthPoint(player);
//                } else if (board.get(new Point(r - 1, c)).type == Tile.Type.WATER) {
//                } else move = new Dimension(-moveMagnitude, 0);
//            }
        } else if (direction.x == 1) {
//            if (player == 1) {
            if (playerPosition[player - 1].x > getPreferredSize().width - 60) return;

            int r = Math.round((float) playerPosition[player - 1].y / 60);
            int c = Math.round((float) playerPosition[player - 1].x / 60);
//                System.out.println(r + "\t" + c);

            if (board.get(new Point(r + 1, c)).type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (board.get(new Point(r + 1, c)).type == Tile.Type.WATER) {
            } else move = new Dimension(moveMagnitude, 0);
//            } else {
//                if(playerPosition[0].x > getPreferredSize().width - 60) return;
//
//                int r = Math.round((float) playerPosition[1].y / 60);
//                int c = Math.round((float) playerPosition[1].x / 60);
////                System.out.println(r + "\t" + c);
//
//                if (board.get(new Point(r - 1, c)).type == Tile.Type.MOUNTAIN) {
//                    if(playerHealth[player].size() != 0)
//                        deductHealthPoint(player);
//                } else if (board.get(new Point(r - 1, c)).type == Tile.Type.WATER) {
//                } else move = new Dimension(moveMagnitude, 0);
//            }
        } else if (direction.y == -1) {
            move = new Dimension(0, moveMagnitude);
        } else if (direction.y == 1) {
            move = new Dimension(0, -moveMagnitude);
        }

        if (player == 1 && move != null) playerPosition[0].translate(move.width, move.height);
        if (player == 2 && move != null) playerPosition[1].translate(move.width, move.height);

        // Detection for item pickup
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Tile temp = board.get(new Point(r, c));
                Point topLeft = new Point(c * getPreferredSize().height / 10, r * getPreferredSize().height / 10);
                Point bottomRight = new Point((c + 1) * getPreferredSize().height / 10, (r + 1) * getPreferredSize().height / 10);

//                System.out.println(System.currentTimeMillis()/1000.0 + "\t" + new Point(r, c).toString() + "\t" +
//                        coordsWithin(player1Location, topLeft, bottomRight) + "\t" + player1Location + "\t" + topLeft + "\t" + bottomRight + "\t" + temp.hasItem);

                if (player == 1 && coordsWithin(playerPosition[0], topLeft, bottomRight) && temp.hasItem) {
                    System.out.println("Player1 gets item.");
                    temp.hasItem = false;
                    playerItems[0]++;

                    board.put(new Point(r, c), temp);
                    Message msg = Message.createMessage(player, Message.Action.PlayerGotItem, null);
                    // TODO: Send message.
                } else if (player == 2 && coordsWithin(playerPosition[1], topLeft, bottomRight) && temp.hasItem) {
                    System.out.println("Player2 gets item.");
                    temp.hasItem = false;
                    playerItems[1]++;

                    board.put(new Point(r, c), temp);
                    Message msg = Message.createMessage(player, Message.Action.PlayerGotItem, null);
                    // TODO: Send message.
                }
            }
        }

        if (move != null) {
            Message msg = Message.createMessage(player, Message.Action.PlayerMoved, playerPosition[player - 1]);
            // TODO: Send message.
        }
    }
}
