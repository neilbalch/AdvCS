import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);
        DataInputStream in = new DataInputStream(serverSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());

        try {
            Scanner sc = new Scanner(System.in);

            while (!serverSocket.isClosed()) {
                System.out.println("Server: " + in.readUTF());
                System.out.print("Client: ");
                out.writeUTF(sc.nextLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error connecting to " + hostName);
            System.exit(1);
        }
    }
}
