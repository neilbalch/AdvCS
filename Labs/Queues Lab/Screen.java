import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Patient implements Comparable<Patient> {
    private String name;
    private java.util.Date date;
    private String description;
    private CasePriority priority;
    private Age age;
    private String doctorsNote;
    private boolean discharged;

    public enum CasePriority {
        LOW(0), MEDIUM(1), HIGH(2);

        private int value;

        CasePriority(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }

        int compare(CasePriority t) {
            return value - t.getValue();
        }

        public static String[] names() {
            java.util.LinkedList<String> list = new LinkedList<String>();
            for (CasePriority s : CasePriority.values()) {
                list.add(s.name());
            }

            return list.toArray(new String[1]);
        }
    }

    public enum Age {
        CHILD(1), ADULT(0);

        private int value;

        Age(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }

        int compare(Age t) {
            return value - t.getValue();
        }

        public static String[] names() {
            java.util.LinkedList<String> list = new LinkedList<String>();
            for (Age s : Age.values()) {
                list.add(s.name());
            }

            return list.toArray(new String[1]);
        }
    }

    public Patient(String name, String description, CasePriority priority, Age age) {
        this.name = name;
        this.date = new java.util.Date(System.currentTimeMillis());
        this.description = description;
        this.priority = priority;
        this.age = age;
        this.doctorsNote = "";
        this.discharged = false;
    }

    @Override
    public String toString() {
        if (discharged) return name + ", " + doctorsNote;
        else return date + " : " + name + ", " + description + ", " + priority + ", " + age;
    }

    public String formatForDisplay() {
        return "Patient Name: " + name + "\n  - Intake date: " + date + "\n  - Injury Description: " +
                description + "\n  - Care Priority: " + priority.name() + "\n  - Age: " + age.name();
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public CasePriority getPriority() {
        return priority;
    }

    public void updatePriority(CasePriority p) {
        this.priority = p;
    }

    public Age getAge() {
        return age;
    }

    public String getDoctorsNote() {
        return doctorsNote;
    }

    public void discharge(String message) {
        this.discharged = true;
        this.doctorsNote = message;
    }

    @Override
    public boolean equals(Object item) {
        Patient i = (Patient) item;
        return i.getName().equalsIgnoreCase(name) && i.getDate().equals(date)
                && i.getDescription().equalsIgnoreCase(description) && i.getPriority().equals(priority)
                && i.getAge().equals(age) && i.getDoctorsNote().equalsIgnoreCase(doctorsNote);
    }

    @Override
    public int compareTo(Patient i) {
        if (this.equals(i)) return 0;
        else if (priority.compare(i.getPriority()) == 0) {
            if (age.compare(i.getAge()) == 0) {
                if (date.compareTo(i.getDate()) == 0) return 0;
                else return date.compareTo(i.getDate());
            } else return -1 * age.compare(i.getAge());
        } else return -1 * priority.compare(i.getPriority());
    }
}

public class Screen extends JPanel implements ActionListener {
    private int itemsXPos = 25;
    private int itemsWidth = 150;

    private PriorityQueue<Patient> pQueue;
    private Queue<Patient> queue;
    private JButton switchViews;
    private boolean inDoctorView;

    private JList<String> queueDisplay;
    private JScrollPane queueDisplayPane;
    private JList<String> pQueueDisplay;
    private JScrollPane pQueueDisplayPane;

    private JTextField nameInput;
    private JTextField illnessDescription;
    private JComboBox prioritySelection;
    private JComboBox ageSelection;
    private JButton addPatientBtn;
    private JButton updatePatientPriorityBtn;
    private JButton updatePatientIllnessBtn;

    private Patient currentPatient;
    private JTextArea patientDescriptor;
    private JTextField dischargeMessage;
    private JButton dischargePatientBtn;

    private String[] formatPQueueAsArray() {
        Iterator iter = pQueue.iterator();
        ArrayList<String> lines = new ArrayList<>();

        while (iter.hasNext()) lines.add(iter.next().toString());

        return lines.toArray(new String[1]);
    }

    private String[] formatQueueAsArray() {
        Iterator iter = queue.iterator();
        ArrayList<String> lines = new ArrayList<>();

        while (iter.hasNext()) lines.add(iter.next().toString());

        return lines.toArray(new String[1]);
    }

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        pQueue = new PriorityQueue<>();
        queue = new LinkedList<>();
        inDoctorView = false;
        currentPatient = null;

        pQueue.add(new Patient("John Deere", "Fell off a cliff", Patient.CasePriority.HIGH, Patient.Age.ADULT));
        pQueue.add(new Patient("Teddy Roosevelt", "Mauled by a back bear", Patient.CasePriority.MEDIUM, Patient.Age.ADULT));
        pQueue.add(new Patient("Jack Bauer", "Has a paper cut", Patient.CasePriority.LOW, Patient.Age.CHILD));

        queueDisplay = new JList<>();
        queueDisplay.setListData(formatQueueAsArray());
        queueDisplayPane = new JScrollPane(queueDisplay);
        queueDisplayPane.setBounds(itemsXPos + 225, 330, getPreferredSize().width - itemsXPos - 200 - 25, (getPreferredSize().height - 50 - 25) / 2);
        queueDisplayPane.setVisible(false);
        add(queueDisplayPane);

        pQueueDisplay = new JList<>();
        pQueueDisplay.setListData(formatPQueueAsArray());
        pQueueDisplayPane = new JScrollPane(pQueueDisplay);
        pQueueDisplayPane.setBounds(itemsXPos + 225, 50, getPreferredSize().width - itemsXPos - 200 - 25, (getPreferredSize().height - 50 - 25) / 2);
        add(pQueueDisplayPane);

        nameInput = new JTextField();
        nameInput.setBounds(itemsXPos, 70, itemsWidth, 30);
        add(nameInput);

        illnessDescription = new JTextField();
        illnessDescription.setBounds(itemsXPos, 120, itemsWidth, 30);
        add(illnessDescription);

        prioritySelection = new JComboBox(Patient.CasePriority.names());
        prioritySelection.setBounds(itemsXPos, 170, itemsWidth, 30);
        add(prioritySelection);

        ageSelection = new JComboBox(Patient.Age.names());
        ageSelection.setBounds(itemsXPos, 220, itemsWidth, 30);
        add(ageSelection);

        addPatientBtn = new JButton("Add Patient");
        addPatientBtn.setBounds(itemsXPos, 260, itemsWidth, 30);
        addPatientBtn.addActionListener(this);
        add(addPatientBtn);

        switchViews = new JButton("Switch to Doctor View");
        switchViews.setBounds(itemsXPos, getPreferredSize().height - 75, (int) (itemsWidth * 1.25), 30);
        switchViews.addActionListener(this);
        add(switchViews);

        updatePatientIllnessBtn = new JButton("Update Illness");
        updatePatientIllnessBtn.setBounds(itemsXPos, 300, itemsWidth, 30);
        updatePatientIllnessBtn.addActionListener(this);
        add(updatePatientIllnessBtn);

        updatePatientPriorityBtn = new JButton("Update Priority");
        updatePatientPriorityBtn.setBounds(itemsXPos, 340, itemsWidth, 30);
        updatePatientPriorityBtn.addActionListener(this);
        add(updatePatientPriorityBtn);

        ////////////////////////////////////////////////////////////////////////////////////////////

        patientDescriptor = new JTextArea();
        patientDescriptor.setBounds(itemsXPos, 60, getPreferredSize().width - 60 - 50, 200);
        patientDescriptor.setVisible(false);
        patientDescriptor.setEditable(false);
        add(patientDescriptor);

        dischargeMessage = new JTextField();
        dischargeMessage.setBounds(itemsXPos, 280, getPreferredSize().width - 60 - 50, 30);
        dischargeMessage.setVisible(false);
        add(dischargeMessage);

        dischargePatientBtn = new JButton("Discharge Patient");
        dischargePatientBtn.setBounds(itemsXPos, 320, itemsWidth, 30);
        dischargePatientBtn.addActionListener(this);
        dischargePatientBtn.setVisible(false);
        add(dischargePatientBtn);
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textVerticalOffset = -7;
        if (inDoctorView) {
            g.drawString("Current Patient:", itemsXPos, 60 + textVerticalOffset);
            g.drawString("Discharge Note:", itemsXPos, 280 + textVerticalOffset);

            g.drawString("Queue of Discharged Patients:", itemsXPos + 225, 330 + textVerticalOffset);
        } else {
            g.drawString("Add a Patient:", itemsXPos, 50 + textVerticalOffset);
            g.drawString("Priority Queue of Patients:", itemsXPos + 225, 50 + textVerticalOffset);

            g.drawString("Patient Name:", itemsXPos, 70 + textVerticalOffset);
            g.drawString("Ailment Description:", itemsXPos, 120 + textVerticalOffset);
            g.drawString("Patient Priority:", itemsXPos, 170 + textVerticalOffset);
            g.drawString("Patient Age:", itemsXPos, 220 + textVerticalOffset);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPatientBtn) {
            String name = nameInput.getText();
            String description = illnessDescription.getText();
            String priorityStr = (String) prioritySelection.getSelectedItem();
            String ageStr = (String) ageSelection.getSelectedItem();

            Patient.CasePriority priority = null;
            Patient.Age age = null;
            for (Patient.CasePriority cp : Patient.CasePriority.values()) {
                if (cp.name().equalsIgnoreCase(priorityStr)) priority = cp;
            }
            for (Patient.Age a : Patient.Age.values()) {
                if (a.name().equalsIgnoreCase(ageStr)) age = a;
            }

            if (priority != null && age != null) {
                Patient newPatient = new Patient(name, description, priority, age);
                pQueue.add(newPatient);
            } else System.out.println("ERROR: priority and or age were null!");
        }
        if (e.getSource() == switchViews) {
            inDoctorView = !inDoctorView;

            if (inDoctorView) {
                queueDisplayPane.setVisible(true);
                pQueueDisplayPane.setVisible(false);
                nameInput.setVisible(false);
                illnessDescription.setVisible(false);
                prioritySelection.setVisible(false);
                ageSelection.setVisible(false);
                addPatientBtn.setVisible(false);

                patientDescriptor.setVisible(true);
                dischargeMessage.setVisible(true);
                dischargePatientBtn.setVisible(true);

                if (currentPatient == null) {
                    currentPatient = pQueue.poll();
                    if (currentPatient != null)
                        patientDescriptor.setText(currentPatient.formatForDisplay());
                }

                switchViews.setText("Switch to Nurse View");
            } else {
                queueDisplayPane.setVisible(false);
                pQueueDisplayPane.setVisible(true);
                nameInput.setVisible(true);
                illnessDescription.setVisible(true);
                prioritySelection.setVisible(true);
                ageSelection.setVisible(true);
                addPatientBtn.setVisible(true);

                patientDescriptor.setVisible(false);
                dischargeMessage.setVisible(false);
                dischargePatientBtn.setVisible(false);

                switchViews.setText("Switch to Doctor View");
            }
        } else if (e.getSource() == dischargePatientBtn) {
            if (currentPatient != null) {
                String message = dischargeMessage.getText();
                pQueue.remove(currentPatient);
                currentPatient.discharge(message);
                queue.add(currentPatient);

                patientDescriptor.setText("");
                dischargeMessage.setText("");

                currentPatient = pQueue.poll();
                if (currentPatient != null)
                    patientDescriptor.setText(currentPatient.formatForDisplay());
            }
        } else if (e.getSource() == updatePatientIllnessBtn) {
            String name = nameInput.getText();
            String newIllness = illnessDescription.getText();

            Iterator iter = pQueue.iterator();
            while (iter.hasNext()) {
                Patient temp = (Patient) iter.next();
                if (temp.getName().equalsIgnoreCase(name)) {
                    temp.updateDescription(newIllness);
                }
            }
        } else if (e.getSource() == updatePatientPriorityBtn) {
            String name = nameInput.getText();
            String priorityStr = (String) prioritySelection.getSelectedItem();

            Patient.CasePriority priority = null;
            for (Patient.CasePriority cp : Patient.CasePriority.values()) {
                if (cp.name().equalsIgnoreCase(priorityStr)) priority = cp;
            }

            if (priority != null) {
                Iterator iter = pQueue.iterator();
                while (iter.hasNext()) {
                    Patient temp = (Patient) iter.next();
                    if (temp.getName().equalsIgnoreCase(name)) {
                        temp.updatePriority(priority);
                    }
                }
            }
        }

        pQueueDisplay.setListData(formatPQueueAsArray());
        queueDisplay.setListData(formatQueueAsArray());

        repaint();
    }
}
