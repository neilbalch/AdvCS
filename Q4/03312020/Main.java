import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
    private BTree<Integer> tree;

    public Main() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        tree = new BTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(1);
        tree.add(9);
        tree.add(11);
        tree.add(19);
    }

    public void paintComponent(Graphics g) {
        tree.drawTree(g, new Point(400, 25));
    }

    public static void main(String[] args) {
        Main game = new Main();
        JFrame frame = new JFrame("BTree Visualization");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}