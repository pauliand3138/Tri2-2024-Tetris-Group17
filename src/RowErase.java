import java.awt.Color;

public class RowErase {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final Tetrominoes[][] board;
    private int score;
    private int level;
    private int rowsErased;

    public RowErase() {
        board = new Tetrominoes[BOARD_HEIGHT][BOARD_WIDTH];
        clearBoard();
        score = 0;
        level = 1;
        rowsErased = 0;
    }

    public void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = Tetrominoes.NoShape;
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                System.out.print(board[i][j] == Tetrominoes.NoShape ? "." : "#");
            }
            System.out.println();
        }
        System.out.println("Score: " + score);
        System.out.println("Level: " + level);
    }

    public void eraseRows(int rows) {
        switch (rows) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 600;
                break;
            case 4:
                score += 1000;
                break;
            default:
                break;
        }
        rowsErased += rows;
        if (rowsErased >= 10) {
            level++;
            rowsErased -= 10;
        }
    }

    public enum Tetrominoes {
        NoShape(new Color(0, 0, 0)),
        ZShape(new Color(204, 102, 102)),
        SShape(new Color(102, 204, 102)),
        LineShape(new Color(102, 102, 204)),
        TShape(new Color(204, 204, 102)),
        SquareShape(new Color(204, 102, 204)),
        LShape(new Color(102, 204, 204)),
        MirroredLShape(new Color(218, 170, 0));

        Tetrominoes(Color ignoredColor) {
        }
    }

    public static void main(String[] args) {
        RowErase game = new RowErase();
        game.printBoard();
        // Simulate erasing rows
        game.eraseRows(1);
        game.printBoard();
        game.eraseRows(4);
        game.printBoard();
        game.eraseRows(5);
        game.printBoard();
    }
}
