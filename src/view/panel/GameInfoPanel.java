package view.panel;

import Common.Common;
import model.GameInfo;
import utilities.*;

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
        LabelFactory centerFactory = new CenterLabelFactory();
        gameInfoLabel = centerFactory.createLabel("Game Info (Player " + this.gameInfo.getPlayerNum() + ")");
        int playerType = gameInfo.getPlayerType();
        String playerTypeString = playerType == 0 ? "Human" : playerType == 1 ? "AI" : "External";
        playerTypeLabel = centerFactory.createLabel("Player Type: " + playerTypeString);
        initLevelLabel = centerFactory.createLabel("Initial Level: " + common.gameConfig.getGameLevel());
        currLevelLabel = centerFactory.createLabel("Current Level: " + common.gameConfig.getGameLevel());
        lineErasedLabel = centerFactory.createLabel("Line Erased: " + "0");
        scoreLabel = centerFactory.createLabel("Score: " + "0");
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
