import java.awt.*;
import java.util.HashMap;
import java.util.Stack;

public class GameState {
    private HashMap<Point, Tile> board;

    private Stack<Integer> player1Health;
    private Stack<Integer> player2Health;

    private int player1Items;
    private int player2Items;

    private Point player1Location;
    private Point player2Location;

    public GameState(boolean server) {
        board = new HashMap<Point, Tile>();

        player1Health = new Stack<Integer>();
        player2Health = new Stack<Integer>();
        for (int i = 0; i < 3; i++) {
            player1Health.push(0);
            player2Health.push(0);
        }

        player1Items = 0;
        player2Items = 0;

        player1Location = new Point(3, 2);
        player2Location = new Point(3, 4);

        if (server) {
            // Generate new board.
            for (int r = 0; r < 10; r++) {
                for (int c = 0; c < 10; c++) {
                    // TODO: Make randomization.
                    board.put(new Point(r, c), Tile.createTile(Tile.Type.LAND, false));
                }
            }

            // TODO: Open socket and send first board object.
        } else {
            // TODO: Open socket and receive first board object.
        }

        // TODO: Start thread to poll receiving messages.
    }

    public void recieveMessage(Message msg) {

    }

    public void draw(Graphics g, Dimension windowSize) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, windowSize.width, windowSize.height);

        int squareSize = windowSize.height / 10;
        int x = squareSize;
        int y = squareSize;
        g.setColor(Color.BLACK);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                g.fillRect(x, 0, 1, 800);
                x += squareSize;
            }
            g.fillRect(0, y, 800, 1);
            y += squareSize;
        }
    }

    public void deductHealthPoint(int player) {
        if (player == 1) {
            player1Health.pop();
            Message msg = Message.createMessage(1, Message.Action.PlayerLostHealth, null);
            // TODO: Send message.
        } else if (player == 2) {
            player2Health.pop();
            Message msg = Message.createMessage(2, Message.Action.PlayerLostHealth, null);
            // TODO: Send message.
        }
    }

    public void movePlayer(int player, Point direction) {
        if (direction.x == -1) {
            if (player == 1) player1Location.setLocation(player1Location.x - 1, player1Location.y);
            else if (player == 2)
                player2Location.setLocation(player2Location.x - 1, player2Location.y);
        } else if (direction.x == 1) {
            if (player == 1) player1Location.setLocation(player1Location.x + 1, player1Location.y);
            else if (player == 2)
                player2Location.setLocation(player2Location.x + 1, player2Location.y);
        } else if (direction.y == -1) {
            if (player == 1) player1Location.setLocation(player1Location.x, player1Location.y - 1);
            else if (player == 2)
                player2Location.setLocation(player2Location.x, player2Location.y - 1);
        } else if (direction.y == 1) {
            if (player == 1) player1Location.setLocation(player1Location.x, player1Location.y + 1);
            else if (player == 2)
                player2Location.setLocation(player2Location.x, player2Location.y + 1);
        }

        Message msg = Message.createMessage(player, Message.Action.PlayerMoved, player == 1 ? player1Location : player2Location);
        // TODO: Send message.
    }
}