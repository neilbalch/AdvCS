import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {PlayerTurn, PlayerMadeMove, PlayerWonGame}

    public enum Card {
        ONE, TWO, THREE, FOUR, FIVE, SEVEN, EIGHT, TEN, ELEVEN, TWELVE, SORRY;

        public static Card selectCard() {
            int multipleOne = 2; // ONE will be selected 2x more than normal.
            int multipleTwo = 2; // TWO will be selected 2x more than normal.
            int selection = (int) (Math.random() * ((1 + multipleOne) + (1 + multipleTwo) + Card.values().length - 2));

            if (selection < (multipleOne + 1)) return Card.values()[0];
            else if (selection < (multipleOne + 1 + multipleTwo + 1)) return Card.values()[1];
            else return Card.values()[selection - multipleOne - multipleTwo];

//            return Card.values()[(int) (Math.random() * Card.values().length)];
        }
    }

    public Type type;
    public int playerNum;
    public Card card; // Only applicable for PlayerTurn
    public int pawnMoved; // Not applicable for PlayerTurn
    public Player[] players;
}
