import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final Socket clientSocket;
    private final GameManager gm;
    private ObjectOutputStream out;
    private int myID;

    public ServerThread(Socket clientSocket, GameManager gm) {
        this.clientSocket = clientSocket;
        this.gm = gm;
        myID = -1;
    }

    public void sendGameData(GameData gameData) {
        if (out != null) {
            try {
                System.out.print("Sending GameData to player: " + myID + " ");
                out.reset();
                out.writeObject(gameData);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": connection opened.");

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println(Thread.currentThread().getName() + " Connection Successful. ");

            //add new player
            myID = gm.addNewPlayer();
            out.writeObject(myID);

            //send gameData to all clients
            gm.broadcastGameData();

            while (true) {
                System.out.println(" Waiting for gameData...");
                GameData gameData = (GameData) in.readObject();
                gm.setGameData(gameData);
                gm.broadcastGameData();
            }
        } catch (IOException ex) {
            System.out.println("Connection closed");

            gm.removePlayer(myID);
            gm.broadcastGameData();

            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }
}
