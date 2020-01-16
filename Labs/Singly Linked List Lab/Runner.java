import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class Runner extends JPanel implements ActionListener {
    private SLList shoppingList;
    private JList<ItemPair> listView;
    private JScrollPane listPane;

    private JButton addItem;
    private JButton removeItem;
    private JTextField itemName;
    private JTextField itemPrice;

    private JButton sortName;
    private JButton sortPrice;
    private JButton sortTime;

    public Runner() {
        shoppingList = new SLList();
        shoppingList.add(new ItemPair(new Item("Portobello Mushrooms", 1.25, LocalDateTime.now()), 1));
        shoppingList.add(new ItemPair(new Item("Marmite", 10.00, LocalDateTime.now()), 1));
        shoppingList.add(new ItemPair(new Item("Tangerines", 2.43, LocalDateTime.now()), 1));
        shoppingList.sortList();

        this.setPreferredSize(new Dimension(800, 600));
        this.setLayout(null);

        listView = new JList<>();
        listPane = new JScrollPane(listView);
        listPane.setLocation(120, 25);
        listPane.setSize(new Dimension(getPreferredSize().width - 120 - 25, getPreferredSize().height - 50));
        add(listPane);

        itemName = new JTextField();
        itemName.setBounds(10, 25, 100, 30);
        add(itemName);

        itemPrice = new JTextField();
        itemPrice.setBounds(10, 25 + 50, 100, 30);
        add(itemPrice);

        addItem = new JButton("Add");
        addItem.setBounds(10, 25 + 50 * 2, 100, 30);
        addItem.addActionListener(this);
        add(addItem);

        removeItem = new JButton("Remove");
        removeItem.setBounds(10, 25 + 50 * 3, 100, 30);
        removeItem.addActionListener(this);
        add(removeItem);

        sortName = new JButton("Name");
        sortName.setBounds(10, 25 + 50 * 5, 100, 30);
        sortName.addActionListener(this);
        add(sortName);

        sortPrice = new JButton("Price");
        sortPrice.setBounds(10, 25 + 50 * 6, 100, 30);
        sortPrice.addActionListener(this);
        add(sortPrice);

        sortTime = new JButton("Time");
        sortTime.setBounds(10, 25 + 50 * 7, 100, 30);
        sortTime.addActionListener(this);
        add(sortTime);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ItemPair[] tempList = new ItemPair[shoppingList.getSize()];
        for (int i = 0; i < tempList.length; i++) {
            tempList[i] = shoppingList.get(i);
        }
        listView.setListData(tempList);

        int textOffset = -7;
        g.drawString("Item Name", 10, 25 + textOffset);
        g.drawString("Item Price", 10, 25 + 50 + textOffset);
        g.drawString("Sort By:", 10, 25 + 50 * 5 + textOffset);

        double total = 0;
        for (int i = 0; i < shoppingList.getSize(); i++) {
            total += shoppingList.get(i).item.getPrice() * shoppingList.get(i).count;
        }

        g.drawString("Total: $" + total, 10, getPreferredSize().height - 25 + textOffset);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addItem) {
            try {
                Item newItem = new Item(itemName.getText(), Double.parseDouble(itemPrice.getText()), LocalDateTime.now());
                ItemPair newItemPair = new ItemPair(newItem, 1);
                if (shoppingList.contains(newItemPair)) {
                    int index = shoppingList.indexOf(newItemPair);
                    newItemPair = shoppingList.get(index);
                    newItemPair.count++;
                } else {
                    shoppingList.add(newItemPair);
                    shoppingList.sortList();
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Price must be a number!");
            }
        } else if (e.getSource() == removeItem) {
            try {
                Item newItem = new Item(itemName.getText(), Double.parseDouble(itemPrice.getText()), LocalDateTime.now());
                ItemPair newItemPair = new ItemPair(newItem, 1);
                if (shoppingList.contains(newItemPair)) {
                    shoppingList.remove(shoppingList.indexOf(newItemPair));
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: This item doesn't exist in the cart!");
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Price must be a number!");
            }
        } else if (e.getSource() == sortName) {
            shoppingList.setCurrentSortMethod(SLList.SortingMethods.NAME);
            shoppingList.sortList();
        } else if (e.getSource() == sortPrice) {
            shoppingList.setCurrentSortMethod(SLList.SortingMethods.PRICE);
            shoppingList.sortList();
        } else if (e.getSource() == sortTime) {
            shoppingList.setCurrentSortMethod(SLList.SortingMethods.TIME);
            shoppingList.sortList();
        }

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Shopping List Helper");
        frame.add(new Runner());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
