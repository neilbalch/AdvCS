import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Profile implements Comparable<Profile> {
    private String nameFirst;
    private String nameLast;
    private int dob;

    public Profile(String nameFirst, String nameLast, int dob) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.dob = dob;
    }

    public int getDob() {
        return dob;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public String toString() {
        return nameLast + ", " + nameFirst + " : " + dob;
    }

    public int hashCode() {
        int hashCode = nameFirst.hashCode() * 11;
        hashCode += nameLast.hashCode() * 17;
        hashCode += dob * 19;

        return 23 * hashCode;
    }

    public boolean equals(Object item) {
        Profile i = (Profile) item;
        return nameLast.equalsIgnoreCase(i.getNameFirst()) && nameLast.equalsIgnoreCase(i.getNameLast()) && dob == i.getDob();
    }

    public int compareTo(Profile i) {
        int lastComparison = nameLast.compareToIgnoreCase(i.getNameLast());
        if (lastComparison > 0) return 1;
        else if (lastComparison < 0) return -1;
        else {
            int firstComparison = nameFirst.compareToIgnoreCase(i.getNameFirst());
            if (firstComparison > 0) return 1;
            else if (firstComparison < 0) return -1;
            else {
                if (dob > i.getDob()) return 1;
                else if (dob < i.getDob()) return -1;
                else return 0;
            }
        }
    }
}

class Pair<T, U> {
    public T t;
    public U u;

    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public String toString() {
        return t.toString() + ", Qty: " + u.toString();
    }
}

public class Screen extends JPanel implements ActionListener {
    private int itemsXPos = 275;
    private int itemsWidth = 150;

    private String[] schools;
    private TreeMap<Profile, String> studentsTM;
    private HashMap<Profile, String> studentsHM;
    private final String inputFilePath = "names.txt";
    private String selectedSchool;

    private enum WindowPane {NORMAL, ADMIN}

    ;
    private WindowPane windowState;

    private JList<String> studentsDisplay;
    private JScrollPane studentsDisplayPane;
    private JList<String> detailsDisplay;
    private JScrollPane detailsDisplayPane;

    private JTextField firstNameInput;
    private JTextField lastNameInput;
    private JTextField birthYearInput;
    private JButton displayStudentDetailsBtn;

    private JButton addItemBtn;
    private JButton deleteItemBtn;

    private String[] formatTMasArray(String querySchool) {
        ArrayList<String> returnable = new ArrayList<>();
        for (Map.Entry<Profile, String> entry : studentsTM.entrySet()) {
            Profile key = entry.getKey();
            String value = entry.getValue();

//            System.out.println(key + " => " + value);
            if (!querySchool.equalsIgnoreCase("*")) {
                if (value.equalsIgnoreCase(querySchool)) returnable.add(key.toString());
            } else returnable.add(key.toString());
        }

        return returnable.toArray(new String[1]);
    }

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        studentsHM = new HashMap<>();
        studentsTM = new TreeMap<>();

        schools = new String[]{"Mountain View HS", "Los Altos HS", "Graham MS", "Crittenden MS", "Blach MS", "Huff ES", "Bubb ES", "Homestead HS", "Fremont HS", "Gunn HS"};
        selectedSchool = "*";
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
        studentsDisplay.setListData(formatTMasArray("*"));
        studentsDisplayPane = new JScrollPane(studentsDisplay);
        studentsDisplayPane.setBounds(25, 50, 250, getPreferredSize().height - 50 - 25);
//        schoolsDisplay.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                Object selection = ((JList)e.getSource()).getSelectedValue();
//                String selectedSchool = (String)selection;
//
//                selectedSchool = selectedSchool.substring(selectedSchool.indexOf(":") + 1);
////                System.out.println(selectedSchool);
//                studentsDisplay.setListData(formatTMasArray(selectedSchool));
//            }
//        });
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

        addItemBtn = new JButton("Add Item");
        addItemBtn.setBounds(itemsXPos, 230, itemsWidth, 30);
        addItemBtn.addActionListener(this);
        add(addItemBtn);

        deleteItemBtn = new JButton("Remove Item");
        deleteItemBtn.setBounds(itemsXPos, 270, itemsWidth, 30);
        deleteItemBtn.addActionListener(this);
        add(deleteItemBtn);
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textVerticalOffset = -7;
        if (windowState == WindowPane.NORMAL) {
            g.drawString("Students List:", 25, 50 + textVerticalOffset);
            g.drawString("Student Detail:", 50 + 250 + 125, 50 + textVerticalOffset);

            g.drawString("First name:", itemsXPos, 50 + textVerticalOffset);
            g.drawString("Last price:", itemsXPos, 100 + textVerticalOffset);
            g.drawString("Birth Year:", itemsXPos, 150 + textVerticalOffset);
        } else if (windowState == WindowPane.ADMIN) {

        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == displayStudentDetailsBtn) {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            try {
                int dob = Integer.parseInt(birthYearInput.getText());

                System.out.println(lastName + ", " + firstName + " : " + dob);

                if (!studentsTM.containsKey(new Profile(firstName, lastName, dob))) {
                    JOptionPane.showMessageDialog(null, "ERROR: Such a student doesn't exist!");
                    detailsDisplay.setListData(new String[1]);
                } else {
                    String school = studentsTM.get(new Profile(firstName, lastName, dob));
                    detailsDisplay.setListData(new String[]{"Attends: " + school});
                    // TODO: Add challenge schedule functionality.
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: Birth Year must be an integer!");
            }
        }

        repaint();
    }
}
