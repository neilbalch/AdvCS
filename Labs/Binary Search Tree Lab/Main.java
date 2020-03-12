import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Main extends JPanel implements ActionListener {
    private JButton toAdminView;
    private JButton toCustomerView;
    private boolean inAdminView = true; // TODO: Set to false by default
    private boolean operationJustPerformed;
    private Account queriedAccount;
    private final String saveFile = "treeSave.jobj";

    private JButton addAccount;
    private JButton getAccount;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField pinField;
    private JTextField balanceField;

    private JList<String> accountsList;
    private JScrollPane accountsPane;

    private BTree<Account> accounts;

    private boolean coordsWithin(Point location, Point topLeft, Point bottomRight) {
        if (location.x > topLeft.x && location.x < bottomRight.x &&
                location.y > topLeft.y && location.y < bottomRight.y) return true;
        else return false;
    }

    private void drawStringArr(Graphics g, String[] text, Point topLeft) {
        int x = topLeft.x;
        int y = topLeft.y + 7;
        for (String line : text) {
            g.drawString(line, x, y);
            y += 15;
        }
    }

    private void writeTreeToFile() {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accounts);
            oos.close();
        } catch (IOException err) {
            System.out.println(err.toString());
        }
    }

    public Main() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));

        accounts = new BTree<Account>();
        operationJustPerformed = false;
        queriedAccount = null;

        if ((new File(saveFile)).exists()) {
            try {
                FileInputStream fin = new FileInputStream(saveFile);
                ObjectInputStream ois = new ObjectInputStream(fin);
                accounts = (BTree<Account>) ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException err) {
                System.out.println(err.toString());
            }
        } else {
            try {
                Scanner scan = new Scanner(new File("names.txt"));
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
//                    System.out.println(line);

                    accounts.add(new Account(line.substring(line.indexOf(",") + 1),
                            line.substring(0, line.indexOf(",")),
                            (int) (Math.random() * 10000),
                            Math.random() * 100001));
                }
            } catch (FileNotFoundException err) {
                System.out.println(err);
            }

            writeTreeToFile();
        }

//        String temp = "";
//        for(int i = 0; i < accounts.getSize() - 1; i++) {
//            temp += accounts.get(i) + ", ";
//        }
//        temp += accounts.get(accounts.getSize() - 1);
//
//        System.out.println(temp);
//        System.out.println(accounts);

        toAdminView = new JButton("Admin");
        toAdminView.setBounds(25 + 200 + 15, 25, 100, 30);
        toAdminView.addActionListener(this);
        toAdminView.setEnabled(false);
        toAdminView.setVisible(false);
        add(toAdminView);

        toCustomerView = new JButton("Customer");
        toCustomerView.setBounds(25 + 200 + 15 + 120, 25, 100, 30);
        toCustomerView.addActionListener(this);
        toCustomerView.setVisible(false);
        add(toCustomerView);

        accountsList = new JList<>();
        accountsList.setListData(recreateAccountsList());
        accountsPane = new JScrollPane(accountsList);
        accountsPane.setBounds(25, 25, 200, getPreferredSize().height - 50);
        add(accountsPane);

        firstNameField = new JTextField();
        firstNameField.setBounds(25 + 200 + 15, 25, 120, 30);
        add(firstNameField);

        lastNameField = new JTextField();
        lastNameField.setBounds(25 + 200 + 15, 25 + 50, 120, 30);
        add(lastNameField);

        pinField = new JTextField();
        pinField.setBounds(25 + 200 + 15, 25 + 50 * 2, 120, 30);
        add(pinField);

        balanceField = new JTextField();
        balanceField.setBounds(25 + 200 + 15, 25 + 50 * 3, 120, 30);
        add(balanceField);

        addAccount = new JButton("Add Account");
        addAccount.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40, 120, 30);
        addAccount.addActionListener(this);
        add(addAccount);

        getAccount = new JButton("Get Account");
        getAccount.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 2, 120, 30);
        getAccount.addActionListener(this);
        add(getAccount);
    }

    private String[] recreateAccountsList() {
//        String[] out = new String[accounts.size()];
//        for (int i = 0; i < list.size(); i++)
//            out[i] = list.get(i).toString() + " - " + countries.getValue(list.get(i)) + " : " + map.getValue(list.get(i)).size();
//
////        System.out.println("array");
////        for(String item : out) System.out.println(" - " + item);
//
//        return out;
        return accounts.toString().split(", ");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int textOffset = -7;

        g.setColor(Color.BLACK);
        g.drawString("Bank Accounts: ", 25, 25 + textOffset);

        if (operationJustPerformed)
            g.drawString("Operation took " + accounts.getPasses() + " passes.", 400, 25 + textOffset);

        g.drawString("Account First Name", 25 + 200 + 15, 25 + 50 * 0 + textOffset);
        g.drawString("Account Last Name", 25 + 200 + 15, 25 + 50 * 1 + textOffset);
        g.drawString("Account PIN", 25 + 200 + 15, 25 + 50 * 2 + textOffset);
        g.drawString("Account Balance", 25 + 200 + 15, 25 + 50 * 3 + textOffset);

        if (queriedAccount != null) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toAdminView) {
        } else if (e.getSource() == toCustomerView) {
        } else if (e.getSource() == addAccount) {
            try {
                int pin = Integer.parseInt(pinField.getText());
                double balance = Double.parseDouble(balanceField.getText());

                accounts.add(new Account(firstNameField.getText(), lastNameField.getText(), pin, balance));
                accountsList.setListData(recreateAccountsList());
                writeTreeToFile();
                operationJustPerformed = true;
                System.out.println(accounts);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "ERROR: PIN and Balance must be numbers!");
            }
        } else if (e.getSource() == getAccount) {
            Account query = new Account(firstNameField.getText(), lastNameField.getText(), 0, 0.0);
            queriedAccount = accounts.get(query);
        }

        repaint();
    }

    public static void main(String[] args) {
        Main game = new Main();
        JFrame frame = new JFrame("Bank Manager");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
