import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JPanel {
    private BTree<Integer> tree;
    private JTextField numberField;
    private JButton addBtn;

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
        tree.add(19);
        tree.add(23);
        tree.add(4);

        numberField = new JTextField();
        numberField.setBounds(10, getPreferredSize().height - 15 - 30, 100, 30);
        add(numberField);

        addBtn = new JButton("Add Number");
        addBtn.setBounds(10 + 100 + 10, getPreferredSize().height - 15 - 30, 125, 30);
        add(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int number = Integer.parseInt(numberField.getText());
                    tree.add(number);
                    repaint();
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(null, "ERROR: Number must be an integer!");
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

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