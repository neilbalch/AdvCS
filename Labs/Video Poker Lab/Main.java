import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class Pair<K, V> {
    private K item1;
    private V item2;

    public Pair(K item1, V item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(item1, pair.item1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item1);
    }

    public K getItem1() {
        return item1;
    }

    public V getItem2() {
        return item2;
    }

    public String toString() {
        return item1 + " : " + item2;
    }

    public void setItem1(K item1) {
        this.item1 = item1;
    }

    public void setItem2(V item2) {
        this.item2 = item2;
    }
}

public class Main extends JPanel implements ActionListener {
    private DLList<Card> deck;
    private DLList<Card> hand;

    private int playerPts;
    private int lastPtsWon;
    private boolean[] cardsHeld;

    private JButton play;
    private JButton draw;

    private enum Sounds {
        GAME_LOST("game_lost.wav"),
        GAME_WON("game_won.wav");

        // Nested class for specifying volume
        public static enum Volume {MUTE, LOW, MEDIUM, HIGH}

        public static Sounds.Volume volume = Sounds.Volume.LOW;

        // Each sound effect has its own clip, loaded with its own sound file.
        private Clip clip;

        Sounds(String fileName) {
            try {
                // Use URL (instead of File) to read from disk and JAR.
                URL url = this.getClass().getClassLoader().getResource(fileName);
                // Set up an audio input stream piped from the sound file.
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                // Get a clip resource.
                clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioInputStream);
            } catch (UnsupportedAudioFileException e) {
//                e.printStackTrace();
                System.out.println(e.toString());
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println(e.toString());
            } catch (LineUnavailableException e) {
//                e.printStackTrace();
                System.out.println(e.toString());
            }
        }

        // Play or Re-play the sound effect from the beginning, by rewinding.
        public void play() {
            if (volume != Sounds.Volume.MUTE) {
                if (clip.isRunning())
                    clip.stop();   // Stop the player if it is still running
                clip.setFramePosition(0); // rewind to the beginning
                clip.start();     // Start playing
            }
        }

        // Optional static method to pre-load all the sound files.
        static void init() {
            values(); // calls the constructor for all the elements
        }
    }

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
        lastPtsWon = 0;
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
                            new Point(x_pos + 100, 200 + 150)) && draw.isEnabled()) {
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
        if (lastPtsWon != 0) {
            g.setFont(new Font("Calibri", Font.PLAIN, 14));
            g.drawString("+" + lastPtsWon, 480, 100 + 40);
            lastPtsWon = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            play.setEnabled(false);
            draw.setEnabled(true);
            playerPts--;

            // Start the game
            hand = new DLList<>();

            // Scramble deck
            for (int i = 0; i < deck.size(); i++) {
                int j = (int) (deck.size() * Math.random());

                Card temp = deck.get(i);
                deck.set(i, deck.get(j));
                deck.set(j, temp);
            }

            for (int i = 0; i < 5; i++) hand.add(deck.get(i));
        } else if (e.getSource() == draw) {
            for (int i = 0; i < cardsHeld.length; i++) {
                if (!cardsHeld[i]) {
                    if (deck.size() > 0) hand.set(i, deck.get((int) (deck.size() * Math.random())));
                    else hand.remove(i);
                } else {
                    // Reset status
                    cardsHeld[i] = false;
                }
            }

            DLList<Card> handSorted = new DLList<>();
            for (int j = 0; j < hand.size(); j++) handSorted.add(hand.get(j));
            handSorted.sort();

            boolean fourOfAKind = false;
            for (int i = 2; i < 15; i++) {
                if (hand.contains(new Card(Card.Suit.CLUB, i)) &&
                        hand.contains(new Card(Card.Suit.DIAMOND, i)) &&
                        hand.contains(new Card(Card.Suit.HEART, i)) &&
                        hand.contains(new Card(Card.Suit.SPADE, i))) {
                    fourOfAKind = true;
                    break;
                }
            }

            boolean threeAndTwoOfAKind = true;
            boolean threeOfAKind = false;
            if (hand.size() < 3) {
                threeAndTwoOfAKind = false;
                threeOfAKind = false;
            } else {
                Card type1 = hand.get(0);
                Card type2 = hand.get(1);
                int numType1 = 1;
                int numType2 = 1;
                for (int i = 2; i < hand.size(); i++) {
                    if (hand.get(i).getSuit() != type1.getSuit() && hand.get(i).getSuit() != type2.getSuit()) {
                        threeAndTwoOfAKind = false;
                        break;
                    } else if (hand.get(i).getSuit() == type1.getSuit()) numType1++;
                    else if (hand.get(i).getSuit() == type2.getSuit()) numType2++;
                }
                if (numType1 >= 3 || numType2 >= 3) threeOfAKind = true;
            }

            boolean flush = true;
            Card type = hand.get(0);
            for (int i = 1; i < hand.size(); i++) {
                if (hand.get(i).getSuit() != type.getSuit()) {
                    flush = false;
                    break;
                }
            }

            boolean straight = true;
            for (int i = 0; i < hand.size() - 1; i++) {
                if (hand.get(i).getNumber() == 14 && hand.get(i + 1).getNumber() == 2) continue;
                else if (hand.get(i).getNumber() < hand.get(i + 1).getNumber()) continue;
                else {
                    straight = false;
                    break;
                }
            }

            boolean twoPairs = false;
            boolean pairOfRoyals = false;
            ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
            for (int i = 0; i < hand.size(); i++) {
                boolean added = false;
                for (int j = 0; j < pairs.size(); j++) {
                    if (pairs.get(j).equals(hand.get(i).getNumber())) {
                        added = true;
                        pairs.get(j).setItem2(pairs.get(j).getItem2() + 1);
                    }
                }
                if (!added) pairs.add(new Pair<Integer, Integer>(hand.get(i).getNumber(), 1));
            }
            int numPairs = 0;
            for (Pair<Integer, Integer> pair : pairs) {
                if (pair.getItem2() > 2) numPairs++;
                if (pair.getItem1() > 10) pairOfRoyals = true;
            }
            if (numPairs > 2) twoPairs = true;

            for (int i = 0; i < 4; i++) {
                if (lastPtsWon != 0) break;

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

                int sequenceLength = 0;
                for (int j = 2; j < 15; j++) {
                    if (hand.contains(new Card(reqSuit, j))) sequenceLength++;
                    else sequenceLength = 0;
                }

                if (hand.contains(new Card(reqSuit, 10)) &&
                        hand.contains(new Card(reqSuit, 11)) &&
                        hand.contains(new Card(reqSuit, 12)) &&
                        hand.contains(new Card(reqSuit, 13)) &&
                        hand.contains(new Card(reqSuit, 14))) { // Royal flush
                    lastPtsWon += 250;
                } else if (sequenceLength >= 5) lastPtsWon += 50; // Straight flush
                else if (fourOfAKind) lastPtsWon += 25;
                else if (threeAndTwoOfAKind) lastPtsWon += 9;
                else if (flush) lastPtsWon += 6;
                else if (straight) lastPtsWon += 4;
                else if (threeOfAKind) lastPtsWon += 3;
                else if (twoPairs) lastPtsWon += 2;
                else if (pairOfRoyals) lastPtsWon += 1;
            }

            playerPts += lastPtsWon;
            if (playerPts > 0) play.setEnabled(true);
            draw.setEnabled(false);

            if (lastPtsWon > 0) Sounds.GAME_WON.play();
            else Sounds.GAME_LOST.play();
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
