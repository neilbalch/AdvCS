import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

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

    private JTextArea workInput;
    private JButton submitSecondPage;

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

        skillsInput = new JTextArea();
        skillsInput.setBounds(itemsXPos, 330, itemsWidth, 100);
        add(skillsInput);

        educationInput = new JTextArea();
        educationInput.setBounds(items2ndColXPos, 115, itemsWidth, 315);
        add(educationInput);

        submitFirstPage = new JButton("Continue to Prior Work Experience");
        submitFirstPage.setBounds(itemsXPos, 435, itemsWidth, 30);
        submitFirstPage.addActionListener(this);
        add(submitFirstPage);

        workInput = new JTextArea();
        workInput.setBounds(itemsXPos, 100, (int) (2.5 * itemsWidth), 315);
//        add(workInput);

        submitSecondPage = new JButton("Continue to Resume View");
        submitSecondPage.setBounds(itemsXPos, 435, itemsWidth, 30);
        submitSecondPage.addActionListener(this);
//        add(submitSecondPage);

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
                g.drawString("Applicable Skills: (Format: one per line!", itemsXPos, 315 + textVerticalOffset);
                g.drawString("BRIEF DESCRIPTION: LONGER DESCRIPTION)", itemsXPos, 330 + textVerticalOffset);

                g.drawString("Applicable Education: (Format: one per line!,", items2ndColXPos, 100 + textVerticalOffset);
                // TODO(Neil): Add graduation info...
                g.drawString("DIPLOMA from INSTITUTION: (YYYY-MM) LONGER DESCRIPTION)", items2ndColXPos, 115 + textVerticalOffset);
                break;
            case 2: // query work experience
                // TODO(Neil): Add date support...
                g.drawString("Applicable Work Experience: (Format: one per line!, POSITION at COMPANY: (YYYY-MM to YYYY-MM) DESCRIPTION)", itemsXPos, 100 + textVerticalOffset);
                break;
            case 3: //
                break;
        }
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

            // DIPLOMA from INSTITUTION: (YYYY-MM) LONGER DESCRIPTION
            String[] education = educationInput.getText().split("\n");
            Arrays.sort(education, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String dateStr1 = o1.substring(o1.indexOf("(") + 1, o1.indexOf("-")) + o1.substring(o1.indexOf("-") + 1, o1.indexOf(")"));
                    String dateStr2 = o2.substring(o2.indexOf("(") + 1, o2.indexOf("-")) + o2.substring(o2.indexOf("-") + 1, o2.indexOf(")"));

                    try {
                        int date1 = Integer.parseInt(dateStr1);
                        int date2 = Integer.parseInt(dateStr2);

                        if (date1 > date2) return -1;
                        else if (date1 < date2) return 1;
                        else return 0;
                    } catch (NumberFormatException err) {
                        System.out.println("NUMBER FORMAT EXCEPTION: o1: " + o1 + ", o2: " + o2);
                        return 0;
                    }
                }
            });
            resumeList.add(new ResumeSection("Applicable Education:", education));

            remove(nameInput);
            remove(addressInput);
            remove(emailInput);
            remove(objectiveInput);
            remove(skillsInput);
            remove(educationInput);
            remove(submitFirstPage);
            currentScreen++;
            add(workInput);
            add(submitSecondPage);
        } else if (e.getSource() == submitSecondPage) {
            // POSITION at COMPANY: (YYYY-MM to YYYY-MM) DESCRIPTION
            String[] work = workInput.getText().split("\n");
            Arrays.sort(work, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String endDateStr1 = o1.substring(o1.indexOf("to ") + 3, o1.indexOf("-", o1.indexOf("to "))) + o1.substring(o1.indexOf("-", o1.indexOf("to ")) + 1, o1.indexOf(")"));
                    String endDateStr2 = o2.substring(o2.indexOf("to ") + 3, o2.indexOf("-", o2.indexOf("to "))) + o2.substring(o2.indexOf("-", o2.indexOf("to ")) + 1, o2.indexOf(")"));

                    try {
                        int endDate1 = Integer.parseInt(endDateStr1);
                        int endDate2 = Integer.parseInt(endDateStr2);

                        if (endDate1 > endDate2) return -1;
                        else if (endDate1 < endDate2) return 1;
                        else return 0;
                    } catch (NumberFormatException err) {
                        System.out.println("NUMBER FORMAT EXCEPTION: o1: " + o1 + ", o2: " + o2);
                        return 0;
                    }
                }
            });
            resumeList.add(new ResumeSection("Applicable Work Experience:", work));

            remove(workInput);
            remove(submitSecondPage);
            currentScreen++;
        }
        repaint();
    }
}
