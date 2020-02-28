import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main extends JPanel implements ActionListener {
    private JButton jogLeft;
    private JButton jogRight;
    private JButton deletePhoto;
    private JButton addLandmark;
    private JTextField countryCode;
    private JTextField landmarkName;
    private JTextField imageURL;

    private JList<String> countryList;
    private JScrollPane countryPane;

    private String selectedCountryCode;
    private int imageIndex;

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

    private void writeMapToFile() {
        try {
            FileOutputStream fos = new FileOutputStream("mapSave.jobj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
        } catch (IOException err) {
            System.out.println(err.toString());
        }
    }

    public Main() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));

        map = new HashMap<Country, ImageContainer>();
        countries = new HashMap<Country, String>();
        selectedCountryCode = null;
        imageIndex = -1;

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

//        System.out.println(countries);

        if ((new File("mapSave.jobj")).exists()) {
            try {
                FileInputStream fin = new FileInputStream("mapSave.jobj");
                ObjectInputStream ois = new ObjectInputStream(fin);
                map = (HashMap<Country, ImageContainer>) ois.readObject();
                ois.close();
            } catch (IOException | ClassNotFoundException err) {
                System.out.println(err.toString());
            }

            // Reload images...
            DLList<Country> keys = map.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                DLList<ImageContainer> containers = map.getValue(keys.get(i));
                for (int j = 0; j < containers.size(); j++) {
                    containers.get(j).loadImage();
                }
            }
        } else {
            try {
                map.put(new Country("us"), new ImageContainer("aa", new URL("https://www.bing.com/th/id/OIP.YsUb9KOPmlq9XAvhQWbsXgHaE8?pid=Api&rs=1")));
                map.put(new Country("us"), new ImageContainer("bb", new URL("http://i.huffpost.com/gen/2686060/images/o-FLORIDA-facebook.jpg")));
                map.put(new Country("us"), new ImageContainer("cc", new URL("https://images.wisegeek.com/jetty-in-the-tropics.jpg")));
                map.put(new Country("es"), new ImageContainer("dd", new URL("http://www.travelspro.com/images/Packages/LAizawl-Homeof-Highlanders.jpg")));
                map.put(new Country("es"), new ImageContainer("ee", new URL("https://www.meteo.be/meteo/download/fr/196752/image/gertgoesaert_29122006_glenelg_zuid-australi__2.jpg")));
                map.put(new Country("es"), new ImageContainer("ff", new URL("https://www.bing.com/th?id=OIP.sUn9D9FqJwrUVbdFPIsZfgHaE_&pid=Api&rs=1")));
                map.put(new Country("gb"), new ImageContainer("gg", new URL("https://upload.wikimedia.org/wikipedia/commons/6/6b/V%C3%A1rosi_Tan%C3%A1csh%C3%A1z_%282253._sz%C3%A1m%C3%BA_m%C5%B1eml%C3%A9k%29_4.jpg")));
                map.put(new Country("gb"), new ImageContainer("hh", new URL("http://www.derbytourism.com.au/images/side-images/gibb-river-road/50A.jpg")));
                map.put(new Country("gb"), new ImageContainer("ii", new URL("http://darkhorseobservatory.org/images/products/146_01_full.jpg")));
            } catch (MalformedURLException err) {
                System.out.println(err);
            }

            writeMapToFile();
        }

//        System.out.println(map);

        jogLeft = new JButton("<");
        jogLeft.setBounds(25 + 200 + 15, getPreferredSize().height - 75 - 30, 100, 30);
        jogLeft.addActionListener(this);
        jogLeft.setEnabled(false);
        jogLeft.setVisible(false);
        add(jogLeft);

        jogRight = new JButton(">");
        jogRight.setBounds(25 + 200 + 15 + 120, getPreferredSize().height - 75 - 30, 100, 30);
        jogRight.addActionListener(this);
        jogRight.setVisible(false);
        add(jogRight);

        deletePhoto = new JButton("Delete");
        deletePhoto.setBounds(25 + 200 + 15 + 120 * 2, getPreferredSize().height - 75 - 30, 100, 30);
        deletePhoto.addActionListener(this);
        deletePhoto.setVisible(false);
        add(deletePhoto);

        countryList = new JList<>();
        countryList.setListData(recreateCountriesList());
        countryPane = new JScrollPane(countryList);
        countryPane.setBounds(25, 25, 200, getPreferredSize().height - 50);
        countryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = countryList.getSelectedIndex();
                String queryListItem = recreateCountriesList()[selectedIndex > -1 ? selectedIndex : 0];
                String queryCountryCode = queryListItem.substring(0, queryListItem.indexOf(" "));
