import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class Pair<T, U> {
    public T t;
    public U u;

    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }
}

class Student {
    public String first;
    public String last;
    public int age;

    public Student(String first, String last, int age) {
        this.first = first;
        this.last = last;
        this.age = age;
    }
}

class StudentDatabase {
    private String inputFilePath = "names.txt";
    private ArrayList<Student> students;

    public StudentDatabase() {
        students = new ArrayList<>();
        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(inputFilePath);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                String last = line.substring(line.indexOf(" "));
                String first = line.substring(0, line.indexOf(" "));

                ListIterator<Student> iter = students.listIterator();
                boolean inserted = false;
                int age = (int) (Math.random() * 5 + 14);
                while (iter.hasNext() && !inserted) {
                    Student temp = iter.next();
                    if ((temp.last + temp.first).compareToIgnoreCase(last + first) > 0) {
                        iter.previous();
                        iter.add(new Student(first, last, age));
                        inserted = true;
                        iter.next();
                    }
                }
                if (!inserted) iter.add(new Student(first, last, age));
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
    }

    public String[] formatStudentsListAsArray() {
        String[] temp = new String[students.size()];

        for (int i = 0; i < students.size(); i++) {
            temp[i] = students.get(i).last + ", " + students.get(i).first + " - " + students.get(i).age;
        }

        return temp;
    }

}

public class Screen extends JPanel implements ActionListener {
    private int itemsXPos = 100;
    private int itemsWidth = 250;
    private int items2ndColXPos = itemsXPos + itemsWidth + 50;
    private StudentDatabase students;

    private JList<String> studentsDisplay;
    private JScrollPane studentsScrollPane;


    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        students = new StudentDatabase();

        studentsDisplay = new JList<>();
        add(studentsDisplay);
        studentsScrollPane = new JScrollPane(studentsDisplay);
        studentsScrollPane.setBounds(50, 50, 400, getPreferredSize().height - 100);
        add(studentsScrollPane);


        studentsDisplay.setListData(students.formatStudentsListAsArray());
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
