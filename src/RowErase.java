import java.awt.Color;

public class RowErase {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final Tetrominoes[][] board;

    public RowErase() {
        board = new Tetrominoes[BOARD_HEIGHT][BOARD_WIDTH];
        clearBoard();
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
    }

    public boolean detectCollision(Tetrominoes piece, int x, int y) {
        for (int i = 0; i < 4; i++) {
            int newX = x + piece.x(i);
            int newY = y - piece.y(i);
            if (newX < 0 || newX >= BOARD_WIDTH || newY < 0 || newY >= BOARD_HEIGHT) {
                return true; // Collision with boundaries
            }
            if (board[newY][newX] != Tetrominoes.NoShape) {
                return true; // Collision with settled blocks
            }
        }
        return false;
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

        public int x(int index) {
            // Define x-coordinates for each shape
            return 0;
        }

        public int y(int index) {
            // Define y-coordinates for each shape
            return 0;
        }
    }

    public static void main(String[] args) {
        RowErase game = new RowErase();
        game.printBoard();
        // Add pieces and test row erasure and collision detection
    }
}
