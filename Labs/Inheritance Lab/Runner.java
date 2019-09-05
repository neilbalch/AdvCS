import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Screen extends JPanel implements ActionListener {
    private JScrollPane emeployeesListPane;
    private JList employeesListDisplay;
    private ArrayList<Employee> employeesAList;
    private List<Employee> employeesList;

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        employeesAList = new ArrayList<Employee>();
        employeesAList.add(new Government("Mountain View", "Jane Smith", "imgs/jsmith.jpg", "Math Teacher", 1.6e5));
        employeesAList.add(new Government("Mountain View", "Jack Mann", "imgs/jmann.jpg", "Band Teacher", 1.4e5));
        employeesAList.add(new Government("Mountain View", "Thijs van Leer", "imgs/tleer.jpg", "Police Officer", 1.8e5));
        employeesAList.add(new Government("Mountain View", "Pierre Linden", "imgs/plinden.jpg", "Police Officer", 1.3e5));
        employeesAList.add(new Company("Square Systems", "Dave Vanian", "imgs/dvanian.jpg", "Engineer", 2.2e5));
        employeesAList.add(new Company("Square Systems", "Capt. Sensible", "imgs/csensible.jpg", "Engineer", 2.1e5));
        employeesAList.add(new Company("Wells Fartgo", "Monty Oxymoron", "imgs/moxymoron.jpg", "Banker", 8e4));
        employeesAList.add(new Company("Wells Fartgo", "Paul Gray", "imgs/pgray.jpg", "Banker", 9e4));

//        employeesList = (ArrayList<Employee>)employeesAList.clone();

        employeesListDisplay = new JList(employeesAList.toArray());
        employeesListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeesListDisplay.setLayoutOrientation(JList.VERTICAL);

        emeployeesListPane = new JScrollPane(employeesListDisplay);
//        emeployeesListPane.setPreferredSize(new Dimension(400, 400));
        emeployeesListPane.setBounds(25, 25, 500, 400);
        add(emeployeesListPane);
    }

    //Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int xPos = 10;
        for (int i = 0; i < employeesAList.size(); i++) {
            employeesAList.get(i).drawPhoto(g, new Point(xPos, 450), this);
            xPos += 95;
        }
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

public class Runner {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Employees Manager");
        Screen sc = new Screen();
        frame.add(sc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
