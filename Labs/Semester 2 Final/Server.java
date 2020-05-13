import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

        System.out.println("Player number " + players.size() + ", id " + (players.size() - 1) + "added.");
        socks.add(sock);
        try {
            out.add(new ObjectOutputStream(sock.getOutputStream()));
            in.add(new ObjectInputStream(sock.getInputStream()));

            Thread.sleep(50);

            // Send the new player their player number (0 to 3).
            out.get(out.size() - 1).writeObject(out.size() - 1);
        } catch (IOException err) {
            System.out.println("Thread caught IOException: " + err.getMessage());
        } catch (InterruptedException ignored) {
        }
    }

    public boolean readyToStart() {
        // Only send the signal once, not every time (players.size() >= 2).
        return players.size() == 2;
    }

    public void run() {
//        try {
        while (true) {
            for (int currentPlayer = 0; currentPlayer < players.size(); currentPlayer++) {
                { // Generate movement card for player, send message to all players.
                    Message msg = new Message();
                    msg.type = Message.Type.PlayerTurn;
                    msg.playerNum = currentPlayer;
                    msg.players = players.asArray();
                    msg.card = Message.Cards.selectCard();
                }

                // TODO: Get updated board from player.
                // TODO: Update other players with board.
            }
        }
//        } catch (InterruptedException err) {
//            System.out.println("Thread caught InterruptedException: " + err.getMessage());
//        }
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
                if (game.readyToStart()) thread.start();
            }
        } catch (IOException err) {
            System.out.println("Client caught IOException: " + err.getMessage());
        }
    }
}
