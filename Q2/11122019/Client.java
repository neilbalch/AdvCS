import java.io.*;
import java.net.Socket;
import java.util.HashSet;

public class Client {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 1024);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            ObjectOutputStream objOut = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
            DataInputStream dataIn = new DataInputStream(client.getInputStream());

            dataOut.writeInt(1);
            System.out.println("List request sent");
            System.out.println((HashSet<Student>) objIn.readObject());

            dataOut.writeInt(2);
            dataOut.writeUTF("Joe Schmo");
            dataOut.writeInt(1234);
            System.out.println("Student add request sent");
            System.out.println((HashSet<Student>) objIn.readObject());

            dataOut.writeInt(0);

            objOut.close();
            objIn.close();
            dataOut.close();
            dataIn.close();
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
}
