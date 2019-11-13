import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    private static int port = 1024;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);

            System.out.println("Waiting for a client...");
            Socket clientHandle = serverSocket.accept();
            System.out.println("Client connected...\n");

            ObjectOutputStream objOut = new ObjectOutputStream(clientHandle.getOutputStream());
            ObjectInputStream objIn = new ObjectInputStream(clientHandle.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(clientHandle.getOutputStream());
            DataInputStream dataIn = new DataInputStream(clientHandle.getInputStream());

            HashSet<Student> students = new HashSet<>();

            while (true) {
                System.out.println("Waiting for a message");
                int msg = dataIn.readInt();
                System.out.println("Message received: " + msg);

                if (msg == 0) break;
                else if (msg == 1) { // List
                    System.out.println("Received list request");
                    objOut.reset();
                    objOut.writeObject(students);
                } else if (msg == 2) { // Add
                    System.out.println("Received add request");
                    String name = dataIn.readUTF();
                    int id = dataIn.readInt();

                    students.add(new Student(name, id));

                    objOut.reset();
                    objOut.writeObject(students);
                } else if (msg == 3) { // Remove
                    System.out.println("Received remove request");
                    String name = dataIn.readUTF();
                    int id = dataIn.readInt();
                    Student query = new Student(name, id);

                    if (students.contains(query))
                        students.removeIf(o -> o.equals(query));

                    objOut.reset();
                    objOut.writeObject(students);
                } else dataOut.writeUTF("ERR! Invalid instruction!");

                System.out.println();
            }

            objOut.close();
            objIn.close();
            dataOut.close();
            dataIn.close();
            clientHandle.close();
        } catch (IOException err) {
            System.out.println(err.toString());
        }
    }
}
