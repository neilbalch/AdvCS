import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Runner {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Totally Secure and 100% Awesome Student Database (for schools)");
        Screen sc = new Screen();
        frame.add(sc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
