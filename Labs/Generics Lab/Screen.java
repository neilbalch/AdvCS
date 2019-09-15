import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Screen extends JPanel implements ActionListener {
    private JScrollPane studentsListPane;
    private JList studentsListDisplay;
    private JScrollPane scheduleListPane;
    private JList scheduleListDisplay;
    private ArrayList<Pair<Schedule, Student>> studentsList;
    private JButton selectStudent;
    private int selectedStudent = -1;
    private JButton changePeriod;
    private JButton resetAll;
    private JButton deletePeriod;
    private JTextField periodInput;
    private JTextField classNameInput;

    private int btnsX = 25 + 200 + 5;


    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        studentsList = new ArrayList<>();
        studentsList.add(new Pair<Schedule, Student>(new Schedule(), new Student("John Doe", "imgs/jdoe.jpg"), null, ""));
        studentsList.add(new Pair<Schedule, Student>(new Schedule(), new Student("Josh Smith", "imgs/jsmith.jpg"), null, ""));
        studentsList.add(new Pair<Schedule, Student>(new Schedule(), new Student("David Wallace", "imgs/dwallace.jpg"), null, ""));

        studentsList.get(0).getItem1().addPeriod(0, "Marching Band");
        studentsList.get(0).getItem1().addPeriod(1, "PE");
        studentsList.get(0).getItem1().addPeriod(2, "English");
        studentsList.get(0).getItem1().addPeriod(3, "Physics");
        studentsList.get(0).getItem1().addPeriod(4, "Calculus");
        studentsList.get(0).getItem1().addPeriod(5, "Photography");
        studentsList.get(0).getItem1().addPeriod(6, "FREE PERIOD");
        studentsList.get(0).getItem1().addPeriod(7, "Wind Ensemble");

        studentsList.get(1).getItem1().addPeriod(1, "English");
        studentsList.get(1).getItem1().addPeriod(2, "Calculus");
        studentsList.get(1).getItem1().addPeriod(3, "FREE PERIOD");
        studentsList.get(1).getItem1().addPeriod(4, "Physics");
        studentsList.get(1).getItem1().addPeriod(5, "PE");
        studentsList.get(1).getItem1().addPeriod(6, "Engineering");
        studentsList.get(1).getItem1().addPeriod(7, "String Orchestra");

        studentsList.get(2).getItem1().addPeriod(1, "English");
        studentsList.get(2).getItem1().addPeriod(2, "Earth Science");
        studentsList.get(2).getItem1().addPeriod(3, "Trigonometry");
        studentsList.get(2).getItem1().addPeriod(4, "PE");
        studentsList.get(2).getItem1().addPeriod(5, "FREE PERIOD");
        studentsList.get(2).getItem1().addPeriod(6, "FREE PERIOD");
        studentsList.get(2).getItem1().addPeriod(7, "Advanced Computer Science");

        studentsListDisplay = new JList(studentsList.toArray());
        studentsListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsListDisplay.setLayoutOrientation(JList.VERTICAL);

        studentsListPane = new JScrollPane(studentsListDisplay);
        studentsListPane.setBounds(25, 25, 200, getPreferredSize().height - 50 - 130);
        add(studentsListPane);

        scheduleListDisplay = new JList(new Schedule[1]);
        scheduleListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleListDisplay.setLayoutOrientation(JList.VERTICAL);

        scheduleListPane = new JScrollPane(scheduleListDisplay);
        scheduleListPane.setBounds(25 + 200 + 150, 25, getPreferredSize().width - 25 - 200 - 150 - 25, getPreferredSize().height - 50 - 130);
        add(scheduleListPane);

        selectStudent = new JButton("Select Student");
        selectStudent.setBounds(btnsX, 50, 140, 30);
        selectStudent.addActionListener(this);
        add(selectStudent);

        resetAll = new JButton("Reset Selection");
        resetAll.setBounds(btnsX, 90, 140, 30);
        resetAll.addActionListener(this);
        add(resetAll);

        periodInput = new JTextField();
        periodInput.setBounds(btnsX, 180, 140, 30);
//        add(periodInput);

        classNameInput = new JTextField();
        classNameInput.setBounds(btnsX, 230, 140, 30);
//        add(classNameInput);

        changePeriod = new JButton("Change Period");
        changePeriod.setBounds(btnsX, 270, 140, 30);
        changePeriod.addActionListener(this);
//        add(changePeriod);

        deletePeriod = new JButton("Delete Period");
        deletePeriod.setBounds(btnsX, 310, 140, 30);
        deletePeriod.addActionListener(this);
//        add(deletePeriod);
    }

    //Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Students List:", 25, 20);
        g.drawString("Schedule Breakdown: ", 25 + 200 + 150, 20);


        if (selectedStudent != -1) {
            int yPos = getPreferredSize().height - 25 - 140;
            g.drawString("Profile Picture:", 25 + 200 + 5, yPos);
            studentsList.get(selectedStudent).getItem2().drawProfilePicture(g, new Point(25 + 200 + 5, yPos), this);

            g.drawString("Period Number:", btnsX, 175);
            g.drawString("Course Name:", btnsX, 225);
        } else {
            int xPos = 10;
            for (int i = 0; i < studentsList.size(); i++) {
                studentsList.get(i).getItem2().drawProfilePicture(g, new Point(xPos, getPreferredSize().height - 50 - 100), this);
                xPos += 150;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectStudent) {
            selectedStudent = studentsListDisplay.getSelectedIndex();
            if (selectedStudent == -1)
                System.out.println("Selected index was -1...");
            else {
                scheduleListDisplay.setListData(studentsList.get(selectedStudent).getItem1().formatSchedule());
                add(periodInput);
                add(classNameInput);
                add(changePeriod);
                add(deletePeriod);

                studentsListPane.setBounds(25, 25, 200, getPreferredSize().height - 50);
                scheduleListPane.setBounds(25 + 200 + 150, 25, getPreferredSize().width - 25 - 200 - 150 - 25, getPreferredSize().height - 50);
            }
        } else if (e.getSource() == resetAll) {
            scheduleListDisplay.setListData(new String[1]);
            studentsListPane.setBounds(25, 25, 200, getPreferredSize().height - 50 - 130);
            scheduleListPane.setBounds(25 + 200 + 150, 25, getPreferredSize().width - 25 - 200 - 150 - 25, getPreferredSize().height - 50 - 130);
            selectedStudent = -1;
            remove(periodInput);
            remove(classNameInput);
            remove(changePeriod);
            remove(deletePeriod);
        } else if (e.getSource() == changePeriod) {
            int period;
            String courseName = classNameInput.getText();

            try {
                period = Integer.parseInt(periodInput.getText());
            } catch (NumberFormatException err) {
                // oopsie
                JOptionPane.showMessageDialog(null, "Period must be an integer number!");
                return;
            }

            studentsList.get(selectedStudent).getItem1().addPeriod(period, courseName);
            scheduleListDisplay.setListData(studentsList.get(selectedStudent).getItem1().formatSchedule());
        } else if (e.getSource() == deletePeriod) {
            int period;

            try {
                period = Integer.parseInt(periodInput.getText());
            } catch (NumberFormatException err) {
                // oopsie
                JOptionPane.showMessageDialog(null, "Period must be an integer number!");
                return;
            }

            studentsList.get(selectedStudent).getItem1().removePeriod(period);
            scheduleListDisplay.setListData(studentsList.get(selectedStudent).getItem1().formatSchedule());
        }

        repaint();
    }
}
