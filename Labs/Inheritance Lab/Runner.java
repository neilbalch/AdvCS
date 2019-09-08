import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Screen extends JPanel implements ActionListener {
    private JScrollPane emeployeesListPane;
    private JList employeesListDisplay;
    private ArrayList<Employee> employeesList;
    private ArrayList<Employee> selectedEmployees;
    private JButton onlyTeachers;
    private JButton onlyCops;
    private JButton onlyEngineers;
    private JButton onlyBankers;
    private JButton resetAll;
    private JTextField nameInput;
    private JButton searchByName;
    private JButton deleteSelectedEmployee;

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        employeesList = new ArrayList<Employee>();
        employeesList.add(new Government("Mountain View", "Jane Smith", "imgs/jsmith.jpg", "Math Teacher", 1.6e5));
        employeesList.add(new Government("Mountain View", "Jack Mann", "imgs/jmann.jpg", "Band Teacher", 1.4e5));
        employeesList.add(new Government("Mountain View", "Thijs van Leer", "imgs/tleer.jpg", "Police Officer", 1.8e5));
        employeesList.add(new Government("Mountain View", "Pierre Linden", "imgs/plinden.jpg", "Police Officer", 1.3e5));
        employeesList.add(new Company("Square Systems", "Dave Vanian", "imgs/dvanian.jpg", "Engineer", 2.2e5));
        employeesList.add(new Company("Square Systems", "Capt. Sensible", "imgs/csensible.jpg", "Engineer", 2.1e5));
        employeesList.add(new Company("Wells Fartgo", "Monty Oxymoron", "imgs/moxymoron.jpg", "Banker", 8e4));
        employeesList.add(new Company("Wells Fartgo", "Paul Gray", "imgs/pgray.jpg", "Banker", 9e4));

        selectedEmployees = (ArrayList<Employee>) employeesList.clone();

        employeesListDisplay = new JList(selectedEmployees.toArray());
        employeesListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeesListDisplay.setLayoutOrientation(JList.VERTICAL);

        emeployeesListPane = new JScrollPane(employeesListDisplay);
//        emeployeesListPane.setPreferredSize(new Dimension(400, 400));
        emeployeesListPane.setBounds(25, 25, 500, 400);
        add(emeployeesListPane);

        int searchBtnsX = 550;

        onlyCops = new JButton("Only Police Officers");
        onlyCops.setBounds(searchBtnsX, 50, 150, 30);
        onlyCops.addActionListener(this);
        add(onlyCops);

        onlyTeachers = new JButton("Only Teachers");
        onlyTeachers.setBounds(searchBtnsX, 90, 150, 30);
        onlyTeachers.addActionListener(this);
        add(onlyTeachers);

        onlyBankers = new JButton("Only Bankers");
        onlyBankers.setBounds(searchBtnsX, 130, 150, 30);
        onlyBankers.addActionListener(this);
        add(onlyBankers);

        onlyEngineers = new JButton("Only Engineers");
        onlyEngineers.setBounds(searchBtnsX, 170, 150, 30);
        onlyEngineers.addActionListener(this);
        add(onlyEngineers);

        nameInput = new JTextField();
        nameInput.setBounds(searchBtnsX, 210, 100, 30);
        add(nameInput);

        searchByName = new JButton("Search by Name");
        searchByName.setBounds(searchBtnsX + 105, 210, 135, 30);
        searchByName.addActionListener(this);
        add(searchByName);

        deleteSelectedEmployee = new JButton("Delete Selected Employee");
        deleteSelectedEmployee.setBounds(searchBtnsX, 250, 200, 30);
        deleteSelectedEmployee.addActionListener(this);
        add(deleteSelectedEmployee);

        resetAll = new JButton("Reset Selection");
        resetAll.setBounds(searchBtnsX, 290, 150, 30);
        resetAll.addActionListener(this);
        add(resetAll);
    }

    //Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int xPos = 10;
        for (int i = 0; i < selectedEmployees.size(); i++) {
            selectedEmployees.get(i).drawPhoto(g, new Point(xPos, 450), this);
            xPos += 95;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetAll) {
            selectedEmployees = (ArrayList<Employee>) employeesList.clone();
        } else if (e.getSource() == onlyBankers) {
            selectedEmployees.clear();
            for (int i = 0; i < employeesList.size(); i++) {
                if (employeesList.get(i).getJobTitle().toLowerCase().contains("banker"))
                    selectedEmployees.add(employeesList.get(i));
            }
        } else if (e.getSource() == onlyCops) {
            selectedEmployees.clear();
            for (int i = 0; i < employeesList.size(); i++) {
                if (employeesList.get(i).getJobTitle().toLowerCase().contains("police"))
                    selectedEmployees.add(employeesList.get(i));
            }
        } else if (e.getSource() == onlyEngineers) {
            selectedEmployees.clear();
            for (int i = 0; i < employeesList.size(); i++) {
                if (employeesList.get(i).getJobTitle().toLowerCase().contains("engineer"))
                    selectedEmployees.add(employeesList.get(i));
            }
        } else if (e.getSource() == onlyTeachers) {
            selectedEmployees.clear();
            for (int i = 0; i < employeesList.size(); i++) {
                if (employeesList.get(i).getJobTitle().toLowerCase().contains("teacher"))
                    selectedEmployees.add(employeesList.get(i));
            }
        } else if (e.getSource() == searchByName) {
            String name = nameInput.getText().toLowerCase();
            selectedEmployees.clear();
            for (int i = 0; i < employeesList.size(); i++) {
                if (employeesList.get(i).getName().toLowerCase().contains(name))
                    selectedEmployees.add(employeesList.get(i));
            }

            nameInput.setText("");
        } else if (e.getSource() == deleteSelectedEmployee) {
            int selectedIndex = employeesListDisplay.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedName = selectedEmployees.get(selectedIndex).getName();
                selectedEmployees.remove(selectedIndex);

                for (int i = 0; i < employeesList.size(); i++) {
                    if (employeesList.get(i).getName().equals(selectedName)) {
                        employeesList.remove(i);
                        break;
                    }
                }
            }
        }

        repaint();
        employeesListDisplay.setListData(selectedEmployees.toArray());
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
