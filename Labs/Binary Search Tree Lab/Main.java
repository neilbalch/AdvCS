import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Main extends JPanel implements ActionListener {
    private boolean inAdminView = false;
    private boolean operationJustPerformed;
    private Account adminQueriedAccount;
    private Account customerAccount;
    private final String saveFile = "treeSave.jobj";

    // Common
    private JButton changeView;
    private JButton changeFirst;
    private JButton changeLast;
    private JButton changePIN;
    private JTextField newValueField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField pinField;

    // Customer View
    private JButton loginStateChange;
    private JButton deposit;
    private JButton withdraw;

    // Admin View
    private JButton addAccount;
    private JButton getAccount;
    private JButton changeBalance;
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

    public Main() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));

        accounts = new BTree<Account>();
        operationJustPerformed = false;
        adminQueriedAccount = null;
        customerAccount = null;

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
//                    System.out.println(accounts.get(accounts.getSize() - 1).getPin());
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

        changeView = new JButton("Admin");
        changeView.setBounds(getPreferredSize().width - 100 - 15, getPreferredSize().height - 30 - 15, 100, 30);
        changeView.addActionListener(this);
        add(changeView);

        accountsList = new JList<>();
        accountsList.setListData(recreateAccountsList());
        accountsPane = new JScrollPane(accountsList);
        accountsPane.setBounds(25, 25, 200, getPreferredSize().height - 50);
        accountsPane.setVisible(false);
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
        balanceField.setVisible(false);
        add(balanceField);

        addAccount = new JButton("Add Account");
        addAccount.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40, 120, 30);
        addAccount.addActionListener(this);
        addAccount.setVisible(false);
        add(addAccount);

        getAccount = new JButton("Get Account");
        getAccount.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 2, 120, 30);
        getAccount.addActionListener(this);
        getAccount.setVisible(false);
        add(getAccount);

        changeFirst = new JButton("Change First");
        changeFirst.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 3, 120, 30);
        changeFirst.addActionListener(this);
        changeFirst.setEnabled(false);
        add(changeFirst);

        changeLast = new JButton("Change Last");
        changeLast.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 4, 120, 30);
        changeLast.addActionListener(this);
        changeLast.setEnabled(false);
        add(changeLast);

        changePIN = new JButton("Change PIN");
        changePIN.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 5, 120, 30);
        changePIN.addActionListener(this);
        changePIN.setEnabled(false);
        add(changePIN);

        changeBalance = new JButton("Change Balance");
        changeBalance.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 6, 130, 30);
        changeBalance.addActionListener(this);
        changeBalance.setVisible(false);
        add(changeBalance);

        newValueField = new JTextField();
        newValueField.setBounds(25 + 200 + 15 + 130, 25 + 50 * 3 + 40 * 5, 120, 30);
        newValueField.setEnabled(false);
        add(newValueField);

        // Customer View

        loginStateChange = new JButton("Login");
        loginStateChange.setBounds(25 + 200 + 15 - 100 - 15, 25, 100, 30);
        loginStateChange.addActionListener(this);
        add(loginStateChange);

        deposit = new JButton("Deposit");
        deposit.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 6, 120, 30);
        deposit.addActionListener(this);
        deposit.setEnabled(false);
        add(deposit);

        withdraw = new JButton("Withdraw");
        withdraw.setBounds(25 + 200 + 15, 25 + 50 * 3 + 40 * 7, 120, 30);
        withdraw.addActionListener(this);
        withdraw.setEnabled(false);
        add(withdraw);

        System.out.println(accounts.get(0).toStringAll());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int textOffset = -7;

        g.setColor(Color.BLACK);
        if (inAdminView) {
            g.drawString("Bank Accounts: ", 25, 25 + textOffset);
            g.drawString("Account Balance", 25 + 200 + 15, 25 + 50 * 3 + textOffset);
        }

        if (operationJustPerformed) {
            g.drawString("Operation took " + accounts.getPasses() + " passes.", 400, 25 + textOffset);
            operationJustPerformed = false;
        }

        g.drawString("Account First Name", 25 + 200 + 15, 25 + 50 * 0 + textOffset);
        g.drawString("Account Last Name", 25 + 200 + 15, 25 + 50 * 1 + textOffset);
        g.drawString("Account PIN", 25 + 200 + 15, 25 + 50 * 2 + textOffset);
        g.drawString("New Value: (all account changes)", 25 + 200 + 15 + 130, 25 + 50 * 3 + 40 * 5 + textOffset);

        if (adminQueriedAccount != null) {
            g.drawString("Name: " + adminQueriedAccount.getFirst() + " " + adminQueriedAccount.getLast(), 400, 40 + textOffset);
            g.drawString("PIN: " + adminQueriedAccount.getPin(), 400, 40 + 15 + textOffset);
            g.drawString("Balance: $" + Math.round(adminQueriedAccount.getBalance() * 100) / 100.0, 400, 40 + 15 * 2 + textOffset);
        }
        if (customerAccount != null) {
            g.drawString("Name: " + customerAccount.getFirst() + " " + customerAccount.getLast(), 400, 40 + textOffset);
            g.drawString("PIN: " + customerAccount.getPin(), 400, 40 + 15 + textOffset);
            g.drawString("Balance: $" + Math.round(customerAccount.getBalance() * 100) / 100.0, 400, 40 + 15 * 2 + textOffset);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeView) {
            inAdminView = !inAdminView;

            if (inAdminView) {
                addAccount.setVisible(true);
                getAccount.setVisible(true);
                changeBalance.setVisible(true);
                balanceField.setVisible(true);
                accountsPane.setVisible(true);

                customerAccount = null;
                changeFirst.setEnabled(true);
                changeLast.setEnabled(true);
                changePIN.setEnabled(true);
                newValueField.setEnabled(true);

                loginStateChange.setVisible(false);
                deposit.setVisible(false);
                withdraw.setVisible(false);

                changeView.setText("Customer");
            } else {
                addAccount.setVisible(false);
                getAccount.setVisible(false);
                changeBalance.setVisible(false);
                balanceField.setVisible(false);
                accountsPane.setVisible(false);

                changeFirst.setEnabled(false);
                changeLast.setEnabled(false);
                changePIN.setEnabled(false);
                newValueField.setEnabled(false);

                loginStateChange.setVisible(true);
                deposit.setVisible(true);
                withdraw.setVisible(true);

                changeView.setText("Admin");
            }
        } else if (e.getSource() == loginStateChange) {
            if (customerAccount != null) {
                customerAccount = null;
                loginStateChange.setText("Login");

                changeFirst.setEnabled(false);
                changeLast.setEnabled(false);
                changePIN.setEnabled(false);
                deposit.setEnabled(false);
                withdraw.setEnabled(false);
                newValueField.setEnabled(false);
            } else {
                Account query = new Account(firstNameField.getText(), lastNameField.getText(), 0, 0.0);
                if (firstNameField.getText().equalsIgnoreCase("") || lastNameField.getText().equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(this, "ERROR: First and last name must not be blank!");
                    return;
                } else if (!accounts.contains(query)) {
                    JOptionPane.showMessageDialog(this, "ERROR: Account not found!");
                    return;
                } else {
                    query = accounts.get(query);
                    try {
                        int parsedPIN = Integer.parseInt(pinField.getText());
//                        System.out.println("parsedPIN: " + parsedPIN + ", query.pin: " + query.getPin());
                        if (query.getPin() != parsedPIN) {
                            JOptionPane.showMessageDialog(this, "ERROR: PIN is incorrect!");
                            return;
                        } else {
                            customerAccount = query;
                            loginStateChange.setText("Logout");
                            operationJustPerformed = true;

                            changeFirst.setEnabled(true);
                            changeLast.setEnabled(true);
                            changePIN.setEnabled(true);
                            deposit.setEnabled(true);
                            withdraw.setEnabled(true);
                            newValueField.setEnabled(true);
                        }
                    } catch (NumberFormatException err) {
                        JOptionPane.showMessageDialog(this, "ERROR: PIN must be an integer!");
                        return;
                    }
                }
            }
        } else if (e.getSource() == addAccount) {
            try {
                int pin = Integer.parseInt(pinField.getText());
                double balance = Double.parseDouble(balanceField.getText());

                accounts.add(new Account(firstNameField.getText(), lastNameField.getText(), pin, balance));
                accountsList.setListData(recreateAccountsList());
                writeTreeToFile();
                operationJustPerformed = true;
//                System.out.println(accounts);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "ERROR: PIN and Balance must be numbers!");
            }
        } else if (e.getSource() == getAccount) {
            Account query = new Account(firstNameField.getText(), lastNameField.getText(), 0, 0.0);
//            System.out.println("\"" + firstNameField.getText() +"\", " + "\"" + lastNameField.getText() +"\"");
            if (!accounts.contains(query))
                JOptionPane.showMessageDialog(this, "ERROR: Account not found!");

            adminQueriedAccount = accounts.get(query);
            operationJustPerformed = true;
        } else if (e.getSource() == deposit) {
            try {
                double change = Double.parseDouble(newValueField.getText());
                customerAccount.setBalance(customerAccount.getBalance() + change);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "ERROR: Deposit must be a number!");
                return;
            }
        } else if (e.getSource() == withdraw) {
            try {
                double change = Double.parseDouble(newValueField.getText());
                customerAccount.setBalance(customerAccount.getBalance() - change);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "ERROR: Withdraw amount must be a number!");
                return;
            }
        } else { // TODO: Ensure this case remains the last one checked so becomes catch-all for account modifications.
            Account query = new Account(firstNameField.getText(), lastNameField.getText(), 0, 0.0);
            String newValue = newValueField.getText();
            if (firstNameField.getText().equalsIgnoreCase("") || lastNameField.getText().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "ERROR: First and last name must not be blank!");
                return;
            }

            if (e.getSource() == changeFirst) {
                if (newValue.equals("")) {
                    JOptionPane.showMessageDialog(this, "ERROR: New first name must not be empty!");
                    return;
                }

                Account temp = accounts.remove(query);
                temp.setFirst(newValue);

                accounts.add(temp);
                firstNameField.setText(newValue);
            } else if (e.getSource() == changeLast) {
                if (newValue.equals("")) {
                    JOptionPane.showMessageDialog(this, "ERROR: New last name must not be empty!");
                    return;
                }
                Account temp = accounts.remove(query);
                temp.setLast(newValue);

                accounts.add(temp);
                lastNameField.setText(newValue);
            } else if (e.getSource() == changePIN) {
                try {
                    accounts.get(query).setPin(Integer.parseInt(newValue));
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(this, "ERROR: New PIN must be an integer number!");
                }
            } else if (e.getSource() == changeBalance) {
                try {
                    accounts.get(query).setBalance(Double.parseDouble(newValue));
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(this, "ERROR: New PIN must be a decimal number!");
                }
            }

            accountsList.setListData(recreateAccountsList());
            writeTreeToFile();
            operationJustPerformed = false;
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
