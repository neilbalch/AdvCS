import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen extends JPanel implements ActionListener {
    private Account[] accounts;
    private enum State {LOGGED_OUT, LOGGED_IN}
    private State state;

    // Root dimension that is the parent of all other objects' positions
    private Dimension root = new Dimension(150, 100);

    private JTextField pinInput;
    private JTextField nameInput;
    private JTextField withdrawInput;
    private JTextField depositInput;
    private JButton loginBtn;
    private JButton logoutBtn;
    private JButton actBtn;

    private String error;
    private int loggedInUser;

    private int countdown;
    private Thread countdownThread;

    private void countdownExecutor() {
        countdown = 30;
//        countdown = 3;
        while(countdown > 0) {
            repaint();
            countdown--;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }

        logoutBtn.doClick(250);
    }

    public Screen() {
        setLayout(null);

        accounts = new Account[5];
        accounts[0] = new Account("Jennifer", 999.99, 1234);
        accounts[1] = new Account("Jose", 500.01, 4321);
        accounts[2] = new Account("Jane", 50043.01, 5432);
        accounts[3] = new Account("Jack", 234.01, 1523);
        accounts[4] = new Account("Diane", 593.01, 8402);

        state = State.LOGGED_OUT;
        error = "";
        loggedInUser = -1;

//        countdownThread = new Thread(() -> {
////          countdown = 30;
//            countdown = 3;
//            while(countdown >= 0) {
//                repaint();
//                countdown--;
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException) return;
//            }
//
//            logoutBtn.doClick(250);
//        });
        countdownThread = new Thread(this::countdownExecutor);

        nameInput = new JTextField();
        nameInput.setBounds(root.width, root.height + 50, 100, 30);
        add(nameInput);

        pinInput = new JTextField();
        pinInput.setBounds(root.width, root.height + 90, 100, 30);
        add(pinInput);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(root.width, root.height, 100, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(root.width, root.height, 100, 30);
        logoutBtn.addActionListener(this);
//        add(logout);

        withdrawInput = new JTextField();
        withdrawInput.setBounds(root.width, root.height + 100, 100, 30);
//        add(withdrawInput);

        depositInput = new JTextField();
        depositInput.setBounds(root.width, root.height + 140, 100, 30);
//        add(depositInput);

        actBtn = new JButton("Perform Action");
        actBtn.setBounds(root.width, root.height + 180, 150, 30);
        actBtn.addActionListener(this);
//        add(act);
    }

    public Dimension getPreferredSize() { return new Dimension(800, 600); }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!error.equals("")) g.drawString(error, root.width, root.height - 10);

        if(state == State.LOGGED_OUT) {
            g.drawString("Name: ", root.width - 40, root.height + 70);
            g.drawString("PIN: ", root.width - 25, root.height + 110);
        } else {
            g.drawString("Welcome, " + accounts[loggedInUser].getName(), root.width, root.height + 60);
            g.drawString("Balance: $" + accounts[loggedInUser].getBalance(), root.width, root.height + 80);

            g.drawString("Desired amount", root.width - 95, root.height + 115);
            g.drawString("to withdraw: ", root.width - 70, root.height + 130);
            g.drawString("Desired amount", root.width - 95, root.height + 155);
            g.drawString("to deposit: ", root.width - 70, root.height + 170);

            g.drawString("Countdown to auto-logout:", root.width, root.height + 250);

            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString(Integer.toString(countdown), root.width, root.height + 280);
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            g.setFont(defaults.getFont("TextField.font"));
        }
    }

    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == loginBtn) {
            String name = nameInput.getText();
            try {
                int pin = Integer.parseInt(pinInput.getText());

                boolean accountFound = false;
                for (int i = 0; i < accounts.length; i++) {
                    if(accounts[i].getName().equals(name)) {
                        accountFound = true;
                        accounts[i].unlockWithPIN(pin);
                        if(accounts[i].getAccess()) {
                            state = State.LOGGED_IN;
                            loggedInUser = i;

                            // Remove the login button in favor of the logout button.
                            remove(loginBtn);
                            remove(nameInput);
                            remove(pinInput);
                            add(logoutBtn);
                            add(depositInput);
                            add(withdrawInput);
                            add(actBtn);

                            // Start the countdown thread.
                            countdownThread = new Thread(this::countdownExecutor);
                            countdownThread.start();

                            // Clear the error variable.
                            error = "";

                            // Clear the input data.
                            nameInput.setText("");
                            pinInput.setText("");
                        } else error = "Mismatched account information.";
                    }
                }

                if(!accountFound) error = "Mismatched account information.";
            } catch (NumberFormatException err) {
               error = "PIN must be a number";
            }
        } else if(event.getSource() == logoutBtn) {
            accounts[loggedInUser].logout();
            loggedInUser = -1;
            state = State.LOGGED_OUT;

            // Clear the error variable.
            error = "";

            // Stop the countdown thread.
            countdownThread.interrupt();

            // Remove the logout button in favor of the login one.
            remove(logoutBtn);
            remove(depositInput);
            remove(withdrawInput);
            remove(actBtn);
            add(loginBtn);
            add(nameInput);
            add(pinInput);
        } else if(event.getSource() == actBtn) {
            try {
                double withdrawAmt = Double.parseDouble(withdrawInput.getText().equals("") ? "-1" : withdrawInput.getText());
                double depositAmt = Double.parseDouble(depositInput.getText().equals("") ? "-1" : depositInput.getText());

                if(withdrawAmt != -1 && depositAmt != -1) {
                    error = "Only one action may be performed at a time. Only " +
                            "enter in a value for either a deposit or a " +
                            "withdrawal";
                } else if(withdrawAmt == -1 && depositAmt == -1) {
                        error = "Please specify an amount to withdrawal or deposit";
                } else if((withdrawAmt != -1 && withdrawAmt < 0)
                        || (depositAmt != -1 && depositAmt < 0)) {
                    error = "Amounts must be greater than zero.";
                } else if(withdrawAmt != -1) {
                    if(withdrawAmt > accounts[loggedInUser].getBalance()) {
                        error = "Negative balances aren't allowed!";
                    } else {
                        accounts[loggedInUser].withdraw(withdrawAmt);

                        // Reset the timeout counter.
                        countdown = 30;

                        // Clear the error variable.
                        error = "";

                        // Clear the input box.
                        withdrawInput.setText("");
                    }
                } else if(depositAmt != -1) {
                    accounts[loggedInUser].deposit(depositAmt);

                    // Reset the timeout counter.
                    countdown = 30;

                    // Clear the error variable.
                    error = "";

                    // Clear the input box.
                    depositInput.setText("");
                }
            } catch (NumberFormatException err) {
                error = "Amounts must be decimal or integer numbers.";
            }
        }

        repaint();
    }
}
