package model;

import Common.Common;
import view.Play;

import java.awt.*;

public class Game {
    private GameConfig gameConfig;
    private GameInfo gameInfo;
    private Block currentBlock;
    private Block nextBlock;
    private int[][] board;
    private Color[][] droppedBlocks;
    private boolean isPaused = false;
    private boolean isGameOver = false;

    public Game(GameInfo gameInfo, int[][] board, Block currentBlock, Block nextBlock, Color[][] droppedBlocks) {
        this.gameConfig = Common.gameConfig;
        this.gameInfo = gameInfo;
        this.board = board;
        this.currentBlock = currentBlock;
        this.nextBlock = nextBlock;
        this.droppedBlocks = droppedBlocks;
    }

    public boolean isBlockOutside() {
        if (currentBlock.getY() < 0) {
            currentBlock = null;
            return true;
        }
        return false;
    }

    public void dropBlockNaturally() {
        if (currentBlock != null && !isPaused) {
            currentBlock.moveDownNaturally(gameInfo.getCurrLevel());
        }
    }

    public void moveBlockRight() {
        if (currentBlock != null && !isPaused && !isReachedRight()) {
            currentBlock.moveRight();
            Play.soundManager.playSound("move.wav");
        }
    }

    public boolean isReachedRight() {
        if (currentBlock.getX() + currentBlock.getBlockInfo().getColumns() == gameConfig.getFieldWidth()) return true;

        int[][] shape = currentBlock.getBlockInfo().getShape();
        int width = currentBlock.getBlockInfo().getColumns();
        int height = currentBlock.getBlockInfo().getRows();

        for (int row = 0; row < height; row++) {
            for (int col = width - 1; col >= 0; col--) {
                if(shape[row][col] != 0) {
                    int x = col + currentBlock.getX() + 1;
                    int y = row + (int)Math.floor(currentBlock.getY());
                    if (y < 0) break; // when the block still hasn't entered the frame
                    if (droppedBlocks[y][x] != null) return true;
                    break;
                }
            }
        }
        return false;
    }

    public void moveBlockLeft() {
        if (currentBlock != null && !isPaused && !isReachedLeft()) {
            currentBlock.moveLeft();
            Play.soundManager.playSound("move.wav");
        }
    }

    public boolean isReachedLeft() {
        if (currentBlock.getX() == 0) return true;

        int[][] shape = currentBlock.getBlockInfo().getShape();
        int width = currentBlock.getBlockInfo().getColumns();
        int height = currentBlock.getBlockInfo().getRows();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if(shape[row][col] != 0) {
                    int x = col + currentBlock.getX() - 1;
                    int y = row + (int)Math.floor(currentBlock.getY());
                    if (y < 0) break; // when the block still hasn't entered the frame
                    if (droppedBlocks[y][x] != null) return true;
                    break;
                }
            }
        }
        return false;
    }

    public void moveBlockDown(){
        if (currentBlock != null && !isPaused && !isReachedBottom()) {
            currentBlock.moveDown();
        }
    }

    public boolean isReachedBottom() {
        if ((int)(currentBlock.getBlockInfo().getRows() + currentBlock.getY()) == gameConfig.getFieldHeight()) {
            return true;
        }

        int[][] shape = currentBlock.getBlockInfo().getShape();
        int width = currentBlock.getBlockInfo().getColumns();
        int height = currentBlock.getBlockInfo().getRows();

        for (int col = 0; col < width; col++) {
            for (int row = height - 1; row >= 0; row--) {
                if(shape[row][col] != 0) {
                    int x = col + currentBlock.getX();
                    int y = row + (int)Math.floor(currentBlock.getY()) + 1;
                    if (y < 0) break; // when the block still hasn't entered the frame
                    if (droppedBlocks[y][x] != null) return true;
                    break;
                }
            }
        }

        return false;
    }

    public void rotateBlock() {
        if (currentBlock != null && !isPaused) {
            currentBlock.rotate();
            if (currentBlock.getX() < 0) currentBlock.setX(0);
            if (currentBlock.getX() + currentBlock.getBlockInfo().getColumns() >= gameConfig.getFieldWidth())
                currentBlock.setX(gameConfig.getFieldWidth() - currentBlock.getBlockInfo().getColumns());
            if ((int) (currentBlock.getBlockInfo().getRows() + currentBlock.getY()) >= gameConfig.getFieldHeight())
                currentBlock.setY(gameConfig.getFieldHeight() - currentBlock.getBlockInfo().getRows());
            Play.soundManager.playSound("move.wav");
        }
    }

    public void clearLines() {
        boolean completedLine = true;
        boolean shouldPlaySound = false;
        int linesCleared = 0;
        for (int row = gameConfig.getFieldHeight() - 1; row >= 0; row--) {
            completedLine = true;

            for (int col = 0; col < gameConfig.getFieldWidth(); col++) {
                if (droppedBlocks[row][col] == null) {
                    completedLine = false;
                    break;
                }
            }

            if (completedLine) {
                shouldPlaySound = true;
                linesCleared++;
                clearLine(row);
                moveLinesDown(row);
                clearLine(0);
                row++;
                //repaint();
            }
        }

        if (shouldPlaySound) {
            Play.soundManager.playSound("clearline.wav");
        }

        if (linesCleared == 1) {
            gameInfo.setScore(gameInfo.getScore() + 100);
        } else if (linesCleared == 2) {
            gameInfo.setScore(gameInfo.getScore() + 300);
        } else if (linesCleared == 3) {
            gameInfo.setScore(gameInfo.getScore() + 600);
        } else if (linesCleared == 4) {
            gameInfo.setScore(gameInfo.getScore() + 1000);
        }
        int currentLineErased = gameInfo.getLineErased();
        gameInfo.setLineErased(gameInfo.getLineErased() + linesCleared);
        int newLineErased = gameInfo.getLineErased();
        if (newLineErased / 10 != currentLineErased / 10) {
            gameInfo.setCurrLevel(gameInfo.getCurrLevel() + 1);
        }
        //gameInfoPanel.repaint();
    }

    public void moveLinesDown(int row) {
        for(int r = row; r > 0; r-- ) {
            for (int col = 0; col < gameConfig.getFieldWidth(); col++) {
                droppedBlocks[r][col] = droppedBlocks[r-1][col];
            }
        }
    }

    public void clearLine(int row) {
        for(int i = 0; i < gameConfig.getFieldWidth(); i++) {
            droppedBlocks[row][i] = null;
        }
    }

    public void setBlockAsDroppedBlock(){
        int[][] shape = currentBlock.getBlockInfo().getShape();
        int height = currentBlock.getBlockInfo().getRows();
        int width = currentBlock.getBlockInfo().getColumns();
        int x = currentBlock.getX();
        int y = (int)currentBlock.getY();
        Color color = currentBlock.getBlockInfo().getColour();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1 && row + y >= 0 && col + x >= 0 ) {
                    droppedBlocks[row + y][col + x] = color;
                }
            }
        }
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public void updateBoardArray() {
        for(int row = 0; row < gameConfig.getFieldHeight(); row++) {
            for(int col = 0; col < gameConfig.getFieldWidth(); col++) {
                if (droppedBlocks[row][col] != null) {
                    board[row][col] = 1;
                } else {
                    board[row][col] = 0;
                }
            }
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Color[][] getDroppedBlocks() {
        return droppedBlocks;
    }

    public void setDroppedBlocks(Color[][] droppedBlocks) {
        this.droppedBlocks = droppedBlocks;
    }
}
