import javax.swing.*;

public class Server {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ready Player 1!");
        frame.add(new Screen(true));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
