import javax.swing.*;

public class ServerRunner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ready Player 1!");
        ServerScreen sc = new ServerScreen();
        frame.add(sc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
