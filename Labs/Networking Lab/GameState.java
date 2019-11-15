import java.awt.*;
import java.io.Serializable;

class GameState implements Serializable {
    public enum State {START, TURN1, TURN2, OVER}

    ;
    public State state;

//    public enum Mode {PvP, PvComp};
//    public Mode mode;

    public int[][] board;
    private Point topLeft;

    public GameState(Point topLeft) {
        state = State.TURN1;
//        mode = Mode.PvP;
        board = new int[3][3];
        this.topLeft = topLeft;
    }

    public void drawBoard(Graphics g) {
        for (int r = 0; r < board.length; r++) {         // r --> y
            for (int c = 0; c < board[r].length; c++) {  // c --> x
                g.setColor(Color.WHITE);
                g.fillRect(topLeft.x + c * (50 + 3), topLeft.y + r * (50 + 3), 50, 50);

                g.setFont(new Font("Arial", Font.BOLD, 36));
                g.setColor(Color.BLACK);
                switch (board[r][c]) {
                    case 1:
                        g.drawString("X", topLeft.x + c * (50 + 3) + 15, topLeft.y + r * (50 + 3) + 40);
                        break;
                    case 2:
                        g.drawString("O", topLeft.x + c * (50 + 3) + 15, topLeft.y + r * (50 + 3) + 40);
                        break;
                }

                g.fillRect(topLeft.x + c * (50 + 3) - 3, topLeft.y - 3, 3, board.length * (50 + 3) + 3);
            }
            g.fillRect(topLeft.x - 3, topLeft.y + r * (50 + 3) - 3, board[r].length * (50 + 3) + 3, 3);
        }

        g.fillRect(topLeft.x + board[0].length * (50 + 3) - 3, topLeft.y - 3, 3, board.length * (50 + 3) + 3);
        g.fillRect(topLeft.x - 3, topLeft.y + board.length * (50 + 3) - 3, board[0].length * (50 + 3) + 3, 3);

        g.setColor(Color.BLACK);
    }

    private boolean coordsWithin(Point location, Point topLeft, Point bottomRight) {
        if (location.x > topLeft.x && location.x < bottomRight.x &&
                location.y > topLeft.y && location.y < bottomRight.y) return true;
        else return false;
    }

    public boolean handleClick(Point click) {
        for (int r = 0; r < board.length; r++) {         // r --> y
            for (int c = 0; c < board[r].length; c++) {  // c --> x
                if (coordsWithin(click,
                        new Point(topLeft.x + c * (50 + 3), topLeft.y + r * (50 + 3)),
                        new Point(topLeft.x + (c + 1) * (50 + 3), topLeft.y + (r + 1) * (50 + 3)))) {
                    System.out.println("Click Found in x=" + c + ", y:" + r);
                    return insertXO(r, c);
                }
            }
        }

        return false;
    }

    // depreciated
    public void printTable() {
        for (int[] row : board) {
            for (int item : row) {
                String toPrint = "";
                if (item == 0) toPrint += "-";
                else if (item == 1) toPrint += "X";
                else if (item == 2) toPrint += "O";
                System.out.print(toPrint + "\t");
            }
            System.out.println();
        }

        System.out.println();
    }

    public boolean insertXO(int r, int c) {
        if (board[r][c] == 0) {
            if (state == State.TURN1) {
                board[r][c] = 1;
                state = State.TURN2;
            } else {
                board[r][c] = 2;
                state = State.TURN1;
            }

            return true;
        }

        return false;
    }

    public boolean checkFull() {
        boolean full = true;
        for (int[] row : board) {
            for (int item : row) {
                if (item == 0) full = false;
            }
        }

        return full;
    }

    public int checkTicTackToe() {
        // Check Left-to-rights
        if (board[0][0] == 1 && board[0][1] == 1 && board[0][2] == 1) return 1;
        if (board[0][0] == 2 && board[0][1] == 2 && board[0][2] == 2) return 2;
        if (board[1][0] == 1 && board[1][1] == 1 && board[1][2] == 1) return 1;
        if (board[1][0] == 2 && board[1][1] == 2 && board[1][2] == 2) return 2;
        if (board[2][0] == 1 && board[2][1] == 1 && board[2][2] == 1) return 1;
        if (board[2][0] == 2 && board[2][1] == 2 && board[2][2] == 2) return 2;
        // Check Up-and-Downs
        if (board[0][0] == 1 && board[1][0] == 1 && board[2][0] == 1) return 1;
        if (board[0][0] == 2 && board[1][0] == 2 && board[2][0] == 2) return 2;
        if (board[0][1] == 1 && board[1][1] == 1 && board[2][1] == 1) return 1;
        if (board[0][1] == 2 && board[1][1] == 2 && board[2][1] == 2) return 2;
        if (board[0][2] == 1 && board[1][2] == 1 && board[2][2] == 1) return 1;
        if (board[0][2] == 2 && board[1][2] == 2 && board[2][2] == 2) return 2;
        // Check diagonals
        if (board[0][0] == 1 && board[1][1] == 1 && board[2][2] == 1) return 1;
        if (board[0][0] == 2 && board[1][1] == 2 && board[2][2] == 2) return 2;
        if (board[0][2] == 1 && board[1][1] == 1 && board[2][0] == 1) return 1;
        if (board[0][2] == 2 && board[1][1] == 2 && board[2][0] == 2) return 2;

        return 0;
    }
}
