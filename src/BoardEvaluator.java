import java.awt.*;

public class BoardEvaluator {
    public int evaluateBoard(Color[][] board) {
        int heightScore = getHeight(board);
        int holesScore = getHoles(board);
        int linesCleared = getClearedLines(board);
        int bumpinessScore = getBumpiness(board);

        return (-4 * heightScore) + (3 * linesCleared) - (5 * holesScore) - (2 * bumpinessScore);
    }

    private int getHeight(Color[][] board) {
        int height = 0;
        for (int x = 0; x < board[0].length; x++) {
            for(int y = 0; y < board.length; y++) {
                if (board[y][x] != null) {
                    height = Math.max(height, board.length - y);
                    break;
                }
            }
        }
        return height;
    }

    private int getHoles(Color[][] board) {
        int holes = 0;
        for (int x = 0; x < board[0].length; x++) {
            boolean foundBlock = false;
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != null) {
                    foundBlock = true;
                } else if (foundBlock && board[y][x] == null) {
                    holes++;
                }
            }
        }
        return holes;
    }

    private int getClearedLines(Color[][] board) {
        int clearedLines = 0;
        for(int y = 0; y < board.length; y++) {
            boolean isLineFull = true;
            for(int x = 0; x < board.length; x++) {
                if (board[y][x] == null) {
                    isLineFull = false;
                    break;
                }
            }
        }
        return clearedLines;
    }

    private int getBumpiness(Color[][] board) {
        int bumpiness = 0;
        for(int x = 0; x < board[0].length - 1; x++) {
            int colHeight1 = getColumnHeight(board, x);
            int colHeight2 = getColumnHeight(board, x + 1);
            bumpiness += Math.abs(colHeight1 - colHeight2);
        }
        return bumpiness;
    }

    private int getColumnHeight(Color[][] board, int col) {
        for(int y = 0; y < board.length; y++) {
            if (board[y][col] != null) {
                return board.length - y;
            }
        }
        return 0;
    }

}
