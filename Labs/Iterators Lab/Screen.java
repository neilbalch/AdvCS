import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ResumeSection {
    public String sectionName;
    public ArrayList<String> items;
}

public class Screen extends JPanel implements ActionListener {
    private JScrollPane resumeScrollPane;
    private JList resumeLst;
    private ArrayList<ResumeSection> resumeList;
    private int currentScreen = 1;

//    private JButton selectStudent;
//    private int selectedStudent = -1;
//    private JButton changePeriod;
//    private JButton resetAll;
//    private JButton deletePeriod;
//    private JTextField periodInput;
//    private JTextField classNameInput;

    private int btnsX = 25 + 200 + 5;

    private String[] formatResumeAsArray() {
        ArrayList<String> flattened = new ArrayList<>();
        for (ResumeSection section : resumeList) {
            flattened.add(section.sectionName);

            for (String line : section.items) {
                flattened.add("    - " + line);
            }

            flattened.add("");
        }

        return (String[]) flattened.toArray();
    }

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        resumeList = new ArrayList<>();

        resumeLst = new JList(new String[1]);
        resumeLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resumeLst.setLayoutOrientation(JList.VERTICAL);

        resumeScrollPane = new JScrollPane(resumeLst);
        resumeScrollPane.setBounds(25, 25, 200, getPreferredSize().height - 50 - 130);
        add(resumeScrollPane);

//
//        selectStudent = new JButton("Select Student");
//        selectStudent.setBounds(btnsX, 50, 140, 30);
//        selectStudent.addActionListener(this);
//        add(selectStudent);
//
//        resetAll = new JButton("Reset Selection");
//        resetAll.setBounds(btnsX, 90, 140, 30);
//        resetAll.addActionListener(this);
//        add(resetAll);
//
//        periodInput = new JTextField();
//        periodInput.setBounds(btnsX, 180, 140, 30);
////        add(periodInput);
//
//        classNameInput = new JTextField();
//        classNameInput.setBounds(btnsX, 230, 140, 30);
////        add(classNameInput);
//
//        changePeriod = new JButton("Change Period");
//        changePeriod.setBounds(btnsX, 270, 140, 30);
//        changePeriod.addActionListener(this);
////        add(changePeriod);
//
//        deletePeriod = new JButton("Delete Period");
//        deletePeriod.setBounds(btnsX, 310, 140, 30);
//        deletePeriod.addActionListener(this);
////        add(deletePeriod);
    }

    //Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (currentScreen) {
            case 1: // query name, address, email, objectives, and skills
                break;
            case 2: // query education info, sort by date in reverse chronological order
                break;
            case 3: //
                break;
        }

//        g.drawString("Students List:", 25, 20);
//        g.drawString("Schedule Breakdown: ", 25 + 200 + 150, 20);

//        if (selectedStudent != -1) {
//            int yPos = getPreferredSize().height - 25 - 140;
//            g.drawString("Profile Picture:", 25 + 200 + 5, yPos);
//            studentsList.get(selectedStudent).getItem2().drawProfilePicture(g, new Point(25 + 200 + 5, yPos), this);
//
//            g.drawString("Period Number:", btnsX, 175);
//            g.drawString("Course Name:", btnsX, 225);
//        } else {
//            int xPos = 10;
//            for (int i = 0; i < studentsList.size(); i++) {
//                studentsList.get(i).getItem2().drawProfilePicture(g, new Point(xPos, getPreferredSize().height - 50 - 100), this);
//                xPos += 150;
//            }
//        }
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
