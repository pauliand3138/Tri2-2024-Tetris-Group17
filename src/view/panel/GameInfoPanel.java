package view.panel;

import Common.Common;
import model.GameInfo;

import javax.swing.*;
import java.awt.*;

import static view.MainScreen.common;

public class GameInfoPanel extends JPanel {
    private JLabel gameInfoLabel;
    private JLabel playerTypeLabel;
    private JLabel initLevelLabel;
    private JLabel currLevelLabel = new JLabel("Current Level: " + common.gameConfig.getGameLevel());;
    private JLabel lineErasedLabel = new JLabel("Line Erased: " + "0");;
    private JLabel scoreLabel = new JLabel("Score: " + "0");
    private JLabel nextTetrominoLabel;
    public NextBlockPanel nextBlockPanel;
    private GameInfo gameInfo;

    public GameInfoPanel(int playerNum, int playerType) {
        this.gameInfo = new GameInfo(playerNum, playerType);
        initPane();
    }

    public void initPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(8, 1, 0, 15));
        add(textPanel, BorderLayout.CENTER);
        gameInfoLabel = new JLabel("Game Info (Player " + this.gameInfo.getPlayerNum() + ")");
        gameInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(gameInfoLabel);
        int playerType = gameInfo.getPlayerType();
        String playerTypeString = playerType == 0 ? "Human" : playerType == 1 ? "AI" : "External";
        playerTypeLabel = new JLabel("Player Type: " + playerTypeString);
        playerTypeLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(playerTypeLabel);
        initLevelLabel = new JLabel("Initial Level: " + common.gameConfig.getGameLevel());
        initLevelLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(initLevelLabel);
        currLevelLabel = new JLabel("Current Level: " + common.gameConfig.getGameLevel());
        currLevelLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(currLevelLabel);
        lineErasedLabel = new JLabel("Line Erased: " + "0");
        lineErasedLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(lineErasedLabel);
        scoreLabel = new JLabel("Score: " + "0");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(scoreLabel);
        nextTetrominoLabel = new JLabel("Next Tetromino:");
        nextTetrominoLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(nextTetrominoLabel);
        JPanel nextBlockPanel = new JPanel();
        add(nextBlockPanel, BorderLayout.SOUTH);
        nextBlockPanel.add(this.nextBlockPanel = new NextBlockPanel());
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void repaint() {
        // Codes to update new value for current level, line erase, score and next shape
        if (gameInfo != null) {
            this.currLevelLabel.setText("Current Level: " + gameInfo.getCurrLevel());
            this.lineErasedLabel.setText("Line Erased: " + gameInfo.getLineErased());
            this.scoreLabel.setText("Score: " + gameInfo.getScore());
        }
    }
}