//                System.out.println("\"" + queryCountryCode + "\"");
                selectedCountryCode = queryCountryCode;

                jogLeft.setVisible(true);
                jogLeft.setEnabled(false);
                jogRight.setVisible(true);
                jogRight.setEnabled(true);
                deletePhoto.setVisible(true);
                deletePhoto.setEnabled(true);
                imageIndex = 0;
                repaint();
            }
        });
        add(countryPane);

        countryCode = new JTextField();
        countryCode.setBounds(25 + 200 + 15, getPreferredSize().height - 50, 120, 30);
        add(countryCode);

        landmarkName = new JTextField();
        landmarkName.setBounds(25 + 200 + 15 + 130, getPreferredSize().height - 50, 120, 30);
        add(landmarkName);

        imageURL = new JTextField();
        imageURL.setBounds(25 + 200 + 15 + 130 * 2, getPreferredSize().height - 50, 120, 30);
        add(imageURL);

        addLandmark = new JButton("Add");
        addLandmark.setBounds(25 + 200 + 15 + 130 * 3, getPreferredSize().height - 50, 120, 30);
        addLandmark.addActionListener(this);
        add(addLandmark);
    }

    private String[] recreateCountriesList() {
        DLList<Country> list = new DLList<>();
        DLList<Country> keys = map.getKeys();
//        System.out.println("keys");
//        System.out.println(keys);
        for (int i = 0; i < map.size(); i++) {
            if (map.getValue(keys.get(i)).size() > 0) list.add(keys.get(i));
        }
//        System.out.println("filtered keys");
//        System.out.println(list);

        String[] out = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            out[i] = list.get(i).toString() + " - " + countries.getValue(list.get(i)) + " : " + map.getValue(list.get(i)).size();

//        System.out.println("array");
//        for(String item : out) System.out.println(" - " + item);

        return out;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int textOffset = -7;

        g.setColor(Color.BLACK);
        g.drawString("Countries List: ", 25, 25 + textOffset);

        g.drawString("Country Abbrev:", 25 + 200 + 15, getPreferredSize().height - 50 + textOffset);
        g.drawString("Landmark Name:", 25 + 200 + 15 + 130, getPreferredSize().height - 50 + textOffset);
        g.drawString("Image URL:", 25 + 200 + 15 + 130 * 2, getPreferredSize().height - 50 + textOffset);

        if (selectedCountryCode != null) {
            drawStringArr(g, new String[]{
                            "Country: " + selectedCountryCode + " - "
                                    + countries.getValue(new Country(selectedCountryCode))
                                    + "        Landmark name: " +
                                    map.getValue(new Country(selectedCountryCode)).get(imageIndex).getName()},
                    new Point(25 + 200 + 15, 25 + textOffset));
            if (imageIndex == -1) imageIndex = 0;
            ImageContainer container = map.getValue(new Country(selectedCountryCode)).get(imageIndex);
            if (container != null) {
                BufferedImage image = container.getImg();
                int height = 350;
                if (image != null)
                    g.drawImage(image, 25 + 200 + 15, 30, height * image.getWidth() / image.getHeight(), height, this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jogLeft) {
            if (imageIndex > 0) imageIndex--;

            if (imageIndex == 0) jogLeft.setEnabled(false);
            jogRight.setEnabled(true);
        } else if (e.getSource() == jogRight) {
            if (imageIndex < map.getValue(new Country(selectedCountryCode)).size()) imageIndex++;

            if (imageIndex == map.getValue(new Country(selectedCountryCode)).size() - 1)
                jogRight.setEnabled(false);
            jogLeft.setEnabled(true);
        } else if (e.getSource() == deletePhoto) {
//            System.out.println("imageIndex: " + imageIndex + ", size: " + map.getValue(new Country(selectedCountryCode)).size());
            boolean lastImageSelected = imageIndex == map.getValue(new Country(selectedCountryCode)).size() - 1;

            map.getValue(new Country(selectedCountryCode)).remove(imageIndex);
            if (lastImageSelected) imageIndex--;

            if (map.getValue(new Country(selectedCountryCode)).size() == 0)
                deletePhoto.setEnabled(false);

            countryList.setListData(recreateCountriesList());
            writeMapToFile();
        } else if (e.getSource() == addLandmark) {
            try {
                String countryCodeInput = countryCode.getText().toLowerCase();
                if (countryCodeInput.length() != 2) {
                    JOptionPane.showMessageDialog(this, "Error: Country code must be two characters!");
                    return;
                } else if (!countries.getKeys().contains(new Country(countryCodeInput))) {
                    JOptionPane.showMessageDialog(this, "Error: Country code not recognized!");
                    return;
                }
                String landmarkNameInput = landmarkName.getText();
                URL url = new URL(imageURL.getText());

                map.put(new Country(countryCodeInput), new ImageContainer(landmarkNameInput, url));
                countryList.setListData(recreateCountriesList());
                writeMapToFile();

                countryCode.setText("");
                landmarkName.setText("");
                imageURL.setText("");
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(this, "Error: URL input must be a valid URL!");
            }
        }

        repaint();
    }

    public static void main(String[] args) {
        Main game = new Main();
        JFrame frame = new JFrame("Photo Album");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
