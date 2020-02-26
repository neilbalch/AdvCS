import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main extends JPanel implements ActionListener {
    private JButton toConsumerView;
    private JButton toDealerView;
    private JButton purchaseOrRemoveCar;
    private JButton addCar;
    private JButton updateCar;
    private JTextField newCarModel;
    private JTextField newCarPrice;
    private JTextField newCarYear;

    private JList<String> makeList;
    private JScrollPane makePane;

    private JList<String> carList;
    private JScrollPane carPane;

    private HashMap<Country, ImageContainer> map;
    private HashMap<Country, String> countries;

    private boolean coordsWithin(Point location, Point topLeft, Point bottomRight) {
        if (location.x > topLeft.x && location.x < bottomRight.x &&
                location.y > topLeft.y && location.y < bottomRight.y) return true;
        else return false;
    }

    private void drawStringArr(Graphics g, String[] text, Point topLeft) {
        int x = topLeft.x;
        int y = topLeft.y + 7;
        for (String line : text) {
            g.drawString(line, x, y);
            y += 15;
        }
    }

    public Main() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));

        map = new HashMap<Country, ImageContainer>();
        countries = new HashMap<Country, String>();

        try {
            Scanner scan = new Scanner(new File("countries.txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
//                System.out.println(line);

                map.put(new Country(line.substring(0, line.indexOf(","))), null);
                countries.put(new Country(line.substring(0, line.indexOf(","))), line.substring(line.indexOf(",") + 1));
            }
        } catch (FileNotFoundException err) {
            System.out.println(err);
        }
        System.out.println(countries);

        try {
            map.put(new Country("us"), new ImageContainer("aa", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
            map.put(new Country("us"), new ImageContainer("bb", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
            map.put(new Country("us"), new ImageContainer("cc", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
            map.put(new Country("us"), new ImageContainer("dd", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
            map.put(new Country("us"), new ImageContainer("ee", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
            map.put(new Country("us"), new ImageContainer("ff", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
        } catch (MalformedURLException err) {
            System.out.println(err);
        }

        System.out.println(map);

//        toConsumerView = new JButton("Consumer");
//        toConsumerView.setBounds(25 + 200 + 15, 25, 120, 30);
//        toConsumerView.addActionListener(this);
//        toConsumerView.setEnabled(false);
//        add(toConsumerView);
//
//        toDealerView = new JButton("Dealer");
//        toDealerView.setBounds(25 + 200 + 15, 25 + 40, 120, 30);
//        toDealerView.addActionListener(this);
//        add(toDealerView);
//
//        makeList = new JList<>();
//        makePane = new JScrollPane(makeList);
//        makePane.setBounds(25, 25, 200, getPreferredSize().height - 50);
//        makeList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                int selectedIndex = makeList.getSelectedIndex();
//                String queryMake = recreateMakesList()[selectedIndex > -1 ? selectedIndex : 0];
////                System.out.println("queryMake: " + queryMake);
//                DLList<Car> carsOfMake = db.getByHashCode((new Car(queryMake, "", 0, 0)).hashCode());
////                System.out.println("carsOfMake: " + carsOfMake.toString());
//                String[] cars = new String[carsOfMake.size()];
//                for (int i = 0; i < cars.length; i++) {
//                    cars[i] = carsOfMake.get(i).toString();
//                }
//
//                carList.setListData(cars);
//            }
//        });
//        add(makePane);
//
//        carList = new JList<>();
//        carPane = new JScrollPane(carList);
//        carPane.setBounds(getPreferredSize().width - 400 - 25, 25, 400, getPreferredSize().height - 50);
//        add(carPane);
//
//        purchaseOrRemoveCar = new JButton("Purchase");
//        purchaseOrRemoveCar.setBounds(25 + 200 + 15, 25 + 40 * 3, 120, 30);
//        purchaseOrRemoveCar.addActionListener(this);
//        add(purchaseOrRemoveCar);
//
//        addCar = new JButton("Add");
//        addCar.setBounds(25 + 200 + 15, 25 + 40 * 4, 120, 30);
//        addCar.addActionListener(this);
//        addCar.setVisible(false);
//        add(addCar);
//
//        updateCar = new JButton("Update");
//        updateCar.setBounds(25 + 200 + 15, 25 + 40 * 5, 120, 30);
//        updateCar.addActionListener(this);
//        updateCar.setVisible(false);
//        add(updateCar);
//
//        newCarModel = new JTextField();
//        newCarModel.setBounds(25 + 200 + 15, 25 + 40 * 5 + 20 + 50, 120, 30);
//        newCarModel.setVisible(false);
//        add(newCarModel);
//
//        newCarPrice = new JTextField();
//        newCarPrice.setBounds(25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 2, 120, 30);
//        newCarPrice.setVisible(false);
//        add(newCarPrice);
//
//        newCarYear = new JTextField();
//        newCarYear.setBounds(25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 3, 120, 30);
//        newCarYear.setVisible(false);
//        add(newCarYear);


//        makeList.setListData(recreateMakesList());
    }

//    private String[] recreateMakesList() {
//        String[] makes = new String[db.size()];
//        for (int i = 0; i < makes.length; i++) {
//            DLList<Car> temp = db.get(i);
////            System.out.println(temp);
//            makes[i] = db.get(i).get(0).getMake();
//        }
//
//        return makes;
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int textOffset = -7;

        g.setColor(Color.BLACK);
        g.drawString("Makes list: ", 25, 25 + textOffset);
        g.drawString("Cars list: ", getPreferredSize().width - 400 - 25, 25 + textOffset);
        g.drawString("Views Switcher: ", 25 + 200 + 15, 25 + textOffset);

//        if (newCarModel.isVisible()) {
//            g.drawString("Model Name:", 25 + 200 + 15, 25 + 40 * 5 + 20 + 50 + textOffset);
//            g.drawString("Price:", 25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 2 + textOffset);
//            g.drawString("Year:", 25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 3 + textOffset);
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toConsumerView) {
            toConsumerView.setEnabled(false);
            toDealerView.setEnabled(true);

            addCar.setVisible(false);
            updateCar.setVisible(false);
            purchaseOrRemoveCar.setText("Purchase");
            newCarModel.setVisible(false);
            newCarPrice.setVisible(false);
            newCarYear.setVisible(false);
        } else if (e.getSource() == toDealerView) {
            toConsumerView.setEnabled(true);
            toDealerView.setEnabled(false);

            addCar.setVisible(true);
            updateCar.setVisible(true);
            purchaseOrRemoveCar.setText("Remove");
            newCarModel.setVisible(true);
            newCarPrice.setVisible(true);
            newCarYear.setVisible(true);
        } else if (e.getSource() == purchaseOrRemoveCar) {
        } else if (e.getSource() == addCar || e.getSource() == updateCar) {
        }
//
//        String[] makesList = recreateMakesList();
//        String queryMake = makesList[makeList.getSelectedIndex() == -1 ? 0 : makeList.getSelectedIndex()];
////        System.out.println("queryMake: " + queryMake);
//        DLList<Car> carsOfMake = db.getByHashCode((new Car(queryMake, "", 0, 0)).hashCode());
////        System.out.println("carsOfMake: " + carsOfMake.toString());
//        String[] cars = new String[carsOfMake.size()];
//        for (int i = 0; i < cars.length; i++) {
//            cars[i] = carsOfMake.get(i).toString();
//        }
//
//        int selectedIndex = makeList.getSelectedIndex();
//        makeList.setListData(makesList);
//        makeList.setSelectedIndex(selectedIndex);
//        if (makeList.getSelectedIndex() != -1) carList.setListData(cars);

        repaint();
    }

    public static void main(String[] args) {
        Main game = new Main();
        JFrame frame = new JFrame("Dealership Assistant");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
