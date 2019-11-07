import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Server extends JPanel implements ActionListener {
    private ServerSocket serverSocket;
    private int port = 1024;
    private DataInputStream in;
    private DataOutputStream out;

    private JTextArea history;
    private JScrollPane historyPane;
    private JTextField msgBox;

    public Server() {
        setLayout(null);

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);

            System.out.println("Waiting for client on port " +
                    serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();

            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (IOException e) {
            e.printStackTrace();
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
