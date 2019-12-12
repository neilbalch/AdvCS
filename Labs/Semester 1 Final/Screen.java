import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Screen extends JPanel {
    private final int port = 1024;
    private final int moveMagnitude = 3;
    private final boolean amIPlayer1;

    private HashMap<Point, Tile> board;

    private Stack<Integer>[] playerHealth;
    private Point[] playerPosition;
    private int[] playerItems;

    private boolean instanceIsServer;
    private ServerSocket sSocket;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private enum Sounds {
        COLLECT_ITEM("collect_item.wav"),
        HEALTH_LOST("health_lost.wav");

        // Nested class for specifying volume
        public static enum Volume {MUTE, LOW, MEDIUM, HIGH}

        public static Sounds.Volume volume = Sounds.Volume.LOW;

        // Each sound effect has its own clip, loaded with its own sound file.
        private Clip clip;

        Sounds(String soundFileName) {
            try {
                // Use URL (instead of File) to read from disk and JAR.
                URL url = this.getClass().getClassLoader().getResource(soundFileName);
                // Set up an audio input stream piped from the sound file.
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                // Get a clip resource.
                clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioInputStream);
            } catch (UnsupportedAudioFileException e) {
//                e.printStackTrace();
                System.out.println(e.toString());
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println(e.toString());
            } catch (LineUnavailableException e) {
//                e.printStackTrace();
                System.out.println(e.toString());
            }
        }

        // Play or Re-play the sound effect from the beginning, by rewinding.
        public void play() {
            if (volume != Sounds.Volume.MUTE) {
                if (clip.isRunning())
                    clip.stop();   // Stop the player if it is still running
                clip.setFramePosition(0); // rewind to the beginning
                clip.start();     // Start playing
            }
        }

        // Optional static method to pre-load all the sound files.
        static void init() {
            values(); // calls the constructor for all the elements
        }
    }

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
            {
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
            }

            try {
                sSocket = new ServerSocket(port);
                sSocket.setSoTimeout(10000);

                System.out.println("Waiting for a client...");
                socket = sSocket.accept();
                System.out.println("Client connected...\n");

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                out.reset();
                out.writeObject(board);
            } catch (IOException err) {
                System.out.println(err.toString());
            }
        } else {
            try {
                socket = new Socket("localhost", port);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                try {
//                    Object o = in.readObject();
//                    System.out.println(o);
//                    HashMap<Point, Tile> conv = (HashMap<Point, Tile>)o;
//                    System.out.println(conv);

                    board = (HashMap<Point, Tile>) in.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println(board);
            } catch (IOException err) {
                System.out.println(err.toString());
            }
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

        Thread receiver = new Thread(() -> {
            while (true) {
                try {
                    Message msg = (Message) in.readObject();
//                    System.out.println("Message Received! " + msg.action.toString());

                    switch (msg.action) {
                        case PlayerLostHealth:
                            playerHealth[msg.player - 1].pop();
                            break;
                        case PlayerGotItem:
                            playerItems[msg.player - 1]++;
                            board.get(msg.mapCoord).hasItem = false;
                            break;
                        case PlayerMoved:
                            playerPosition[msg.player - 1] = msg.newLocation;
                            break;
                        default:
                            System.out.println("ERR: Unidentified message type received!");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                repaint();
            }
        });
        receiver.start();
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
            g.setColor(new Color(203, 174, 108));
            g.fillOval(playerPosition[0].x - 5, playerPosition[0].y - 5, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.BOLD, 10));
            g.drawString("1", playerPosition[0].x + 2 - 5, playerPosition[0].y + 15);

            g.setColor(new Color(0, 10, 162));
            g.fillRect(playerPosition[1].x + 1 - 5, playerPosition[1].y, 8, 15);
            g.setColor(new Color(203, 174, 108));
            g.fillOval(playerPosition[1].x - 5, playerPosition[1].y - 5, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.BOLD, 10));
            g.drawString("2", playerPosition[1].x + 2 - 5, playerPosition[1].y + 15);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", instanceIsServer ? Font.BOLD : Font.PLAIN, 18));
        g.drawString("Player 1 Items: " + playerItems[0] + ", Health: " + playerHealth[0].size(), 10, 20);
        g.setFont(new Font("Calibri", instanceIsServer ? Font.PLAIN : Font.BOLD, 18));
        g.drawString("Player 2 Items: " + playerItems[1] + ", Health: " + playerHealth[1].size(), 10, 40);

        g.setFont(new Font("Calibri", Font.BOLD, 18));
        g.drawString("Player1Loc: " + playerPosition[0].toString(), 10, 60);
    }

    public void deductHealthPoint(int player) {
        playerHealth[player - 1].pop();
        Message msg = Message.createMessage(player, Message.Action.PlayerLostHealth, null, null);
        try {
            out.reset();
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sounds.HEALTH_LOST.play();
    }

    private boolean coordsWithin(Point location, Point topLeft, Point bottomRight) {
        if (location.x > topLeft.x && location.x < bottomRight.x &&
                location.y > topLeft.y && location.y < bottomRight.y) return true;
        else return false;
    }

    public void movePlayer(int player, Point direction) {
        Dimension move = null;
        //TODO: Close the gap between a no-no tile and the player... currently is the middle of a tile, needs to be closer to the offending edge.
        if (direction.x == -1) {
            if (playerPosition[player - 1].x < 60) return;

            int r = (int) ((float) playerPosition[player - 1].y / 60);
            int c = (int) ((float) playerPosition[player - 1].x / 60);
//            System.out.println(r + "\t" + c);

            Tile queryTile = board.get(new Point(r, c - 1));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.WATER) {
            } else move = new Dimension(-moveMagnitude, 0);
        } else if (direction.x == 1) {
            if (playerPosition[player - 1].x > getPreferredSize().width - 60) return;

            int r = (int) ((float) playerPosition[player - 1].y / 60);
            int c = (int) ((float) playerPosition[player - 1].x / 60);
//                System.out.println(r + "\t" + c);

            Tile queryTile = board.get(new Point(r, c + 1));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.WATER) {
            } else move = new Dimension(moveMagnitude, 0);
        } else if (direction.y == -1) {
            if (playerPosition[player - 1].y > getPreferredSize().height - 60) return;

            int r = (int) ((float) playerPosition[player - 1].y / 60);
            int c = (int) ((float) playerPosition[player - 1].x / 60);
//            System.out.println(r + "\t" + c);

            Tile queryTile = board.get(new Point(r + 1, c));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.WATER) {
            } else move = new Dimension(0, moveMagnitude);
        } else if (direction.y == 1) {
            if (playerPosition[player - 1].y < 60) return;

            int r = (int) ((float) playerPosition[player - 1].y / 60);
            int c = (int) ((float) playerPosition[player - 1].x / 60);
//            System.out.println(r + "\t" + c);

            Tile queryTile = board.get(new Point(r - 1, c));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.WATER) {
            } else move = new Dimension(0, -moveMagnitude);
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
                    Message msg = Message.createMessage(player, Message.Action.PlayerGotItem, null, new Point(r, c));
                    try {
                        out.reset();
                        out.writeObject(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Sounds.COLLECT_ITEM.play();
                } else if (player == 2 && coordsWithin(playerPosition[1], topLeft, bottomRight) && temp.hasItem) {
                    System.out.println("Player2 gets item.");
                    temp.hasItem = false;
                    playerItems[1]++;

                    board.put(new Point(r, c), temp);
                    Message msg = Message.createMessage(player, Message.Action.PlayerGotItem, null, new Point(r, c));
                    try {
                        out.reset();
                        out.writeObject(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Sounds.COLLECT_ITEM.play();
                }
            }
        }

        if (move != null) {
            Message msg = Message.createMessage(player, Message.Action.PlayerMoved, playerPosition[player - 1], null);
            try {
                out.reset();
                out.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
