import javax.swing.*;

public class ClientRunner {
    public static void main(String[] args) {
        Client client = new Client();
        JFrame frame = new JFrame("Client");

        frame.add(client);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}