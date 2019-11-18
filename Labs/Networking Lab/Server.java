import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class Server extends JPanel implements ActionListener, MouseListener {
    private static final int itemsWidth = 150;
    private static final int port = 1024;

    private JButton restartGame;

    private GameState game;
    private ServerSocket serverSock;
    private Socket clientSock;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private enum Sounds {
        GAME_LOST("game_lost.wav"),
        GAME_TIED("game_tied.wav"),
        GAME_WON("game_won.wav"),
        MOVES_O("moves_o.wav"),
        MOVES_X("moves_x.wav");

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

    public Server() {
        this.setLayout(null);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(800, 600));

        game = new GameState(new Point(100, 100));
        try {
            serverSock = new ServerSocket(port);
            serverSock.setSoTimeout(10000);

            System.out.println("Waiting for a client...");
            clientSock = serverSock.accept();
            System.out.println("Client connected...\n");

            out = new ObjectOutputStream(clientSock.getOutputStream());
            in = new ObjectInputStream(clientSock.getInputStream());
        } catch (IOException err) {
            System.out.println(err.toString());
        }

        Thread listener = new Thread(() -> {
            try {
                while (true) {
                    game = (GameState) in.readObject();
                    System.out.println("New Board Received");

                    if (game.state == GameState.State.OVER) {
                        switch (game.checkTicTackToe()) {
                            case 1: // Player 1 won. Will never happen.
                                Sounds.GAME_WON.play();
                                break;
                            case 2: // Player 2 won.
                                Sounds.GAME_LOST.play();
                                break;
                            case 0: // Tie
                                Sounds.GAME_TIED.play();
                                break;
                        }
                        restartGame.setVisible(true);
                    } else Sounds.MOVES_O.play();
                    if (restartGame.isVisible() && game.state != GameState.State.OVER)
                        restartGame.setVisible(false);
                    repaint();
                }
            } catch (IOException | ClassNotFoundException err) {
                System.out.println(err.toString());
            }
        });
        listener.start();

        addMouseListener(this);

        restartGame = new JButton("Restart Game");
        restartGame.setBounds(100, 15, (int) itemsWidth, 30);
        restartGame.addActionListener(this);
        restartGame.setVisible(false);
        add(restartGame);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("Painting Screen");

        int textVerticalOffset = -7;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("You are player 1 (X). " + (game.checkTicTackToe() == 0
                        ? "It is player " + (game.state == GameState.State.TURN1 ? "X's" : "O's") + " turn."
                        : "Player " + (game.checkTicTackToe() == 1 ? "X" : "O") + " won the game!"),
                100, 100 + textVerticalOffset);

        g.drawString("Number of Wins:", 450, 100);
        g.drawString("Player 1:" + game.getPlayer1Wins(), 460, 130);
        g.drawString("Player 2:" + game.getPlayer2Wins(), 460, 160);

        game.drawBoard(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartGame) {
            restartGame.setVisible(true);
            game.reset();

            try {
                out.writeObject(game);
                out.reset();
            } catch (IOException err) {
                System.out.println(err.toString());
            }
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.state == GameState.State.TURN1) {
            if (game.handleClick(e.getPoint())) {
                System.out.println("Board Changed");
                Sounds.MOVES_X.play();

                if (game.state != GameState.State.OVER && (game.checkTicTackToe() == 1 || game.checkFull())) {
                    game.state = GameState.State.OVER;
                    game.addWin(1);
                    switch (game.checkTicTackToe()) {
                        case 1: // Player 1 won.
                            Sounds.GAME_WON.play();
                            break;
                        case 2: // Player 2 won. Will never happen.
                            Sounds.GAME_LOST.play();
                            break;
                        case 0: // Tie
                            Sounds.GAME_TIED.play();
                            break;
                    }
                    restartGame.setVisible(true);
                }

                try {
                    out.writeObject(game);
                    out.reset();
                } catch (IOException err) {
                    System.out.println(err.toString());
                }
                repaint();
            }
        } else System.out.println("Turn is of: " + game.state.name());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ready Player 1!");
        frame.add(new Server());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
