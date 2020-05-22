import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

class TestServerThread implements Runnable {
    // volatile: https://www.javatpoint.com/volatile-keyword-in-java
    private volatile DLList<Player> players;
    private volatile DLList<Socket> socks;
    private volatile DLList<ObjectInputStream> in;
    private volatile DLList<ObjectOutputStream> out;
    private volatile Message lastMessageSent;

    public TestServerThread() {
        players = new DLList<>();
        socks = new DLList<>();
        in = new DLList<>();
        out = new DLList<>();
        lastMessageSent = null;
    }

    public void addPlayer(Socket sock) {
        switch (players.size()) {
            case 0:
                players.add(new Player(Player.RED));
                break;
            case 1:
                players.add(new Player(Player.BLUE));
                break;
            case 2:
                players.add(new Player(Player.YELLOW));
                break;
            case 3:
                players.add(new Player(Player.GREEN));
                break;
            default:
                return;
        }

        logMsg("Player number " + players.size() + ", id " + (players.size() - 1) + " added.");
        socks.add(sock);
        try {
            out.add(new ObjectOutputStream(sock.getOutputStream()));
            in.add(new ObjectInputStream(sock.getInputStream()));

            Thread.sleep(50);

            // Send the new player their player number (0 to 3).
            out.get(out.size() - 1).writeObject(out.size() - 1);

            // Update the last message sent with the new player array, send to all players.
            if (lastMessageSent != null) {
                Player[] playersArr = new Player[players.size()];
                for (int i = 0; i < playersArr.length; i++) playersArr[i] = players.get(i);
                lastMessageSent.players = playersArr;

                logMsg("Last sent message resent to all players because new player was added. New number of players is " + players.size());
                for (int i = 0; i < socks.size(); i++)
                    out.get(i).writeObject(lastMessageSent);
            }
        } catch (IOException err) {
            logMsg("Thread caught IOException: " + err.getMessage());
        } catch (InterruptedException ignored) {
        }
    }

    private void logMsg(Object msg) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.print(formatter.format(date) + " — ");
        System.out.println(msg.toString());
    }

    public void run() {
        try {
            // Game loop.
            while (true) {
                // Go through every player's turn:
                for (int currentPlayer = 0; currentPlayer < players.size(); currentPlayer++) {
                    System.out.println("Num players: " + players.size());
                    { // Generate movement card for player, send message to all players.
                        Player[] playersArr = new Player[players.size()];
                        for (int i = 0; i < playersArr.length; i++) playersArr[i] = players.get(i);

                        Message msg = new Message();
                        msg.type = Message.Type.PlayerTurn;
                        msg.playerNum = currentPlayer;
                        msg.players = playersArr;
                        msg.card = Message.Card.selectCard();
                        lastMessageSent = msg;

                        for (int i = 0; i < socks.size(); i++) {
                            logMsg("PlayerTurn sent to player number " + players.size() + ", id " + (players.size() - 1) + ".");
                            out.get(i).writeObject(msg);
                        }
                    }

                    // Get updated board from player, update other players, and update local board.
                    {
                        Message msg = (Message) in.get(currentPlayer).readObject();
                        lastMessageSent = msg;

                        players = new DLList<>();
                        for (Player p : msg.players) players.add(p);

                        for (int i = 0; i < socks.size(); i++) {
                            if (i == currentPlayer) continue;

                            logMsg("PlayerMadeMove echoed to player number " + players.size() + ", id " + (players.size() - 1) + ".");
                            out.get(i).writeObject(msg);
                        }
                    }
                }
            }
//        } catch (InterruptedException err) {
//            logMsg("Thread caught InterruptedException: " + err.getMessage());
        } catch (IOException err) {
            logMsg("Thread caught IOException: " + err.getMessage());
        } catch (ClassNotFoundException err) {
            logMsg("Thread caught ClassNotFoundException: " + err.getMessage());
        }
    }
}

public class TestServer {
    private static final int portNumber = 1024;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
//            System.out.println("Server ready for up to 4 connections @ " + serverSocket.getInetAddress().getHostAddress() + ":" + portNumber);
//            System.out.println("WARNING: Only the first 4 clients to connect will be served!");

            TestServerThread game = new TestServerThread();
            Thread thread = new Thread(game);

            while (true) {
                game.addPlayer(serverSocket.accept());
                if (!thread.isAlive()) thread.start();
            }
        } catch (IOException err) {
            System.out.println("Client caught IOException: " + err.getMessage());
        }
    }
}
