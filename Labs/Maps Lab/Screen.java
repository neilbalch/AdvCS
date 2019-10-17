import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Screen extends JPanel implements ActionListener {
    private int itemsXPos = 275;
    private int itemsWidth = 150;

    private final String[] schools = {
            "Mountain View HS",
            "Los Altos HS",
            "Graham MS",
            "Crittenden MS",
            "Blach MS",
            "Huff ES",
            "Bubb ES",
            "Homestead HS",
            "Fremont HS",
            "Gunn HS"
    };
    private TreeMap<Profile, String> studentsTM;
    private HashMap<Profile, String> studentsHM;
    private final String inputFilePath = "names.txt";
    private final String[] availablePeriods = {
            "Marching Band",
            "PE",
            "Physics",
            "Calculus",
            "Photography",
            "FREE PERIOD",
            "Wind Ensemble",
            "English",
            "Engineering",
            "String Orchestra",
            "Earth Science",
            "Trigonometry",
            "Advanced Computer Science"
    };

    private enum WindowPane {NORMAL, ADMIN}
    private WindowPane windowState;

    private JList<String> studentsDisplay;
    private JScrollPane studentsDisplayPane;
    private JList<String> detailsDisplay;
    private JScrollPane detailsDisplayPane;

    private JTextField firstNameInput;
    private JTextField lastNameInput;
    private JTextField birthYearInput;
    private JButton displayStudentDetailsBtn;

    private JButton switchPagesBtn;
    private JButton rmProfileBtn;
    private JTextField schoolInput;
    private JButton changeSchool;
    private JButton addProfile;

    private JButton addPeriod;
    private JButton deletePeriod;
    private JTextField periodInput;

    private String[] formatTMasArray() {
        ArrayList<String> returnable = new ArrayList<>();
        for (Map.Entry<Profile, String> entry : studentsTM.entrySet()) {
            Profile key = entry.getKey();
            String value = entry.getValue();

//            System.out.println(key + " => " + value);
            returnable.add(key.toString());
        }

        return returnable.toArray(new String[1]);
    }

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        studentsHM = new HashMap<>();
        studentsTM = new TreeMap<>();

        windowState = WindowPane.NORMAL;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(inputFilePath);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                String nameFirst = line.substring(0, line.indexOf(" "));
                String nameLast = line.substring(line.indexOf(" ") + 1);
                int birthYear = (int) (Math.random() * 25 + 1990);
                Profile student = new Profile(nameFirst, nameLast, birthYear);
                student.addPeriods(new String[]{
                        availablePeriods[(int) (Math.random() * availablePeriods.length)],
                        availablePeriods[(int) (Math.random() * availablePeriods.length)],
                        availablePeriods[(int) (Math.random() * availablePeriods.length)],
                        availablePeriods[(int) (Math.random() * availablePeriods.length)]
                });

//                System.out.println(nameLast + ", " + nameFirst + " : " + birthYear);

                int school = (int) (Math.random() * schools.length);
                studentsHM.put(student, schools[school]);
                studentsTM.put(student, schools[school]);
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + inputFilePath + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + inputFilePath + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

//        System.out.println(studentsHM);

        studentsDisplay = new JList<>();
        studentsDisplay.setListData(formatTMasArray());
        studentsDisplayPane = new JScrollPane(studentsDisplay);
        studentsDisplayPane.setBounds(25, 50, 250, getPreferredSize().height - 50 - 25);
        add(studentsDisplayPane);

        detailsDisplay = new JList<>();
        detailsDisplay.setListData(new String[1]);
        detailsDisplayPane = new JScrollPane(detailsDisplay);
        detailsDisplayPane.setBounds(50 + 250 + 125, 50, 350, getPreferredSize().height - 50 - 25);
        add(detailsDisplayPane);

        firstNameInput = new JTextField();
        firstNameInput.setBounds(itemsXPos, 50, itemsWidth, 30);
        add(firstNameInput);

        lastNameInput = new JTextField();
        lastNameInput.setBounds(itemsXPos, 100, itemsWidth, 30);
        add(lastNameInput);

        birthYearInput = new JTextField();
        birthYearInput.setBounds(itemsXPos, 150, itemsWidth, 30);
        add(birthYearInput);

        displayStudentDetailsBtn = new JButton("Get Student Details");
        displayStudentDetailsBtn.setBounds(itemsXPos, 190, itemsWidth, 30);
        displayStudentDetailsBtn.addActionListener(this);
        add(displayStudentDetailsBtn);

        rmProfileBtn = new JButton("Remove Profile");
        rmProfileBtn.setBounds(itemsXPos, 190, itemsWidth, 30);
        rmProfileBtn.addActionListener(this);
        rmProfileBtn.setVisible(false);
        add(rmProfileBtn);

        schoolInput = new JTextField();
        schoolInput.setBounds(itemsXPos, 240, itemsWidth, 30);
        schoolInput.setVisible(false);
        add(schoolInput);

        changeSchool = new JButton("Change School");
        changeSchool.setBounds(itemsXPos, 280, itemsWidth, 30);
        changeSchool.addActionListener(this);
        changeSchool.setVisible(false);
        add(changeSchool);

        addProfile = new JButton("Add Profile");
        addProfile.setBounds(itemsXPos, 320, itemsWidth, 30);
        addProfile.addActionListener(this);
        addProfile.setVisible(false);
        add(addProfile);

        switchPagesBtn = new JButton("Admin View");
        switchPagesBtn.setBounds(itemsXPos, getPreferredSize().height - 100, itemsWidth, 30);
        switchPagesBtn.addActionListener(this);
        add(switchPagesBtn);

        periodInput = new JTextField();
        periodInput.setBounds(itemsXPos, 370, itemsWidth, 30);
        add(periodInput);

        addPeriod = new JButton("Add Period");
        addPeriod.setBounds(itemsXPos, 410, itemsWidth, 30);
        addPeriod.addActionListener(this);
        add(addPeriod);

        deletePeriod = new JButton("Remove Period");
        deletePeriod.setBounds(itemsXPos, 450, itemsWidth, 30);
        deletePeriod.addActionListener(this);
        add(deletePeriod);
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textVerticalOffset = -7;
        g.drawString("Students List:", 25, 50 + textVerticalOffset);
        g.drawString("Student Detail:", 50 + 250 + 125, 50 + textVerticalOffset);

        g.drawString("First name:", itemsXPos, 50 + textVerticalOffset);
        g.drawString("Last price:", itemsXPos, 100 + textVerticalOffset);
        g.drawString("Birth Year:", itemsXPos, 150 + textVerticalOffset);

        g.drawString("Period:", itemsXPos, 370 + textVerticalOffset);

        if (windowState == WindowPane.NORMAL) {
        } else if (windowState == WindowPane.ADMIN) {
            g.drawString("School Name:", itemsXPos, 240 + textVerticalOffset);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == displayStudentDetailsBtn) {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();

            if (firstName.equals("") || lastName.equals(""))
                JOptionPane.showMessageDialog(null, "ERROR: first name and last name must be provided!");

            try {
                int dob = Integer.parseInt(birthYearInput.getText());

                if (!studentsTM.containsKey(new Profile(firstName, lastName, dob))) {
                    JOptionPane.showMessageDialog(null, "ERROR: Such a student doesn't exist!");
                    detailsDisplay.setListData(new String[1]);
                } else {
                    Profile query = new Profile(firstName, lastName, dob);
                    String school = studentsTM.get(query);
                    Set<Profile> keys = studentsTM.keySet();
                    String[] periods = new String[]{school};
                    for (Profile profile : keys) {
                        if (profile.equals(query)) {
                            periods = profile.formatAllAsStrings();
                        }
                    }
//                    detailsDisplay.setListData(new String[]{"Attends: " + school});
                    detailsDisplay.setListData(periods);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Birth Year must be an integer!");
            }
        } else if (e.getSource() == switchPagesBtn) {
            if (windowState == WindowPane.NORMAL) {
//                detailsDisplayPane.setVisible(false);
//                studentsDisplayPane.setVisible(false);
//                firstNameInput.setVisible(false);
//                lastNameInput.setVisible(false);
//                birthYearInput.setVisible(false);
                displayStudentDetailsBtn.setVisible(false);
                rmProfileBtn.setVisible(true);
                addProfile.setVisible(true);
                changeSchool.setVisible(true);
                schoolInput.setVisible(true);

                switchPagesBtn.setText("Normal View");
                windowState = WindowPane.ADMIN;
            } else if (windowState == WindowPane.ADMIN) {
//                detailsDisplayPane.setVisible(true);
//                studentsDisplayPane.setVisible(true);
//                firstNameInput.setVisible(true);
//                lastNameInput.setVisible(true);
//                birthYearInput.setVisible(true);
                displayStudentDetailsBtn.setVisible(true);
                rmProfileBtn.setVisible(false);
                addProfile.setVisible(false);
                changeSchool.setVisible(false);
                schoolInput.setVisible(false);

                switchPagesBtn.setText("Admin View");
                windowState = WindowPane.NORMAL;
            }
        } else if (e.getSource() == rmProfileBtn) {
            if (firstNameInput.getText().equals("") || lastNameInput.getText().equals("") || birthYearInput.getText().equals(""))
                JOptionPane.showMessageDialog(null, "ERROR: First name, last name, and birth year must be completed!");

            try {
                String first = firstNameInput.getText();
                String last = lastNameInput.getText();
                int dob = Integer.parseInt(birthYearInput.getText());

                if (!studentsTM.containsKey(new Profile(first, last, dob))) {
                    JOptionPane.showMessageDialog(null, "ERROR: Profile as described doesn't exist!");
                } else {
                    studentsHM.remove(new Profile(first, last, dob));
                    studentsTM.remove(new Profile(first, last, dob));
                    studentsDisplay.setListData(formatTMasArray());
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Year of birth must be an integer!");
            }
        } else if (e.getSource() == addProfile) {
            if (firstNameInput.getText().equals("") || lastNameInput.getText().equals("") || birthYearInput.getText().equals("") || schoolInput.getText().equals(""))
                JOptionPane.showMessageDialog(null, "ERROR: First name, last name, birth year, and school name must be completed!");

            try {
                String first = firstNameInput.getText();
                String last = lastNameInput.getText();
                int dob = Integer.parseInt(birthYearInput.getText());
                String school = schoolInput.getText();

                Profile newProfile = new Profile(first, last, dob);
                studentsHM.put(newProfile, school);
                studentsTM.put(newProfile, school);
                studentsDisplay.setListData(formatTMasArray());
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Year of birth must be an integer!");
            }
        } else if (e.getSource() == changeSchool) {
            if (firstNameInput.getText().equals("") || lastNameInput.getText().equals("") || birthYearInput.getText().equals("") || schoolInput.getText().equals(""))
                JOptionPane.showMessageDialog(null, "ERROR: First name, last name, birth year, and school name must be completed!");

            try {
                String first = firstNameInput.getText();
                String last = lastNameInput.getText();
                int dob = Integer.parseInt(birthYearInput.getText());
                String newSchoolName = schoolInput.getText();
                Profile queryProfile = new Profile(first, last, dob);

                if (!studentsHM.containsKey(queryProfile)) {
                    studentsDisplay.setListData(formatTMasArray());
                    JOptionPane.showMessageDialog(null, "ERROR: Profile as described doesn't exist!");
                } else {
                    studentsHM.put(queryProfile, newSchoolName);
                    studentsTM.put(queryProfile, newSchoolName);
                    studentsDisplay.setListData(formatTMasArray());
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Year of birth must be an integer!");
            }
        } else if (e.getSource() == addPeriod) {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String newPeriod = periodInput.getText();

            if (firstName.equals("") || lastName.equals("") || newPeriod.equals(""))
                JOptionPane.showMessageDialog(null, "ERROR: first name, last name, and period name must be provided!");

            try {
                int dob = Integer.parseInt(birthYearInput.getText());

                if (!studentsTM.containsKey(new Profile(firstName, lastName, dob))) {
                    JOptionPane.showMessageDialog(null, "ERROR: Such a student doesn't exist!");
                    detailsDisplay.setListData(new String[1]);
                } else {
                    Profile query = new Profile(firstName, lastName, dob);
                    String school = studentsTM.get(query);
                    Set<Profile> keys = studentsTM.keySet();
                    String[] periods = new String[]{school};
                    for (Profile profile : keys) {
                        if (profile.equals(query)) {
                            profile.addPeriods(new String[]{newPeriod});
                            periods = profile.formatAllAsStrings();
                        }
                    }
//                    detailsDisplay.setListData(new String[]{"Attends: " + school});
                    detailsDisplay.setListData(periods);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Birth Year must be an integer!");
            }
        } else if (e.getSource() == deletePeriod) {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String periodToDelete = periodInput.getText();

            if (firstName.equals("") || lastName.equals("") || periodToDelete.equals(""))
                JOptionPane.showMessageDialog(null, "ERROR: first name, last name, and period name must be provided!");

            try {
                int dob = Integer.parseInt(birthYearInput.getText());

                if (!studentsTM.containsKey(new Profile(firstName, lastName, dob))) {
                    JOptionPane.showMessageDialog(null, "ERROR: Such a student doesn't exist!");
                    detailsDisplay.setListData(new String[1]);
                } else {
                    Profile query = new Profile(firstName, lastName, dob);
                    String school = studentsTM.get(query);
                    Set<Profile> keys = studentsTM.keySet();
                    String[] periods = new String[]{school};
                    for (Profile profile : keys) {
                        if (profile.equals(query)) {
                            if (!profile.removePeriod(periodToDelete))
                                JOptionPane.showMessageDialog(null, "ERROR: Such a period doesn't exist on the selected profile!");

                            periods = profile.formatAllAsStrings();
                        }
                    }
                    //                    detailsDisplay.setListData(new String[]{"Attends: " + school});
                    detailsDisplay.setListData(periods);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Birth Year must be an integer!");
            }

        }

        repaint();
    }
}
