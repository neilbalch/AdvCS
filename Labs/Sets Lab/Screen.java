import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

class Item implements Comparable<Item> {
    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " : $" + price;
    }

    public int hashCode() {
        int hashCode = 0;
        for (char c : name.toCharArray()) {
            hashCode += c;
        }
        hashCode += price * 100;

        return 37 * hashCode;
    }

    public boolean equals(Object item) {
        Item i = (Item) item;
        return i.getName().equalsIgnoreCase(name) && i.getPrice() == price;
    }

    public int compareTo(Item i) {
        if (name.equals(i.getName()) && price == i.getPrice()) return 0;
        else if (name.compareTo(i.getName()) == 0) return (int) (price - i.getPrice());
        else return name.compareTo(i.getName());
    }
}

class Pair<T, U> {
    public T t;
    public U u;

    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public String toString() {
        return t.toString() + ", Qty: " + u.toString();
    }
}

public class Screen extends JPanel implements ActionListener {
    private int itemsXPos = 275;
    private int itemsWidth = 150;
    private int items2ndColXPos = itemsXPos + itemsWidth + 50;

    private ArrayList<Pair<Item, Integer>> shoppingCart;
    private TreeSet<Item> itemsTS;
    private HashSet<Item> itemsHS;
    private String inputFilePath = "StoreA.txt";

    private JList<String> itemsDisplay;
    private JScrollPane itemsDisplayPane;
    private JList<String> cartDisplay;
    private JScrollPane cartDisplayPane;

    private JButton addItemBtn;
    private JTextField nameInput;
    private JTextField priceInput;
    private JTextField qtyInput;
//    private JTextField objectiveInput;

    private String[] formatTSasArray() {
        Object[] temp = itemsTS.toArray();
        ArrayList<String> returnable = new ArrayList<>(temp.length);
        for (Object o : temp) {
            returnable.add(((Item) o).toString());
        }

        return returnable.toArray(new String[1]);
    }

    private String[] formatCartasArray() {
        Object[] temp = shoppingCart.toArray();
        ArrayList<String> returnable = new ArrayList<>(temp.length);
        for (Object o : temp) {
            Pair<Item, Integer> i = (Pair<Item, Integer>) o;
            returnable.add(i.t.toString() + ", Qty: " + i.u);
        }

        return returnable.toArray(new String[1]);
    }

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        itemsHS = new HashSet<>();
        itemsTS = new TreeSet<>();
        shoppingCart = new ArrayList<>();

        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(inputFilePath);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                String name = line.substring(0, line.indexOf(","));
                double price = Double.parseDouble(line.substring(line.indexOf(",") + 1));
                Item item = new Item(name, price);

                itemsTS.add(item);
                itemsHS.add(item);
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + inputFilePath + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + inputFilePath + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

        itemsDisplay = new JList<>();
        itemsDisplay.setListData(formatTSasArray());
        itemsDisplayPane = new JScrollPane(itemsDisplay);
        itemsDisplayPane.setBounds(25, 50, 250, getPreferredSize().height - 50 - 25);
        add(itemsDisplayPane);

        cartDisplay = new JList<>();
        cartDisplay.setListData(formatCartasArray());
        cartDisplayPane = new JScrollPane(cartDisplay);
        cartDisplayPane.setBounds(50 + 250 + 125, 50, 350, getPreferredSize().height - 50 - 25);
        add(cartDisplayPane);

        nameInput = new JTextField();
        nameInput.setBounds(itemsXPos, 50, itemsWidth, 30);
        add(nameInput);

        priceInput = new JTextField();
        priceInput.setBounds(itemsXPos, 100, itemsWidth, 30);
        add(priceInput);

        qtyInput = new JTextField();
        qtyInput.setBounds(itemsXPos, 150, itemsWidth, 30);
        add(qtyInput);

//        objectiveInput = new JTextField();
//        objectiveInput.setBounds(itemsXPos, 265, itemsWidth, 30);
//        add(objectiveInput);

        addItemBtn = new JButton("Add Item");
        addItemBtn.setBounds(itemsXPos, 190, itemsWidth, 30);
        addItemBtn.addActionListener(this);
        add(addItemBtn);
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textVerticalOffset = -7;
        g.drawString("Items List:", 25, 50 + textVerticalOffset);
        g.drawString("Shopping Cart:", 50 + 250 + 125, 50 + textVerticalOffset);

        g.drawString("Item name:", itemsXPos, 50 + textVerticalOffset);
        g.drawString("Item price:", itemsXPos, 100 + textVerticalOffset);
        g.drawString("Item quantity:", itemsXPos, 150 + textVerticalOffset);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addItemBtn) {
            try {
                String name = nameInput.getText();
                double price = Double.parseDouble(priceInput.getText());
                int qty = Integer.parseInt(qtyInput.getText());
                Item item = new Item(name, price);

                if (!itemsHS.contains(item))
                    JOptionPane.showMessageDialog(null, "ERROR! item as described doesn't exist!");
                else {
                    int dupIndex = -1;
                    for (int i = 0; i < shoppingCart.size(); i++) {
                        if (shoppingCart.get(i).t.hashCode() == item.hashCode()) dupIndex = i;
                    }

                    if (dupIndex == -1) shoppingCart.add(new Pair<Item, Integer>(item, qty));
                    else shoppingCart.get(dupIndex).u += qty;
                }
            } catch (NumberFormatException err) {
//                FURIOUS JAZZ HANDS
                JOptionPane.showMessageDialog(null, "ERROR! price and quantity must be decimal and integer numbers respectively!");
            }
        }
        cartDisplay.setListData(formatCartasArray());

        repaint();
    }
}
