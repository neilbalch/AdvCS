import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JPanel implements ActionListener {
    private Socket clientSocket;
    private String serverHost = "localhost";
    private int port = 1024;
    private DataInputStream in;
    private DataOutputStream out;

    private JTextArea history;
    private JScrollPane historyPane;
    private JTextField msgBox;

    public Client() {
        setLayout(null);

        try {
            System.out.println("Connecting to " + serverHost + " on port " + port);
            Socket client = new Socket(serverHost, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        history = new JTextArea();
        history.setEditable(false);
//        history.setBounds(50, 50, getPreferredSize().width - 100, getPreferredSize().height - 100 - 50);
//        add(history);
        history.setText("Chat History:");

        historyPane = new JScrollPane(history);
        historyPane.setBounds(50, 50, getPreferredSize().width - 100, getPreferredSize().height - 100 - 50);
        add(historyPane);

        msgBox = new JTextField();
        msgBox.setBounds(50, getPreferredSize().height - 50 - 40, getPreferredSize().width - 100, 30);
        msgBox.addActionListener(this);
        add(msgBox);

        Thread listener = new Thread(() -> {
            while (true) {
                try {
                    String incomingMsg = in.readUTF();
                    System.out.println("Received: " + incomingMsg);
                    history.setText(history.getText() + "\n\n" + "Friend: " + incomingMsg);
                    repaint();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        });
        listener.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == msgBox) {
            try {
                String msg = msgBox.getText();
                out.writeUTF(msg);
                history.setText(history.getText() + "\n\n" + "Me: " + msg);
                msgBox.setText("");
                System.out.println("Sent: " + msg);
            } catch (IOException err) {
                System.out.println("ERR!");
            }
        }
    }
}
