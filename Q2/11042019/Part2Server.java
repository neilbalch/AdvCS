import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Part2Server extends Thread {
    private ServerSocket serverSocket;

    public Part2Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                HashMap<String, String> responses = new HashMap<>();
                responses.put("How do you make holy water?", "You boil the hell out of it.");
                responses.put("Why did the chicken cross the road?", "To get to the other side!");
                responses.put("What do you call cheese that's not yours?", "Nacho cheese!");
                responses.put("What do elves learn in school?", "The elf-abet.");
                responses.put("What do you call a fake noodle?", "An impasta!");

                while (true) {
                    System.out.println("Waiting for a message: ");
                    String message = in.readUTF();
                    if (message.equalsIgnoreCase("bye")) break;

                    boolean found = false;
                    Iterator iter = responses.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry pair = (Map.Entry) iter.next();
                        if (((String) pair.getKey()).equalsIgnoreCase(message)) {
                            out.writeUTF((String) pair.getValue());
                            found = true;
                        }
                    }
                    if (!found) out.writeUTF("Not an option!");
                }

//                System.out.println(in.readUTF());
//                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
//                        + "\nGoodbye!");

//                out.writeUTF("none more");
                server.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
//        int port = Integer.parseInt(args[0]);
        int port = 1025;

        try {
            Thread t = new Part2Server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
