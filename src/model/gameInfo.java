package model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameInfo {
    private final Common common; // Assuming Common is a class
    private int score;
    private int initLevel;
    private int currLevel;
    private int lineErased;
    private int playerNum;
    private int playerType;
    private Block nextBlock;
    private List<HighScore> highScores;

    public GameInfo(Common common, int playerNum, int playerType) {
        this.common = common;
        this.playerNum = playerNum;
        this.playerType = playerType;
        this.score = 0;
        this.initLevel = common.gameConfig.getGameLevel();
        this.currLevel = initLevel;
        this.lineErased = 0;
        this.nextBlock = null;
        this.highScores = new ArrayList<>();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getInitLevel() {
        return initLevel;
    }

    public void setInitLevel(int initLevel) {
        this.initLevel = initLevel;
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public void setCurrLevel(int currLevel) {
        this.currLevel = currLevel;
    }

    public int getLineErased() {
        return lineErased;
    }

    public void setLineErased(int lineErased) {
        this.lineErased = lineErased;
    }

    public void updateGameInfo(int linesCleared) {
        int scoreIncrement = 0;
        switch (linesCleared) {
            case 1:
                scoreIncrement = 100;
                break;
            case 2:
                scoreIncrement = 300;
                break;
            case 3:
                scoreIncrement = 600;
                break;
            case 4:
                scoreIncrement = 1000;
                break;
            default:
                break;
        }

        this.setScore(this.getScore() + scoreIncrement);

        int currentLineErased = this.getLineErased();
        this.setLineErased(currentLineErased + linesCleared);
        int newLineErased = this.getLineErased();

        if (newLineErased / 10 != currentLineErased / 10) {
            this.setCurrLevel(this.getCurrLevel() + 1);
        }

        // Assuming gameInfoPanel is a member of the class or accessible here
        gameInfoPanel.repaint();

        // Check if the score is within the top 10
        checkHighScore();
    }

    private void checkHighScore() {
        if (highScores.size() < 10 || score > highScores.get(highScores.size() - 1).getScore()) {
            String playerName = JOptionPane.showInputDialog("Congratulations! Enter your name:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                highScores.add(new HighScore(playerName, score));
                highScores.sort(Comparator.comparingInt(HighScore::getScore).reversed());
                if (highScores.size() > 10) {
                    highScores.remove(highScores.size() - 1);
                }
            }
        }
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    // Inner class to represent a high score entry
    private static class HighScore {
        private final String name;
        private final int score;

        public HighScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
