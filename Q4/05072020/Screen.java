import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Screen extends JPanel implements KeyListener {
    private ObjectOutputStream out;
    private int myID;
    private int playerIndex;
    private GameData gameData;

    public Screen() {
        this.setLayout(null);
        addKeyListener(this);

        myID = -1;
        playerIndex = -1;

        //sets keylistener
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameData != null) {
            g.setColor(Color.BLACK);
            g.fillRect(50, 50, 100, 100);

            //draw all players
            ArrayList<Player> playerList = gameData.getPlayerList();
            for (int i = 0; i < playerList.size(); i++) {
                int x = playerList.get(i).getX();
                int y = playerList.get(i).getY();
                int id = playerList.get(i).getID();

                g.setColor(Color.BLACK);
                g.drawString("" + id, x, y);

                if (id == myID) {
                    g.setColor(Color.RED);
                    g.fillOval(x, y, 30, 30);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillOval(x, y, 30, 30);
                }
            }
        }
    }

    // Returns whether or not the provided location is within the bounding box provided
    private boolean coordsWithin(Point location, Point topLeft, Point bottomRight) {
        if (location.x > topLeft.x && location.x < bottomRight.x &&
                location.y > topLeft.y && location.y < bottomRight.y) return true;
        else return false;
    }

    //implement methods of the KeyListener
    public void keyPressed(KeyEvent e) {
        if (gameData != null) {
            ArrayList<Player> playerList = gameData.getPlayerList();

            if (playerIndex != -1) {
                if (e.getKeyCode() == 87) { // w
                    playerList.get(playerIndex).moveUp();
                } else if (e.getKeyCode() == 83) { // s
                    playerList.get(playerIndex).moveDown();
                } else if (e.getKeyCode() == 65) { // a
                    playerList.get(playerIndex).moveLeft();
                } else if (e.getKeyCode() == 68) { // d
                    playerList.get(playerIndex).moveRight();
                }

                if (coordsWithin(new Point(playerList.get(playerIndex).getX(),
                                playerList.get(playerIndex).getY()),
                        new Point(50, 50),
                        new Point(150, 150))) {
                    playerList.get(playerIndex).setLocation(0, 0);
                }
            }
        }

        try {
            out.reset();
            out.writeObject(gameData);
        } catch (IOException ex) {
            System.out.println(ex);
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    @SuppressWarnings("unchecked")
    public void poll() throws IOException {
        String hostName = "localhost";
        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);

        out = new ObjectOutputStream(serverSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

        //listens for a stream
        try {
            //receive id from server
            myID = (Integer) in.readObject();
            System.out.println("my id is " + myID);

            while (true) {
                //wait for gameData object
                gameData = (GameData) in.readObject();

                //get for playerIndex of player
                ArrayList<Player> playerList = gameData.getPlayerList();
                for (int i = 0; i < playerList.size(); i++) {
                    int id = playerList.get(i).getID();
                    if (id == myID) {
                        playerIndex = i;
                        break;
                    }
                }

                //update screen
                repaint();
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
