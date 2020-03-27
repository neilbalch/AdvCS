import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
    public Main() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
    }

    public void paintComponent(Graphics g) {
        drawFractalSegment(g, new Point(400, 600 - 50), 150, Math.toRadians(90));
    }

    public void drawFractalSegment(Graphics g, Point start, int length, double angleFromVertical) {
        if (length < 5) return;

        Dimension lineVector = new Dimension((int) (length * Math.cos(angleFromVertical)), (int) (length * Math.sin(angleFromVertical)));
        g.setColor(Color.BLACK);
        g.drawLine(start.x, start.y, start.x + lineVector.width, start.y - lineVector.height);

        Point newStart = new Point(start.x + lineVector.width, start.y - lineVector.height);
        drawFractalSegment(g, newStart, (length * 3) / 5, angleFromVertical - Math.toRadians(30));
        drawFractalSegment(g, newStart, (length * 3) / 5, angleFromVertical + Math.toRadians(30));
    }

    public static void main(String[] args) {
        Main game = new Main();
        JFrame frame = new JFrame("Fractal Visualization");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}