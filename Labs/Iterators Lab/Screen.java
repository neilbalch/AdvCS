import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class ResumeSection {
    public String sectionName;
    public ArrayList<String> items;

    public ResumeSection(String sectionName, String[] initialItems) {
        this.sectionName = sectionName;
        this.items = new ArrayList<>();

        if (initialItems != null) {
            for (String line : initialItems) {
                if (line != null) this.items.add(line);
            }
        }
    }
}

public class Screen extends JPanel implements ActionListener {
    private JScrollPane resumeScrollPane;
    private JList resumeLst;
    private ArrayList<ResumeSection> resumeList;
    private int currentScreen = 1;

    private JButton submitFirstPage;
//    private int selectedStudent = -1;
//    private JButton changePeriod;
//    private JButton resetAll;
//    private JButton deletePeriod;
private JTextField nameInput;
    private JTextField addressInput;
    private JTextField emailInput;
    private JTextField objectiveInput;
    private JTextArea skillsInput;
    private JTextArea educationInput;

    private int itemsXPos = 100;
    private int items2ndColXPos = 400;
    private int itemsWidth = 250;

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

//        resumeLst = new JList(new String[1]);
//        resumeLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        resumeLst.setLayoutOrientation(JList.VERTICAL);
//
//        resumeScrollPane = new JScrollPane(resumeLst);
//        resumeScrollPane.setBounds(25, 25, 200, getPreferredSize().height - 50 - 130);
//        add(resumeScrollPane);

//        resetAll = new JButton("Reset Selection");
//        resetAll.setBounds(btnsX, 90, 140, 30);
//        resetAll.addActionListener(this);
//        add(resetAll);
//
        nameInput = new JTextField();
        nameInput.setBounds(itemsXPos, 100, itemsWidth, 30);
        add(nameInput);

        addressInput = new JTextField();
        addressInput.setBounds(itemsXPos, 165, itemsWidth, 30);
        add(addressInput);

        emailInput = new JTextField();
        emailInput.setBounds(itemsXPos, 215, itemsWidth, 30);
        add(emailInput);

        objectiveInput = new JTextField();
        objectiveInput.setBounds(itemsXPos, 265, itemsWidth, 30);
        add(objectiveInput);

        educationInput = new JTextArea();
        educationInput.setBounds(itemsXPos, 330, itemsWidth, 100);
        add(educationInput);

        skillsInput = new JTextArea();
        skillsInput.setBounds(items2ndColXPos, 100, itemsWidth, 315);
        add(skillsInput);

        submitFirstPage = new JButton("Continue to Prior Work Experience");
        submitFirstPage.setBounds(itemsXPos, 435, itemsWidth, 30);
        submitFirstPage.addActionListener(this);
        add(submitFirstPage);
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

        int textVerticalOffset = -7;
        switch (currentScreen) {
            case 1: // query name, address, email, objectives, and skills, query education info, sort by date in reverse chronological order
                g.drawString("Name: (Format: First & Last)", itemsXPos, 100 + textVerticalOffset);
                g.drawString("Address: (Format: xxxx STREET NAME,", itemsXPos, 150 + textVerticalOffset);
                g.drawString("CITY NAME, STATE NAME", itemsXPos, 165 + textVerticalOffset);
                g.drawString("Email: (Format: xxx@yyy.zzz)", itemsXPos, 215 + textVerticalOffset);
                g.drawString("Application Objective:", itemsXPos, 265 + textVerticalOffset);
                g.drawString("Applicable Skills: (Format: one per line!)", itemsXPos, 315 + textVerticalOffset);
                g.drawString("BRIEF DESCRIPTION: LONGER DESCRIPTION)", items2ndColXPos, 330 + textVerticalOffset);

                g.drawString("Applicable Education: (Format: one per line!,", items2ndColXPos, 85 + textVerticalOffset);
                // TODO(Neil): Add graduation info...
                g.drawString("BRIEF DESCRIPTION: LONGER DESCRIPTION)", items2ndColXPos, 100 + textVerticalOffset);
                break;
            case 2: //
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
        if (e.getSource() == submitFirstPage) {
            resumeList.add(new ResumeSection(nameInput.getText(), null));
            resumeList.add(new ResumeSection("Personal Info:", new String[]{
                    "Street Address: " + addressInput.getText(),
                    "Email: " + emailInput.getText()
            }));
            resumeList.add(new ResumeSection("Application Objective:", new String[]{
                    objectiveInput.getText()
            }));

            String[] skills = skillsInput.getText().split("\n");
            resumeList.add(new ResumeSection("Applicable Skills:", skills));

            String[] education = educationInput.getText().split("\n");
            resumeList.add(new ResumeSection("Applicable Education:", education));

            remove(nameInput);
            remove(addressInput);
            remove(emailInput);
            remove(objectiveInput);
            remove(skillsInput);
            remove(educationInput);
            currentScreen++;
        }
        repaint();
    }
}
