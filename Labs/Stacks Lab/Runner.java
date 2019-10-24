import javax.swing.JFrame;

public class Runner {
    public static void main(String[] args) {
        Game game = new Game();
        JFrame frame = new JFrame("Stacks Lab...............................................");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}