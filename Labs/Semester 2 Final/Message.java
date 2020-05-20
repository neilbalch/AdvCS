import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {PlayerTurn, PlayerMadeMove}

    public enum Card {
        ONE, TWO, THREE, FOUR, FIVE, SEVEN, EIGHT, TEN, ELEVEN, TWELVE, SORRY;

        public static Card selectCard() {
            return Card.values()[(int) (Math.random() * Card.values().length)];
        }
    }

    public Type type;
    public int playerNum;
    public Card card; // Only applicable for PlayerTurn
    public Player[] players;
}
