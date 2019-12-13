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

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private enum Sounds {
        COLLECT_ITEM("collect_item.wav"),
        HEALTH_LOST("health_lost.wav");

        // Nested class for specifying volume
        public enum Volume {MUTE, LOW, MEDIUM, HIGH}
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
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
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

    public Screen(boolean amIPlayer1) {
        this.setLayout(null);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(600, 600));
        this.amIPlayer1 = amIPlayer1;

        board = new HashMap<Point, Tile>();

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


        // Generate new board.
        if (amIPlayer1) {
            int numOtherAreas = 10, numMountainAreas = 10, numGems = 11;
            ArrayList<Point> waterList = new ArrayList<>(), mountainList = new ArrayList<>(), gemsList = new ArrayList<>();
            Point temp;
            for (int i = 0; i < numOtherAreas; i++) {
                do {
                    temp = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
                } while (waterList.contains(temp));
                waterList.add(temp);
            }
            for (int i = 0; i < numMountainAreas; i++) {
                do {
                    temp = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
                } while (waterList.contains(temp) || mountainList.contains(temp));
                mountainList.add(temp);
            }
            for (int i = 0; i < numGems; i++) {
                do {
                    temp = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
                } while (waterList.contains(temp) || mountainList.contains(temp) || gemsList.contains(temp));
                gemsList.add(temp);
            }

            for (int r = 0; r < 10; r++) {
                for (int c = 0; c < 10; c++) {
                    if (mountainList.contains(new Point(r, c))) {
                        board.put(new Point(r, c), Tile.createTile(Tile.Type.MOUNTAIN, gemsList.contains(new Point(r, c))));
                    } else if (waterList.contains(new Point(r, c))) {
                        board.put(new Point(r, c), Tile.createTile(Tile.Type.UNTRAVERSABLE, gemsList.contains(new Point(r, c))));
                    } else {
                        board.put(new Point(r, c), Tile.createTile(Tile.Type.LAND, gemsList.contains(new Point(r, c))));
                    }
                }
            }
        }

        // Establish communications between Server & Client.
        try {
            Socket socket;
            if (amIPlayer1) {
                ServerSocket sSocket = new ServerSocket(port);
                sSocket.setSoTimeout(10000);

                System.out.println("Waiting for a client...");
                socket = sSocket.accept();
                System.out.println("Client connected\n");
            } else socket = new Socket("localhost", port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            if (amIPlayer1) {
                out.reset();
                out.writeObject(board);
            } else board = (HashMap<Point, Tile>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'w') {
                    movePlayer(Screen.this.amIPlayer1 ? 1 : 2, new Point(0, 1));
                } else if (e.getKeyChar() == 's') {
                    movePlayer(Screen.this.amIPlayer1 ? 1 : 2, new Point(0, -1));
                } else if (e.getKeyChar() == 'a') {
                    movePlayer(Screen.this.amIPlayer1 ? 1 : 2, new Point(-1, 0));
                } else if (e.getKeyChar() == 'd') {
                    movePlayer(Screen.this.amIPlayer1 ? 1 : 2, new Point(1, 0));
                }

                repaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        Thread messageReceiver = new Thread(() -> {
            while (true) {
                try {
                    Message msg = (Message) in.readObject();

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
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                repaint();
            }
        });
        messageReceiver.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension windowSize = this.getPreferredSize();
        int squareSize = windowSize.height / 10;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, windowSize.width, windowSize.height);

        int x = 0, y = 0;
        g.setColor(Color.BLACK);
        g.fillRect(x, 0, 1, windowSize.height);
        g.fillRect(0, y, windowSize.width, 1);
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                board.get(new Point(r, c)).draw(g, new Point(x + 1, y + 1), squareSize);

                // Map square labels
//                g.setColor(Color.WHITE);
//                g.setFont(new Font("Calibri", Font.BOLD, 11));
//                g.drawString(r + ", " + c, x + 1, y + 10);

                // Vertical divider lines
                g.setColor(Color.BLACK);
                g.fillRect(x + squareSize, 0, 1, windowSize.height);
                x += squareSize;
            }

            // Horizontal divider lines
            g.setColor(Color.BLACK);
            g.fillRect(0, y + squareSize, windowSize.width, 1);
            y += squareSize;
            x = 0;

            // Draw Player 1
            g.setColor(new Color(0, 10, 162));
            g.fillRect(playerPosition[0].x + 1 - 5, playerPosition[0].y, 8, 15);
            g.setColor(new Color(203, 174, 108));
            g.fillOval(playerPosition[0].x - 5, playerPosition[0].y - 5, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.BOLD, 10));
            g.drawString("1", playerPosition[0].x + 2 - 5, playerPosition[0].y + 15);

            // Draw Player 2
            g.setColor(new Color(0, 10, 162));
            g.fillRect(playerPosition[1].x + 1 - 5, playerPosition[1].y, 8, 15);
            g.setColor(new Color(203, 174, 108));
            g.fillOval(playerPosition[1].x - 5, playerPosition[1].y - 5, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.BOLD, 10));
            g.drawString("2", playerPosition[1].x + 2 - 5, playerPosition[1].y + 15);
        }

        // Scoreboard?
        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", amIPlayer1 ? Font.BOLD : Font.PLAIN, 18));
        g.drawString("Player 1 Items: " + playerItems[0] + ", Health: " + playerHealth[0].size(), 10, 20);
        g.setFont(new Font("Calibri", amIPlayer1 ? Font.PLAIN : Font.BOLD, 18));
        g.drawString("Player 2 Items: " + playerItems[1] + ", Health: " + playerHealth[1].size(), 10, 40);
    }

    private void writeMsg(Message msg) {
        try {
            out.reset();
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deductHealthPoint(int player) {
        playerHealth[player - 1].pop();
        writeMsg(Message.createMessage(player, Message.Action.PlayerLostHealth, null, null));

        playerPosition[player - 1] = new Point(300, 300);
        writeMsg(Message.createMessage(player, Message.Action.PlayerMoved, playerPosition[player - 1], null));
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
        int player_r = (int) ((float) playerPosition[player - 1].y / 60);
        int player_c = (int) ((float) playerPosition[player - 1].x / 60);
//        System.out.println(player_r + "\t" + player_c);

        if (direction.x == -1) {
            if (playerPosition[player - 1].x < 60) return;

            Tile queryTile = board.get(new Point(player_r, player_c - 1));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.UNTRAVERSABLE) {
            } else move = new Dimension(-moveMagnitude, 0);
        } else if (direction.x == 1) {
            if (playerPosition[player - 1].x > getPreferredSize().width - 60) return;

            Tile queryTile = board.get(new Point(player_r, player_c + 1));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.UNTRAVERSABLE) {
            } else move = new Dimension(moveMagnitude, 0);
        } else if (direction.y == -1) {
            if (playerPosition[player - 1].y > getPreferredSize().height - 60) return;

            Tile queryTile = board.get(new Point(player_r + 1, player_c));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.UNTRAVERSABLE) {
            } else move = new Dimension(0, moveMagnitude);
        } else if (direction.y == 1) {
            if (playerPosition[player - 1].y < 60) return;

            Tile queryTile = board.get(new Point(player_r - 1, player_c));
            if (queryTile.type == Tile.Type.MOUNTAIN) {
                if (playerHealth[player - 1].size() != 0)
                    deductHealthPoint(player);
            } else if (queryTile.type == Tile.Type.UNTRAVERSABLE) {
            } else move = new Dimension(0, -moveMagnitude);
        }

        if (move != null) playerPosition[player - 1].translate(move.width, move.height);

        // Detection for item pickup
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Tile temp = board.get(new Point(r, c));
                Dimension window = getPreferredSize();
                Point topLeft = new Point(c * window.height / 10, r * window.height / 10);
                Point bottomRight = new Point((c + 1) * window.height / 10, (r + 1) * window.height / 10);

                if (coordsWithin(playerPosition[player - 1], topLeft, bottomRight) && temp.hasItem) {
                    temp.hasItem = false;
                    playerItems[player - 1]++;

                    board.put(new Point(r, c), temp);
                    writeMsg(Message.createMessage(player, Message.Action.PlayerGotItem, null, new Point(r, c)));
                    Sounds.COLLECT_ITEM.play();
                }
            }
        }

        // Send player move
        if (move != null)
            writeMsg(Message.createMessage(player, Message.Action.PlayerMoved, playerPosition[player - 1], null));
    }
}
