import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JPanel {
    private final String hostName = "localhost";
    private final int portNumber = 1024;

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        setPreferredSize(new Dimension(800, 600));

        try {
            sock = new Socket(hostName, portNumber);
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());

//            while (!sock.isClosed()) {
//                System.out.println("Server: " + in.readUTF());
//                System.out.print("Client: ");
//                out.writeUTF(sc.nextLine());
//            }
        } catch (UnknownHostException err) {
            System.out.println("Client caught UnknownHostException: " + err.getMessage());
            System.exit(1);
        } catch (IOException err) {
            System.out.println("Client caught IOException: " + err.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorry!");
        frame.add(new Client());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
