import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Part2Client {
    public static void main(String[] args) {
//        String serverName = args[0];
//        int port = Integer.parseInt(args[1]);
        String serverName = "localhost";
        int port = 1025;

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print("Type in a joke: ");
                String query = sc.nextLine();
                out.writeUTF(query);

                if (query.equalsIgnoreCase("bye")) break;

                String response = in.readUTF();
                System.out.println(response);
                System.out.println();
            }

//            out.writeUTF("Hello from " + client.getLocalSocketAddress());
//            InputStream inFromServer = client.getInputStream();
//            DataInputStream in = new DataInputStream(inFromServer);

//            System.out.println("Server says " + in.readUTF());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
