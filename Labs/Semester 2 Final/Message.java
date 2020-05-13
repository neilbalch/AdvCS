import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {PlayerTurn, PlayerMadeMove}

    public enum Cards {
        ONE, TWO, THREE, FOUR, FIVE, SEVEN, EIGHT, TEN, ELEVEN, TWELVE, SORRY;

        public static Cards selectCard() {
            return Cards.values()[(int) (Math.random() * Cards.values().length)];
        }
    }

    public Type type;
    public int playerNum;
    public Cards card; // Only applicable for PlayerTurn
    public Player[] players;
}
