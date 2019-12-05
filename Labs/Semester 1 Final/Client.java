import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ready Player 2!");
        frame.add(new Screen(false));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
