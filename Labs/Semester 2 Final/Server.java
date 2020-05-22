import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

class ServerThread implements Runnable {
    private DLList<Player> players;
    private DLList<Socket> socks;
    private DLList<ObjectInputStream> in;
    private DLList<ObjectOutputStream> out;

    public ServerThread() {
        players = new DLList<>();
        socks = new DLList<>();
        in = new DLList<>();
        out = new DLList<>();
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
        } catch (IOException err) {
            logMsg("Thread caught IOException: " + err.getMessage());
        } catch (InterruptedException ignored) {
        }
    }

    public boolean readyToStart() {
        // Only send the signal once, not every time (players.size() >= 2).
        return players.size() == 2;
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
                for (int currentPlayer = 0; currentPlayer < players.size(); /*currentPlayer++*/) {
                    { // Generate movement card for player, send message to all players.
                        Player[] playersArr = new Player[players.size()];
                        for (int i = 0; i < playersArr.length; i++) playersArr[i] = players.get(i);

                        Message msg = new Message();
                        msg.type = Message.Type.PlayerTurn;
                        msg.playerNum = currentPlayer;
                        msg.players = playersArr;
                        // TODO: Undo this when done testing.
//                        msg.card = Message.Card.selectCard();
                        msg.card = Message.Card.ONE;

                        for (int i = 0; i < socks.size(); i++) {
                            logMsg("PlayerTurn sent to player number " + players.size() + ", id " + (players.size() - 1) + ".");
                            out.get(i).writeObject(msg);
                        }
                    }

                    // Get updated board from player, update other players, and update local board.
                    {
                        Message msg = (Message) in.get(currentPlayer).readObject();

                        players = new DLList<>();
                        for (Player p : msg.players) players.add(p);

                        for (int i = 0; i < socks.size(); i++) {
                            if (i == currentPlayer) continue;

                            logMsg("PlayerMadeMove echoed to player number " + players.size() + ", id " + (players.size() - 1) + ".");
                            out.get(i).writeObject(msg);
                        }
                    }

                    logMsg("Board after last move:");
                    for (int i = 0; i < players.size(); i++)
                        logMsg(players.get(i).toString());
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

public class Server {
    private static final int portNumber = 1024;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server ready for up to 4 connections @ " + serverSocket.getInetAddress().getHostAddress() + ":" + portNumber);
            System.out.println("WARNING: Only the first 4 clients to connect will be served!");

            ServerThread game = new ServerThread();
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
