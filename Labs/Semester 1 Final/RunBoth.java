import javax.swing.*;

public class RunBoth {
    public static void main(String[] args) {
        Thread server = new Thread(() -> {
            JFrame frame = new JFrame("Ready Player 1!");
            frame.add(new Screen(true));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });

        Thread client = new Thread(() -> {
            JFrame frame = new JFrame("Ready Player 2!");
            frame.add(new Screen(false));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
        server.start();
        client.start();
    }
}
