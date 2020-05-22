import java.io.*;
import java.net.*;

public class TestClient {
    private static final String hostName = "localhost";
    private static final int portNumber = 1024;

    public static void main(String[] args) {
        try {
            Socket sock = new Socket(hostName, portNumber);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

            // Read player number from server.
            System.out.println("Waiting for player number...");
            in.readObject();
            System.out.println("Received player number.");

            Thread msgWatcher = new Thread(() -> {
                while (true) {
                    try {
                        Message msg = (Message) in.readObject();
                    } catch (IOException err) {
                        System.out.println("Watcher caught IOException: " + err.getMessage());
                        System.exit(1);
                    } catch (ClassNotFoundException err) {
                        System.out.println("Watcher couldn't convert received object to Message!");
                        System.exit(1);
                    }
                }
            });
            msgWatcher.start();
        } catch (UnknownHostException err) {
            System.out.println("Client caught UnknownHostException: " + err.getMessage());
            System.exit(1);
        } catch (IOException err) {
            System.out.println("Client caught IOException: " + err.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException err) {
            System.out.println("Client caught ClassNotFoundException: " + err.getMessage());
            System.exit(1);
        }
    }
}
