package view.panel;

import model.GameInfo;
import utilities.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static view.MainScreen.common;

public class GameInfoPanel extends JPanel {
    private JLabel gameInfoLabel;
    private JLabel playerTypeLabel;
    private JLabel initLevelLabel;
    private JLabel currLevelLabel;
    private JLabel lineErasedLabel;
    private JLabel scoreLabel;
    private JLabel nextTetrominoLabel;
    public NextBlockPanel nextBlockPanel;
    private GameInfo gameInfo;
    private List<ScoreEntry> highScores;

    public GameInfoPanel(int playerNum, int playerType) {
        this.gameInfo = new GameInfo(playerNum, playerType);;
        this.highScores = new ArrayList<>();
        initPane();
    }

    public void initPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(8, 1, 0, 15));
        add(textPanel, BorderLayout.CENTER);
        LabelFactory centerFactory = new CenterLabelFactory();
        gameInfoLabel = centerFactory.createLabel("Game Info (Player " + this.gameInfo.getPlayerNum() + ")");
        int playerType = gameInfo.getPlayerType();
        String playerTypeString = playerType == 0 ? "Human" : playerType == 1 ? "AI" : "External";
        playerTypeLabel = centerFactory.createLabel("Player Type: " + playerTypeString);
        initLevelLabel = centerFactory.createLabel("Initial Level: " + common.gameConfig.getGameLevel());
        currLevelLabel = centerFactory.createLabel("Current Level: " + common.gameConfig.getGameLevel());
        lineErasedLabel = centerFactory.createLabel("Line Erased: 0");
        scoreLabel = centerFactory.createLabel("Score: 0");
        nextTetrominoLabel = centerFactory.createLabel("Next Tetromino:");
        textPanel.add(gameInfoLabel);
        textPanel.add(playerTypeLabel);
        textPanel.add(initLevelLabel);
        textPanel.add(currLevelLabel);
        textPanel.add(lineErasedLabel);
        textPanel.add(scoreLabel);
        textPanel.add(nextTetrominoLabel);
        JPanel nextBlockPanel = new JPanel();
        add(nextBlockPanel, BorderLayout.SOUTH);
        nextBlockPanel.add(this.nextBlockPanel = new NextBlockPanel());
    }

    public void updateGameInfo() {
        SwingUtilities.invokeLater(() -> {
            if (gameInfo != null) {
                this.currLevelLabel.setText("Current Level: " + gameInfo.getCurrLevel());
                this.lineErasedLabel.setText("Line Erased: " + gameInfo.getLineErased());
                this.scoreLabel.setText("Score: " + gameInfo.getScore());
            }
        });
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
//    public void clearLines() {
//        boolean completedLine;
//        int linesCleared = 0;
//
//        for (int row = ROW_COUNT - 1; row >= 0; row--) {
//            completedLine = true;
//            for (int col = 0; col < COL_COUNT; col++) {
//                // Check if the line is completed
//                // Assuming there's a method isCellOccupied(row, col) to check if a cell is occupied
//                if (!isCellOccupied(row, col)) {
//                    completedLine = false;
//                    break;
//                }
//            }
//            if (completedLine) {
//                linesCleared++;
//                // Assuming there's a method clearLine(row) to clear the line
//                clearLine(row);
//            }
//        }
//
//        if (linesCleared > 0) {
//            int scoreIncrement;
//            switch (linesCleared) {
//                case 1:
//                    scoreIncrement = 100;
//                    break;
//                case 2:
//                    scoreIncrement = 300;
//                    break;
//                case 3:
//                    scoreIncrement = 600;
//                    break;
//                case 4:
//                    scoreIncrement = 1000;
//                    break;
//                default:
//                    scoreIncrement = 0;
//            }
//            gameInfo.setScore(gameInfo.getScore() + scoreIncrement);
//
//            int currentLineErased = gameInfo.getLineErased();
//            gameInfo.setLineErased(currentLineErased + linesCleared);
//            int newLineErased = gameInfo.getLineErased();
//
//            if (newLineErased / 10 != currentLineErased / 10) {
//                gameInfo.setCurrLevel(gameInfo.getCurrLevel() + 1);
//            }
//
//            updateGameInfo();
//            checkHighScore();
//        }
//    }
//
//    private boolean isCellOccupied(int row, int col) {
//        // Implement this method based on your game's logic
//        return false;
//    }
//
//    private void clearLine(int row) {
//        // Implement this method based on your game's logic
//    }

    private void checkHighScore() {
        int score = gameInfo.getScore();
        if (highScores.size() < 10 || score > highScores.get(highScores.size() - 1).getScore()) {
            String playerName = JOptionPane.showInputDialog(this, "Congratulations! Enter your name:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                highScores.add(new ScoreEntry(playerName, score));
                highScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
                if (highScores.size() > 10) {
                    highScores.remove(highScores.size() - 1);
                }
            }
        }
    }

    private static class ScoreEntry {
        private final String name;
        private final int score;

        public ScoreEntry(String name, int score) {
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

