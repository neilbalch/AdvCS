import javax.swing.*;

public class ClientRunner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ready Player 2!");
        ClientScreen sc = new ClientScreen();
        frame.add(sc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
