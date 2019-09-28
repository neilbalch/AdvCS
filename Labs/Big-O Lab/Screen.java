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

    public String toString() {
        return last + ", " + first + ". Age: " + age;
    }
}

class StudentDatabase {
    private String inputFilePath = "names.txt";
    private ArrayList<Student> students;
    private int iterationsCounter = 0;

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
                String last = line.substring(line.indexOf(" ") + 1);
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
            temp[i] = (i + 1) + " — " + students.get(i).last + ", " + students.get(i).first + " - " + students.get(i).age;
        }

        return temp;
    }

    private int binarySearch(ArrayList<Student> data, String query, int start, int end) {
        iterationsCounter++;
        if (start > end) return -1;
        int middle = (end + start) / 2;

        if (query.toLowerCase().equals(data.get(middle).last.toLowerCase())) {
//            System.out.println(query + "==" + data.get(middle).last);
            return middle;
        } else if (query.compareToIgnoreCase(data.get(middle).last) < 0) {
//            System.out.println(query + "<" + data.get(middle).last);
            return binarySearch(data, query, start, middle - 1);
        } else if (query.compareToIgnoreCase(data.get(middle).last) > 0) {
//            System.out.println(query + ">" + data.get(middle).last);
            return binarySearch(data, query, middle + 1, end);
        } else return -1;
    }

    public Pair<Student, Integer> searchByLastNameBinary(String query) {
        // Binary Search
        // Make sure the list is sorted by last name; other operations may have altered it.
        ArrayList<Student> studentsCopy = (ArrayList<Student>) students.clone();

        Collections.sort(studentsCopy, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1.last.compareToIgnoreCase(o2.last) > 0) return 1;
                else if (o1.last.compareToIgnoreCase(o2.last) < 0) return -1;
                else return 0;
            }
        });

//        System.out.println("Sorted...:");
//        for(Student line : studentsCopy) System.out.println(line);
//        System.out.println();

        iterationsCounter = 0;
        int index = binarySearch(studentsCopy, query, 0, studentsCopy.size() - 1);
        if (index == -1) return new Pair<Student, Integer>(null, iterationsCounter);
        else return new Pair<Student, Integer>(studentsCopy.get(index), iterationsCounter);
    }

    public Pair<Student, Integer> searchByLastNameSequential(String query) {
        iterationsCounter = 0;
        for (int i = 0; i < students.size(); i++) {
            iterationsCounter++;
            if (students.get(i).last.toLowerCase().equals(query.toLowerCase()))
                return new Pair<Student, Integer>(students.get(i), iterationsCounter);
        }

        return new Pair<Student, Integer>(null, iterationsCounter);
    }

    public void scramble() {
        for (int i = 0; i < students.size(); i++) {
            int i2 = (int) (Math.random() * students.size());

            Student temp = students.get(i);
            students.set(i, students.get(i2));
            students.set(i2, temp);
        }
    }

    public int bubbleSortByLastName() {
        iterationsCounter = 0;
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = i + 1; j < students.size(); j++) {
                iterationsCounter++;
                if (students.get(i).last.compareToIgnoreCase(students.get(j).last) > 0) {
                    Student temp = students.get(i);
                    students.set(i, students.get(j));
                    students.set(j, temp);
                }
            }
        }

        return iterationsCounter;
    }

    public int mergeSortByLastName() {
        iterationsCounter = 0;
        students = new ArrayList<Student>(Arrays.asList(mergeSort(students.toArray(new Student[1]), students.size())));

        return iterationsCounter;
    }

    private Student[] mergeSort(Student[] a, int n) {
        iterationsCounter++;
        if (n < 2) {
            return a;
        }
        int mid = n / 2;
        Student[] l = new Student[mid];
        Student[] r = new Student[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
        return a;
    }

    private static void merge(Student[] a, Student[] l, Student[] r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i].last.compareToIgnoreCase(r[j].last) < 0) {
                a[k++] = l[i++];
            } else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }
}

public class Screen extends JPanel implements ActionListener {
    private int itemsXPos = 455;
    private int itemsWidth = 250;
    private StudentDatabase students;

    private JList<String> studentsDisplay;
    private JScrollPane studentsScrollPane;
    private JButton searchByLastNameBinary;
    private JButton searchByLastNameSeq;
    private JTextField lastName;

    private JButton bubbleSort;
    private JButton mergeSort;
    private JButton scrambleList;

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

        searchByLastNameBinary = new JButton("Search By Last Name (Binary)");
        searchByLastNameBinary.setBounds(itemsXPos, 50, 215, 30);
        searchByLastNameBinary.addActionListener(this);
        add(searchByLastNameBinary);

        searchByLastNameSeq = new JButton("Search By Last Name (Seq)");
        searchByLastNameSeq.setBounds(itemsXPos, 90, 215, 30);
        searchByLastNameSeq.addActionListener(this);
        add(searchByLastNameSeq);

        lastName = new JTextField();
        lastName.setBounds(itemsXPos + 220, 70, 125, 30);
        add(lastName);

        bubbleSort = new JButton("Bubble Sort List");
        bubbleSort.setBounds(itemsXPos, 150, 215, 30);
        bubbleSort.addActionListener(this);
        add(bubbleSort);

        mergeSort = new JButton("Merge Sort List");
        mergeSort.setBounds(itemsXPos, 190, 215, 30);
        mergeSort.addActionListener(this);
        add(mergeSort);

        scrambleList = new JButton("Scramble List");
        scrambleList.setBounds(itemsXPos, 230, 215, 30);
        scrambleList.addActionListener(this);
        add(scrambleList);
    }

    // Sets the size of the panel
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchByLastNameBinary) {
            Pair<Student, Integer> result = students.searchByLastNameBinary(lastName.getText());
            if (result.t == null)
                JOptionPane.showMessageDialog(null, "STUDENT NOT FOUND! Passes Required: " + result.u);
            else
                JOptionPane.showMessageDialog(null, "Result: " + result.t + " Passes Required: " + result.u);
        } else if (e.getSource() == searchByLastNameSeq) {
            Pair<Student, Integer> result = students.searchByLastNameSequential(lastName.getText());
            if (result.t == null)
                JOptionPane.showMessageDialog(null, "STUDENT NOT FOUND! Passes Required: " + result.u);
            else
                JOptionPane.showMessageDialog(null, "Result: " + result.t + " Passes Required: " + result.u);
        } else if (e.getSource() == bubbleSort) {
            int passes = students.bubbleSortByLastName();
            JOptionPane.showMessageDialog(null, "Sorted in " + passes + " passes.");
        } else if (e.getSource() == mergeSort) {
            int passes = students.mergeSortByLastName();
            JOptionPane.showMessageDialog(null, "Sorted in " + passes + " passes.");
        } else if (e.getSource() == scrambleList) {
            students.scramble();
        }

        studentsDisplay.setListData(students.formatStudentsListAsArray());
        repaint();
    }
}
