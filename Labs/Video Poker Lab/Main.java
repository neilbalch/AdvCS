import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Main extends JPanel implements ActionListener {
    private DLList<Card> deck;
    private DLList<Card> hand;

    private int playerPts;
    private boolean[] cardsHeld;

    private JButton play;
    private JButton draw;

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
        deck = new DLList<>();
        hand = new DLList<>();
        playerPts = 50;
        cardsHeld = new boolean[5];

        play = new JButton("Play");
        play.setBounds(50, 100, 100, 30);
        play.addActionListener(this);
        add(play);

        draw = new JButton("Draw");
        draw.setBounds(160, 100, 100, 30);
        draw.addActionListener(this);
        draw.setEnabled(false);
        add(draw);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int card = -1;
                int x_pos = 50;
                for (int i = 0; i < hand.size(); i++) {
                    if (coordsWithin(e.getPoint(),
                            new Point(x_pos, 200),
                            new Point(x_pos + 100, 200 + 150))) {
                        card = i;
                        break;
                    }
                    x_pos += 110;
                }

                if (card != -1) cardsHeld[card] = !cardsHeld[card];

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });

        Arrays.fill(cardsHeld, false);

        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 15; j++) {
                if (i == 0) deck.add(new Card(Card.Suit.CLUB, j));
                else if (i == 1) deck.add(new Card(Card.Suit.DIAMOND, j));
                else if (i == 2) deck.add(new Card(Card.Suit.HEART, j));
                else deck.add(new Card(Card.Suit.SPADE, j));
            }
        }

        // Scramble deck
        for (int i = 0; i < deck.size(); i++) {
            int j = (int) (deck.size() * Math.random());

            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(30, 165, 27));
        g.fillRect(0, 0, getWidth(), getHeight());

        int x_pos = 50;
        for (int i = 0; i < hand.size(); i++) {
            if (cardsHeld[i]) {
                g.setColor(Color.YELLOW);
                g.fillRect(x_pos - 5, 200 - 20, 110, 175);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("Card Held", x_pos + 8, 200 - 5);
            }

            if (hand.get(i) != null)
                hand.get(i).drawCard(g, new Point(x_pos, 200), new Dimension(100, 150));

            x_pos += 110;
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.PLAIN, 16));
        String[] table = {
                "Achievement --> Winnings",
                "Royal Flush --> 250 points",
                "Straight Flush --> 50 points",
                "Four of Kind --> 25 points",
                "Full House -->  9 points",
                "Flush --> 6 points",
                "Straight --> 4 points",
                "3 of a Kind --> 3 points",
                "2 Pairs --> 2 points",
                "Pair of Jacks or Higher --> 1 point"
        };
        drawStringArr(g, table, new Point(50, 375));

        g.setFont(new Font("Calibri", Font.PLAIN, 24));
        g.drawString("Points: " + playerPts, 400, 100 + 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            play.setEnabled(false);
            draw.setEnabled(true);
            playerPts--;

            // Start the game
            for (int i = 0; i < 5; i++) hand.add(deck.remove(0));
        } else if (e.getSource() == draw) {
            for (int i = 0; i < cardsHeld.length; i++) {
                if (!cardsHeld[i]) {
                    if (deck.size() > 0) hand.set(i, deck.remove(0));
                    else hand.remove(i);
                } else {
                    // Reset status
                    cardsHeld[i] = false;
                }
            }

            // TODO: Check for winning cases from above, high-valued conditions first.
            int pointsWon = 0;
            DLList<Card> handSorted = new DLList<>();
            for (int j = 0; j < hand.size(); j++) handSorted.add(hand.get(j));
            handSorted.sort();

            DLList<Card> tmpHand = new DLList<>();
            tmpHand.add(new Card(Card.Suit.SPADE, 2));
            tmpHand.add(new Card(Card.Suit.SPADE, 3));
            tmpHand.add(new Card(Card.Suit.SPADE, 4));
            tmpHand.add(new Card(Card.Suit.SPADE, 5));
            tmpHand.add(new Card(Card.Suit.SPADE, 6));
            tmpHand.add(new Card(Card.Suit.SPADE, 7));
            tmpHand.add(new Card(Card.Suit.SPADE, 8));

            for (int i = 0; i < 4; i++) {
                if (pointsWon != 0) break;

                Card.Suit reqSuit = Card.Suit.CLUB;
                switch (i) {
                    case 0:
                        reqSuit = Card.Suit.CLUB;
                        break;
                    case 1:
                        reqSuit = Card.Suit.DIAMOND;
                        break;
                    case 2:
                        reqSuit = Card.Suit.HEART;
                        break;
                    case 3:
                        reqSuit = Card.Suit.SPADE;
                        break;
                }

                if (hand.contains(new Card(reqSuit, 10)) &&
                        hand.contains(new Card(reqSuit, 11)) &&
                        hand.contains(new Card(reqSuit, 12)) &&
                        hand.contains(new Card(reqSuit, 13)) &&
                        hand.contains(new Card(reqSuit, 14))) { // Royal flush
                    pointsWon += 250;
                } else {
                    int sequenceLength = 0;
                    for (int j = 2; j < 15; j++) {
                        if (hand.contains(new Card(reqSuit, j))) sequenceLength++;
                        else sequenceLength = 0;

                        if (sequenceLength >= 5) pointsWon += 50; // Straight flush
                    }
                }
            }
        }

        repaint();
    }

    public static void main(String[] args) {
        Main game = new Main();
        JFrame frame = new JFrame("Video Poker");

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
