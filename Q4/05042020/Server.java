import java.net.*;
import java.io.*;
import java.util.HashMap;

class ServerThread implements Runnable {
    private final Socket clientSocket;
    private HashMap<String, String> locations;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;

        locations = new HashMap<>();
        locations.put("Statue of Liberty", "United States");
        locations.put("Tower of London", "United Kingdom");
        locations.put("Alhambra", "Spain");
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + ": connection opened.");
        try {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            int question = (int) (Math.random() * locations.size());
            String location = (String) locations.keySet().toArray()[question];
            out.writeUTF("In what country is the " + location + "?");

            while (true) {
                if (in.readUTF().equalsIgnoreCase(locations.get(location))) {
                    out.writeUTF("Correct!");
                    break;
                } else out.writeUTF("Try again.");
            }

            // Clears and close the output stream.
            out.flush();
            out.close();
            clientSocket.close();
            System.out.println(Thread.currentThread().getName() + ": connection closed.");
        } catch (IOException ex) {
            System.out.println("Error listening for a connection");
            System.out.println(ex.getMessage());
        }
    }
}

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 1024;
        ServerSocket serverSocket = new ServerSocket(portNumber);

        // This loop will run and wait for one connection at a time.
        while (true) {
            System.out.println("Waiting for a connection");

            // Wait for a connection.
            Socket clientSocket = serverSocket.accept();

            // Once a connection is made, run the socket in a ServerThread.
            Thread thread = new Thread(new ServerThread(clientSocket));
            thread.start();
        }
    }
}
