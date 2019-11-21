import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class AnimationTest extends JPanel {
    private boolean animating;
    private int animationTime;
    private int winner = 0;

    public AnimationTest() {
        this.setPreferredSize(new Dimension(800, 600));

        animating = false;
        animationTime = 0;
        Thread animator = new Thread(() -> {
            while (true) {
                // count: 0-100: in-animation, 100-300: static, 300-400: out-animation
                animating = true;
                System.out.println("Winner: " + winner);
                while (animationTime < 400) {
                    try {
                        repaint();
                        animationTime++;
                        Thread.sleep(3);
                    } catch (InterruptedException err) {
                        System.out.println(err.toString());
                    }
                }
                animating = false;

                animationTime = 0;

                winner++;
                if (winner == 3) winner = 0;
            }
        });


        animator.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        System.out.println(animating + ", " + animationTime);

        if (animating) {
            if (animationTime < 100) {
                if (winner == 0) {
                    int x_left = -200 + animationTime * 6; // Right edge
                    int x_right = 1000 + animationTime * -6; // Left edge
                    int y = 300; // Middle

                    g.setColor(Color.BLACK);
                    g.fillRect(x_left - 150, y - 15, 150, 30);
                    g.fillRect(x_right, y - 15, 150, 30);
                }
                if (winner == 1) {
                    int x_left = 100 - 300 + animationTime * 6; // *6 == translation of +600
                    int x_right = 700 + 300 + animationTime * -6; // *-6 == translation of -600
                    int y = 600 + 300 + animationTime * -6; // *-6 == translation of -600
                    int width = 400;
                    int height = 30;
                    double theta = Math.toRadians(45);

                    Rectangle2D rect = new Rectangle2D.Double(-width / 2., -height / 2., width, height);
                    AffineTransform transform_left = new AffineTransform();
                    transform_left.translate(x_left, y);
                    transform_left.rotate(-theta);
                    Shape leftRect = transform_left.createTransformedShape(rect);

                    AffineTransform transform_right = new AffineTransform();
                    transform_right.translate(x_right, y);
                    transform_right.rotate(theta);
                    Shape rightRect = transform_right.createTransformedShape(rect);

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.fill(leftRect);
                    g2d.fill(rightRect);
                } else if (winner == 2) {
                    int x_left = 275 - 600 + animationTime * 6;
                    int x_right = 275 + 600 + animationTime * -6;
                    int y = 375;

                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Calibri", Font.PLAIN, 350));
                    g.drawString("O", x_left, y);
                    g.drawString("O", x_right, y);
                }
            } else if (animationTime < 300) {
                if (winner == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(400 - 150, 300 - 15, 300, 30);
                }
                if (winner == 1) {
                    int x_left = 400;
                    int x_right = x_left;
                    int y = 300;
                    int width = 400;
                    int height = 30;
                    double theta = Math.toRadians(45);

                    Rectangle2D rect = new Rectangle2D.Double(-width / 2., -height / 2., width, height);
                    AffineTransform transform_left = new AffineTransform();
                    transform_left.translate(x_left, y);
                    transform_left.rotate(-theta);
                    Shape leftRect = transform_left.createTransformedShape(rect);

                    AffineTransform transform_right = new AffineTransform();
                    transform_right.translate(x_right, y);
                    transform_right.rotate(theta);
                    Shape rightRect = transform_right.createTransformedShape(rect);

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.fill(leftRect);
                    g2d.fill(rightRect);
                } else if (winner == 2) {
                    int x_left = 275;
                    int x_right = x_left;
                    int y = 375;

                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Calibri", Font.PLAIN, 350));
                    g.drawString("O", x_left, y);
                    g.drawString("O", x_right, y);
                }
            } else { // count < 400
                if (winner == 0) {
                    int x_left = 400 + (animationTime - 300) * -6; // Right edge
                    int x_right = 400 + (animationTime - 300) * +6; // Left edge
                    int y = 300; // Middle

                    g.setColor(Color.BLACK);
                    g.fillRect(x_left - 150, y - 15, 150, 30);
                    g.fillRect(x_right, y - 15, 150, 30);
                }
                if (winner == 1) {
                    int x_left = 400 + (animationTime - 300) * 6; // *6 == translation of +600
                    int x_right = 400 + (animationTime - 300) * -6; // *-6 == translation of -600
                    int y = 300 + (animationTime - 300) * -6; // *-6 == translation of -600
                    int width = 400;
                    int height = 30;
                    double theta = Math.toRadians(45);

                    // create rect centred on the point we want to rotate it about
                    Rectangle2D rect = new Rectangle2D.Double(-width / 2., -height / 2., width, height);
                    AffineTransform transform_left = new AffineTransform();
                    transform_left.translate(x_left, y);
                    transform_left.rotate(-theta);
                    Shape leftRect = transform_left.createTransformedShape(rect);

                    AffineTransform transform_right = new AffineTransform();
                    transform_right.translate(x_right, y);
                    transform_right.rotate(theta);
                    Shape rightRect = transform_right.createTransformedShape(rect);

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.fill(leftRect);
                    g2d.fill(rightRect);
                } else if (winner == 2) {
                    int x_left = 275 + (animationTime - 300) * -6;
                    int x_right = 275 + (animationTime - 300) * 6;
                    int y = 375;

                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Calibri", Font.PLAIN, 350));
                    g.drawString("O", x_left, y);
                    g.drawString("O", x_right, y);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("");
        frame.add(new AnimationTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}