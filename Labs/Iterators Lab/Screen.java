import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
    private ArrayList<ResumeSection> resumeList;
    private int itemsXPos = 100;
    private int itemsWidth = 250;
    private int items2ndColXPos = itemsXPos + itemsWidth + 50;
    private int currentScreen = 1;

    // First screen
    private JButton submitFirstPage;
    private JTextField nameInput;
    private JTextField addressInput;
    private JTextField emailInput;
    private JTextField objectiveInput;
    private JTextArea skillsInput;
    private JScrollPane skillsScrollPane;
    private JTextArea educationInput;
    private JScrollPane educationScrollPane;

    // Second screen
    private JTextArea workInput;
    private JScrollPane workScrollPane;
    private JButton submitSecondPage;

    // Third screen
    private JTextArea resumeDisplay;
    private JScrollPane resumeScrollPane;

    private String[] formatResumeAsArray() {
        ArrayList<String> flattened = new ArrayList<>();
//        for (ResumeSection section : resumeList) {
//            flattened.add(section.sectionName);
//
//            for (String line : section.items)
//                flattened.add("    - " + line);
//
//            flattened.add("");
//        }

        Iterator<ResumeSection> iterOuter = resumeList.listIterator();
        while (iterOuter.hasNext()) {
            ResumeSection current = iterOuter.next();
            flattened.add(current.sectionName);

            Iterator<String> iterInner = current.items.listIterator();
            while (iterInner.hasNext()) {
                flattened.add("    - " + iterInner.next());
            }

            flattened.add("");
        }

        return flattened.toArray(new String[1]);
    }

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);
        resumeList = new ArrayList<>();

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
        add(skillsInput);
        skillsScrollPane = new JScrollPane(skillsInput);
        skillsScrollPane.setBounds(itemsXPos, 330, itemsWidth, 100);
        add(skillsScrollPane);

        educationInput = new JTextArea();
        add(educationInput);
        educationScrollPane = new JScrollPane(educationInput);
        educationScrollPane.setBounds(items2ndColXPos, 115, itemsWidth, 315);
        add(educationScrollPane);

        submitFirstPage = new JButton("Continue to Prior Work Experience");
        submitFirstPage.setBounds(itemsXPos, 435, itemsWidth, 30);
        submitFirstPage.addActionListener(this);
        add(submitFirstPage);

        workInput = new JTextArea();
        add(workInput);
        workScrollPane = new JScrollPane(workInput);
        workScrollPane.setBounds(itemsXPos, 100, (int) (2.5 * itemsWidth), 315);
        add(workScrollPane);
        workScrollPane.setVisible(false);

        submitSecondPage = new JButton("Continue to Resume View");
        submitSecondPage.setBounds(itemsXPos, 435, itemsWidth, 30);
        submitSecondPage.addActionListener(this);

        resumeDisplay = new JTextArea();
        resumeDisplay.setEditable(false);
        resumeScrollPane = new JScrollPane(resumeDisplay);
        resumeScrollPane.setBounds(itemsXPos, 100, (int) (2.5 * itemsWidth), 315);
    }

    // Sets the size of the panel
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
                g.drawString("DIPLOMA from INSTITUTION: (YYYY-MM) LONGER DESCRIPTION)", items2ndColXPos, 115 + textVerticalOffset);
                break;
            case 2: // query work experience
                g.drawString("Applicable Work Experience: (Format: one per line!, POSITION at COMPANY: (YYYY-MM to YYYY-MM) DESCRIPTION)", itemsXPos, 100 + textVerticalOffset);
                break;
            case 3: // resume display
                g.drawString("Your Resume:", itemsXPos, 100 + textVerticalOffset);
                String toDisplay = "";
                String[] raw = formatResumeAsArray();
                for (String line : raw) toDisplay += line + "\n";
                resumeDisplay.setText(toDisplay);
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        ListIterator<ResumeSection> iter = resumeList.listIterator();
        if (e.getSource() == submitFirstPage) {
            while (iter.hasNext()) iter.next();
            iter.add(new ResumeSection(nameInput.getText(), null));
//            resumeList.add(new ResumeSection(nameInput.getText(), null));
            while (iter.hasNext()) iter.next();
            iter.add(new ResumeSection("Personal Info:", new String[]{
                    "Street Address: " + addressInput.getText(),
                    "Email: " + emailInput.getText()
            }));
//            resumeList.add(new ResumeSection("Personal Info:", new String[]{
//                    "Street Address: " + addressInput.getText(),
//                    "Email: " + emailInput.getText()
//            }));
            while (iter.hasNext()) iter.next();
            iter.add(new ResumeSection("Application Objective:", new String[]{
                    objectiveInput.getText()
            }));
//            resumeList.add(new ResumeSection("Application Objective:", new String[]{
//                    objectiveInput.getText()
//            }));

            String[] skills = skillsInput.getText().split("\n");
            while (iter.hasNext()) iter.next();
            iter.add(new ResumeSection("Applicable Skills:", skills));
//            resumeList.add(new ResumeSection("Applicable Skills:", skills));

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

            while (iter.hasNext()) iter.next();
            iter.add(new ResumeSection("Applicable Education:", education));
//            resumeList.add(new ResumeSection("Applicable Education:", education));

            remove(nameInput);
            remove(addressInput);
            remove(emailInput);
            remove(objectiveInput);
            remove(skillsInput);
            remove(skillsScrollPane);
            remove(educationInput);
            remove(educationScrollPane);
            remove(submitFirstPage);
            currentScreen++;
//            add(workInput);
//            add(workScrollPane);
            workScrollPane.setVisible(true);
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
            while (iter.hasNext()) iter.next();
            iter.add(new ResumeSection("Applicable Work Experience:", work));
//            resumeList.add(new ResumeSection("Applicable Work Experience:", work));

//            remove(workInput);
//            remove(workScrollPane);
            workScrollPane.setVisible(false);
            remove(submitSecondPage);
            currentScreen++;
            add(resumeScrollPane);
        }
        repaint();
    }
}
