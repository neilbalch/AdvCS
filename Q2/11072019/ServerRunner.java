import javax.swing.*;
import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        JFrame frame = new JFrame("Server");

        frame.add(server);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}