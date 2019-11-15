import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerScreen extends JPanel implements ActionListener, MouseListener {
    private int itemsXPos = 25;
    private int itemsWidth = 150;

    private JButton restartGame;

//    private JList<String> queueDisplay;
//    private JScrollPane queueDisplayPane;
//    private JList<String> pQueueDisplay;
//    private JScrollPane pQueueDisplayPane;
//
//    private JTextField nameInput;
//    private JTextField illnessDescription;
//    private JComboBox prioritySelection;
//    private JComboBox ageSelection;
//    private JButton addPatientBtn;
//    private JButton updatePatientPriorityBtn;
//    private JButton updatePatientIllnessBtn;
//
//    private JTextArea patientDescriptor;
//    private JTextField dischargeMessage;
//    private JButton dischargePatientBtnn;

    private GameState game;
    private ServerSocket serverSock;
    private Socket clientSock;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int port = 1024;

    public ServerScreen() {
        this.setLayout(null);
        this.setFocusable(true);

        game = new GameState(new Point(100, 100));
        try {
            serverSock = new ServerSocket(port);
            serverSock.setSoTimeout(10000);

            System.out.println("Waiting for a client...");
            clientSock = serverSock.accept();
            System.out.println("Client connected...\n");

            out = new ObjectOutputStream(clientSock.getOutputStream());
            in = new ObjectInputStream(clientSock.getInputStream());
        } catch (IOException err) {
            System.out.println(err.toString());
        }

        Thread listener = new Thread(() -> {
            try {
                while (true) {
                    game = (GameState) in.readObject();
                    System.out.println("New Board Received");
                    repaint();
                }
            } catch (IOException | ClassNotFoundException err) {
                System.out.println(err.toString());
            }
        });
        listener.start();

        addMouseListener(this);

//        queueDisplay = new JList<>();
//        queueDisplay.setListData(formatQueueAsArray());
//        queueDisplayPane = new JScrollPane(queueDisplay);
//        queueDisplayPane.setBounds(itemsXPos + 225, 330, getPreferredSize().width - itemsXPos - 200 - 25, (getPreferredSize().height - 50 - 25) / 2);
//        queueDisplayPane.setVisible(false);
//        add(queueDisplayPane);
//
//        pQueueDisplay = new JList<>();
//        pQueueDisplay.setListData(formatPQueueAsArray());
//        pQueueDisplayPane = new JScrollPane(pQueueDisplay);
//        pQueueDisplayPane.setBounds(itemsXPos + 225, 50, getPreferredSize().width - itemsXPos - 200 - 25, (getPreferredSize().height - 50 - 25) / 2);
//        add(pQueueDisplayPane);

//        nameInput = new JTextField();
//        nameInput.setBounds(itemsXPos, 70, itemsWidth, 30);
//        add(nameInput);
//
//        illnessDescription = new JTextField();
//        illnessDescription.setBounds(itemsXPos, 120, itemsWidth, 30);
//        add(illnessDescription);
//
//        prioritySelection = new JComboBox(Patient.CasePriority.names());
//        prioritySelection.setBounds(itemsXPos, 170, itemsWidth, 30);
//        add(prioritySelection);
//
//        ageSelection = new JComboBox(Patient.Age.names());
//        ageSelection.setBounds(itemsXPos, 220, itemsWidth, 30);
//        add(ageSelection);
//
//        addPatientBtn = new JButton("Add Patient");
//        addPatientBtn.setBounds(itemsXPos, 260, itemsWidth, 30);
//        addPatientBtn.addActionListener(this);
//        add(addPatientBtn);
//
        restartGame = new JButton("Restart Game");
        restartGame.setBounds(100, 15, (int) itemsWidth, 30);
        restartGame.addActionListener(this);
        restartGame.setVisible(false);
        add(restartGame);
//
//        updatePatientIllnessBtn = new JButton("Update Illness");
//        updatePatientIllnessBtn.setBounds(itemsXPos, 300, itemsWidth, 30);
//        updatePatientIllnessBtn.addActionListener(this);
//        add(updatePatientIllnessBtn);
//
//        updatePatientPriorityBtn = new JButton("Update Priority");
//        updatePatientPriorityBtn.setBounds(itemsXPos, 340, itemsWidth, 30);
//        updatePatientPriorityBtn.addActionListener(this);
//        add(updatePatientPriorityBtn);

        ////////////////////////////////////////////////////////////////////////////////////////////

//        patientDescriptor = new JTextArea();
//        patientDescriptor.setBounds(itemsXPos, 60, getPreferredSize().width - 60 - 50, 200);
//        patientDescriptor.setVisible(false);
//        patientDescriptor.setEditable(false);
//        add(patientDescriptor);
//
//        dischargeMessage = new JTextField();
//        dischargeMessage.setBounds(itemsXPos, 280, getPreferredSize().width - 60 - 50, 30);
//        dischargeMessage.setVisible(false);
//        add(dischargeMessage);
//
//        dischargePatientBtn = new JButton("Discharge Patient");
//        dischargePatientBtn.setBounds(itemsXPos, 320, itemsWidth, 30);
//        dischargePatientBtn.addActionListener(this);
//        dischargePatientBtn.setVisible(false);
//        add(dischargePatientBtn);
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textVerticalOffset = -7;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("You are player X. It is player " + (game.state == GameState.State.TURN1 ? "X's" : "O's") + " turn.", 100, 100 + textVerticalOffset);

        if (game.checkTicTackToe() != 0)
            g.drawString("Player " + (game.checkTicTackToe() == 1 ? "X" : "O") + " won the game!", 250, 100 + textVerticalOffset);

        game.drawBoard(g);
    }

    public void actionPerformed(ActionEvent e) {

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked");
        if (game.state == GameState.State.TURN1) {
            System.out.println("My Turn");
            if (game.handleClick(e.getPoint())) {
                System.out.println("Board Changed");

                if (game.checkTicTackToe() == 1 || game.checkFull())
                    game.state = GameState.State.OVER;

                try {
                    out.writeObject(game);
                } catch (IOException err) {
                    System.out.println(err.toString());
                }
                repaint();
            }
        } else System.out.println("Turn is of: " + game.state.name());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
