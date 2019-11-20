import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Main extends JPanel {
    private boolean animating = false;
    private int animationTime = 0;
    private static final int winner = 1;

    public Main() {
        this.setPreferredSize(new Dimension(800, 600));

        animating = false;
        animationTime = 0;
        Thread animator = new Thread(() -> {
            // count: 0-100: in-animation, 100-300: static, 300-400: out-animation
            animating = true;
            while (animationTime < 400) {
                try {
                    repaint();
                    animationTime++;
                    Thread.sleep(5);
                } catch (InterruptedException err) {
                    System.out.println(err.toString());
                }
            }
            animating = false;
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException err) {
            System.out.println(err.toString());
        }
        animator.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(animating + ", " + animationTime);

        if (animating) {
            if (animationTime < 100) {
                if (winner == 1) {
                    int x = 100 - 300 + animationTime * 6; // *6 == translation of +600
                    int y = 600 + 300 + animationTime * -6; // *-6 == translation of -600
                    int width = 400;
                    int height = 30;
                    double theta = Math.toRadians(45);

                    // create rect centred on the point we want to rotate it about
                    Rectangle2D rect = new Rectangle2D.Double(-width / 2., -height / 2., width, height);
                    AffineTransform transform = new AffineTransform();
                    transform.translate(x, y);
                    transform.rotate(-theta);
                    Shape rotatedRect = transform.createTransformedShape(rect);

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.fill(rotatedRect);
                }
            } else if (animationTime < 300) {
                if (winner == 1) {
                    int x = 400;
                    int y = 300;
                    int width = 400;
                    int height = 30;
                    double theta = Math.toRadians(45);

                    // create rect centred on the point we want to rotate it about
                    Rectangle2D rect = new Rectangle2D.Double(-width / 2., -height / 2., width, height);
                    AffineTransform transform = new AffineTransform();
                    transform.translate(x, y);
                    transform.rotate(-theta);
                    Shape rotatedRect = transform.createTransformedShape(rect);

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.fill(rotatedRect);
                }

            } else { // count < 400
                if (winner == 1) {
                    int x = 400 + (animationTime - 300) * 6; // *6 == translation of +600
                    int y = 300 + (animationTime - 300) * -6; // *-6 == translation of -600
                    int width = 400;
                    int height = 30;
                    double theta = Math.toRadians(45);

                    // create rect centred on the point we want to rotate it about
                    Rectangle2D rect = new Rectangle2D.Double(-width / 2., -height / 2., width, height);
                    AffineTransform transform = new AffineTransform();
                    transform.translate(x, y);
                    transform.rotate(-theta);
                    Shape rotatedRect = transform.createTransformedShape(rect);

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.fill(rotatedRect);
                }

            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("");
        frame.add(new Main());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}