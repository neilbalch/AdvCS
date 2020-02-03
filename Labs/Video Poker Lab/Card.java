import java.awt.*;

public class Card {
    enum Suit {CLUB, DIAMOND, HEART, SPADE}

    private Suit suit;
    private int number;

    public Card(Suit suit, int number) {
        this.suit = suit;
        this.number = number;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean equals(Card o) {
        return suit == o.getSuit() && number == o.getNumber();
    }

    public void drawCard(Graphics g, Point location, Dimension size) {
        g.setColor(Color.WHITE);
        g.fillRect(location.x, location.y, size.width, size.height);
        g.setColor(Color.BLACK);
        g.drawRect(location.x, location.y, size.width, size.height);

        double diag = Math.sqrt(Math.pow(size.height, 2) + Math.pow(size.width, 2));

        g.setFont(new Font("Arial", Font.BOLD, (int) (diag * 0.1)));
        if (suit == Suit.SPADE || suit == Suit.CLUB) g.setColor(Color.BLACK);
        else g.setColor(Color.RED);

        g.drawString(this.toString(), location.x + 15, location.y + 15 + (int) (diag * 0.08));
        g.drawString(this.toString(), location.x + size.width - 15 - (int) (diag * 0.1), location.y + size.height - 15);

        String suit = getSuitStr();
        g.setFont(new Font("Arial", Font.BOLD, (int) (diag * 0.2)));
        g.drawString(suit, location.x + size.width / 2 - (int) (diag * 0.05), location.y + size.height / 2 + (int) (diag * 0.1));
    }

    private String getSuitStr() {
        switch (this.suit) {
            case CLUB:
                return "♣";
            case DIAMOND:
                return "♦";
            case HEART:
                return "♥";
            case SPADE:
                return "♠";
            default:
                return "";
        }
    }

    public String toString() {
        String suit = getSuitStr();

        if (number == 11) return "J" + suit;
        if (number == 12) return "Q" + suit;
        if (number == 13) return "K" + suit;
        if (number == 14) return "A" + suit;
        else return number + suit;
    }
}
