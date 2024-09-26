package controller;

import Common.Common;
import model.Block;
import model.Move;

import static view.MainScreen.common;

public class AIController {
    private final BoardEvaluator evaluator = new BoardEvaluator();

    public AIController() {
    }

    public Move findBestMove(int[][] board, Block block) {
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (int rotation = 0; rotation < 4; rotation++) {
            for(int col = 0; col < common.gameConfig.getFieldWidth(); col++) {
                Block simulatedBlock = new Block(block);
                simulatedBlock.rotate();

                int[][] simulatedBoard = simulateDrop(board, simulatedBlock, col);
                int score = evaluator.evaluateBoard(simulatedBoard);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Move(col, rotation);
                }
            }
        }

        return bestMove;
    }

    private int[][] simulateDrop(int[][] board, Block block, int col) {
        int[][] simulatedBoard = copyBoard(board);
        int dropRow = getDropRow(simulatedBoard, block, col);
        placePiece(simulatedBoard, block, col, dropRow);
        return simulatedBoard;
    }

    private int getDropRow(int[][] board, Block block, int col) {
        int row = 0;
        while (canPlacePiece(board, block, col, row)) {
            row++;
        }
        return row - 1;
    }

    private boolean canPlacePiece(int[][] board, Block block, int testCol, int testRow) {
        int[][] shape = block.getBlockInfo().getShape();
        int height = block.getBlockInfo().getRows();
        int width = block.getBlockInfo().getColumns();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1) {
                    int x = col + testCol;
                    int y = row + testRow;
                    if (x < 0 || x >= board[0].length || y < 0 || y >= board.length || board[y][x] != 0)  {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placePiece(int[][] board, Block block, int testCol, int testRow) {
        int[][] shape = block.getBlockInfo().getShape();
        int height = block.getBlockInfo().getRows();
        int width = block.getBlockInfo().getColumns();
        int x = block.getX() + testCol;
        int y = (int)block.getY() + testRow;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1 && row + y >= 0 && col + x >= 0 && row + y < board.length && col + x < board[row].length) {
                    board[row + y][col + x] = 1;
                }
            }
        }
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            System.arraycopy(board[y], 0, newBoard[y], 0, board[y].length);
        }
        return newBoard;
    }
}
