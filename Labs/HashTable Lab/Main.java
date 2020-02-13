import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements ActionListener {
    private JButton toConsumerView;
    private JButton toDealerView;
    private JButton purchaseOrRemoveCar;
    private JButton addCar;
    private JButton updateCar;
    private JTextField newCarModel;
    private JTextField newCarPrice;
    private JTextField newCarYear;

    private HashTable<Car> db;

    private JList<String> makeList;
    private JScrollPane makePane;

    private JList<String> carList;
    private JScrollPane carPane;

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

        toConsumerView = new JButton("Consumer");
        toConsumerView.setBounds(25 + 200 + 15, 25, 120, 30);
        toConsumerView.addActionListener(this);
        toConsumerView.setEnabled(false);
        add(toConsumerView);

        toDealerView = new JButton("Dealer");
        toDealerView.setBounds(25 + 200 + 15, 25 + 40, 120, 30);
        toDealerView.addActionListener(this);
        add(toDealerView);

        makeList = new JList<>();
        makePane = new JScrollPane(makeList);
        makePane.setBounds(25, 25, 200, getPreferredSize().height - 50);
        makeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = makeList.getSelectedIndex();
                String queryMake = recreateMakesList()[selectedIndex > -1 ? selectedIndex : 0];
//                System.out.println("queryMake: " + queryMake);
                DLList<Car> carsOfMake = db.getByHashCode((new Car(queryMake, "", 0, 0)).hashCode());
//                System.out.println("carsOfMake: " + carsOfMake.toString());
                String[] cars = new String[carsOfMake.size()];
                for (int i = 0; i < cars.length; i++) {
                    cars[i] = carsOfMake.get(i).toString();
                }

                carList.setListData(cars);
            }
        });
        add(makePane);

        carList = new JList<>();
        carPane = new JScrollPane(carList);
        carPane.setBounds(getPreferredSize().width - 400 - 25, 25, 400, getPreferredSize().height - 50);
        add(carPane);

        purchaseOrRemoveCar = new JButton("Purchase");
        purchaseOrRemoveCar.setBounds(25 + 200 + 15, 25 + 40 * 3, 120, 30);
        purchaseOrRemoveCar.addActionListener(this);
        add(purchaseOrRemoveCar);

        addCar = new JButton("Add");
        addCar.setBounds(25 + 200 + 15, 25 + 40 * 4, 120, 30);
        addCar.addActionListener(this);
        addCar.setVisible(false);
        add(addCar);

        updateCar = new JButton("Update");
        updateCar.setBounds(25 + 200 + 15, 25 + 40 * 5, 120, 30);
        updateCar.addActionListener(this);
        updateCar.setVisible(false);
        add(updateCar);

        newCarModel = new JTextField();
        newCarModel.setBounds(25 + 200 + 15, 25 + 40 * 5 + 20 + 50, 120, 30);
        newCarModel.setVisible(false);
        add(newCarModel);

        newCarPrice = new JTextField();
        newCarPrice.setBounds(25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 2, 120, 30);
        newCarPrice.setVisible(false);
        add(newCarPrice);

        newCarYear = new JTextField();
        newCarYear.setBounds(25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 3, 120, 30);
        newCarYear.setVisible(false);
        add(newCarYear);


        db = new HashTable<>();
        db.add(new Car("Toyota", "Camry", 7000, 2005));
        db.add(new Car("Toyota", "Prius", 13000, 2017));
        db.add(new Car("Toyota", "Sienna", 9000, 2010));
        db.add(new Car("Tesla", "Model S", 20000, 2017));
        db.add(new Car("Honda", "Accord", 4000, 2004));
        db.add(new Car("Honda", "Civic", 8000, 2009));
        db.add(new Car("Honda", "Pilot", 10000, 2013));

//        System.out.println(db);

        makeList.setListData(recreateMakesList());
    }

    private String[] recreateMakesList() {
        String[] makes = new String[db.size()];
        for (int i = 0; i < makes.length; i++) {
            DLList<Car> temp = db.get(i);
//            System.out.println(temp);
            makes[i] = db.get(i).get(0).getMake();
        }

        return makes;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int textOffset = -7;

        g.setColor(Color.BLACK);
        g.drawString("Makes list: ", 25, 25 + textOffset);
        g.drawString("Cars list: ", getPreferredSize().width - 400 - 25, 25 + textOffset);
        g.drawString("Views Switcher: ", 25 + 200 + 15, 25 + textOffset);

        if (newCarModel.isVisible()) {
            g.drawString("Model Name:", 25 + 200 + 15, 25 + 40 * 5 + 20 + 50 + textOffset);
            g.drawString("Price:", 25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 2 + textOffset);
            g.drawString("Year:", 25 + 200 + 15, 25 + 40 * 5 + 20 + 50 * 3 + textOffset);
        }
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
            if (makeList.getSelectedIndex() > -1 && carList.getSelectedIndex() > -1) {
                db.get(makeList.getSelectedIndex()).remove(carList.getSelectedIndex());
                if (db.get(makeList.getSelectedIndex()).size() == 0)
                    db.remove(makeList.getSelectedIndex());
            }
        } else if (e.getSource() == addCar || e.getSource() == updateCar) {
            String model = newCarModel.getText();
            double price;
            int year;
            try {
                price = Double.parseDouble(newCarPrice.getText());
                year = Integer.parseInt(newCarYear.getText());
                Car newCar = new Car(makeList.getSelectedValue(), model, price, year);

                newCarModel.setText("");
                newCarPrice.setText("");
                newCarYear.setText("");

                if (e.getSource() == addCar) db.add(newCar);
                else if (e.getSource() == updateCar) {
                    String[] parts = carList.getSelectedValue().split(" ");

                    DLList<Car> cars = db.get(makeList.getSelectedIndex());
                    int carIndex = -1;
                    for (int i = 0; i < cars.size(); i++) {
                        if (cars.get(i).getModel().equalsIgnoreCase(parts[1])) carIndex = i;
                    }
                    cars.set(carIndex, newCar);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "ERROR: price and year must be numbers!");
            }
        }

        String[] makesList = recreateMakesList();
        String queryMake = makesList[makeList.getSelectedIndex() == -1 ? 0 : makeList.getSelectedIndex()];
//        System.out.println("queryMake: " + queryMake);
        DLList<Car> carsOfMake = db.getByHashCode((new Car(queryMake, "", 0, 0)).hashCode());
//        System.out.println("carsOfMake: " + carsOfMake.toString());
        String[] cars = new String[carsOfMake.size()];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = carsOfMake.get(i).toString();
        }

        int selectedIndex = makeList.getSelectedIndex();
        makeList.setListData(makesList);
        makeList.setSelectedIndex(selectedIndex);
        if (makeList.getSelectedIndex() != -1) carList.setListData(cars);

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
