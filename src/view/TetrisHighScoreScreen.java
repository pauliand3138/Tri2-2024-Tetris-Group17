package view;

import model.GameConfig;
import utilities.*;
import utilities.LabelFactory;
import view.panel.ScrollingTextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static view.MainScreen.common;

public class TetrisHighScoreScreen extends JFrame {

    public TetrisHighScoreScreen() {
        initContent();
    }

    public void initContent() {
        List<PlayerScore> playerScores = new ArrayList<>();

        setTitle("Tetris High Score Screen");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        LabelFactory titleFactory = new TitleLabelFactory();
        JLabel titleLabel = titleFactory.createLabel("High Score");


        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel panel = new JPanel(new GridLayout(11, 4));

        LabelFactory subtitleFactory = new SubtitleLabelFactory();
        JLabel rankLabel = subtitleFactory.createLabel("Rank");
        JLabel nameLabel = subtitleFactory.createLabel("Name");
        JLabel scoreLabel = subtitleFactory.createLabel("Score");
        JLabel configLabel = subtitleFactory.createLabel("Config");

        panel.add(rankLabel);
        panel.add(nameLabel);
        panel.add(scoreLabel);
        panel.add(configLabel);

        LabelFactory centerFactory = new CenterLabelFactory();
        for (int i = 0; i < 10; i++) {
            panel.add(centerFactory.createLabel(String.valueOf(i + 1)) );
            if (i < common.scoreList.getScores().size()) {
                panel.add(centerFactory.createLabel(common.scoreList.getScores().get(i).getName()));
                panel.add(centerFactory.createLabel(common.scoreList.getScores().get(i).getScore() + ""));
                GameConfig playerConfig = common.scoreList.getScores().get(i).getConfig();
                int width = playerConfig.getFieldWidth();
                int height = playerConfig.getFieldHeight();
                int initLevel = playerConfig.getGameLevel();
                int playerType = playerConfig.getPlayerOneType();
                String playerTypeString = playerType == 0 ? "Human" : playerType == 1 ? "AI" : "External";
                String gameMode = playerConfig.isExtendMode() ? "Double" : "Single";
                String configString = width+"x"+height+"("+initLevel+") "+playerTypeString+" "+gameMode;
                panel.add(centerFactory.createLabel(configString));
            } else {
                panel.add(centerFactory.createLabel("---"));
                panel.add(centerFactory.createLabel("0"));
                panel.add(centerFactory.createLabel("---"));
            }

        }

        JButton clearHighScoreButton = new JButton("Clear High Score");
        clearHighScoreButton.setPreferredSize(new Dimension(360, 30));
        clearHighScoreButton.setBackground(Color.WHITE);

        clearHighScoreButton.setBounds(0, 0, 80, 20);
        clearHighScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure to clear the high scores?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    common.scoreList.clearScores();
                    TetrisHighScoreScreen newHighScoreScreen = new TetrisHighScoreScreen();
                    newHighScoreScreen.setVisible(true);
                    dispose();
                }

            }
        });

        float titlePanelPct = 0.20F;
        float highScorePanelPct = 0.50F;
        float creatorsPanelPct = 1 - highScorePanelPct - titlePanelPct;

        int creatorsPanelHeight = (int) (720 * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setLayout(new BorderLayout());
        creatorsPanel.setSize(1080, creatorsPanelHeight);

        ScrollingTextPanel scrollingTextPanel = new ScrollingTextPanel();
        scrollingTextPanel.setPreferredSize(new Dimension(1080, 30));
        creatorsPanel.add(scrollingTextPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(360, 30));
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button pressed. Going back to main menu...");

                setVisible(false);
                MainScreen mainScreen = new MainScreen();
                mainScreen.setVisible(true);
            }
        });
        //creatorsPanel.add(backButton, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10)); // Moving up back button slightly
        backButtonPanel.add(backButton);
        backButtonPanel.add(clearHighScoreButton);
        creatorsPanel.add(backButtonPanel, BorderLayout.NORTH);

        add(titlePanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(creatorsPanel, BorderLayout.SOUTH);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TetrisHighScoreScreen::run);
    }

    private static void run() {
        TetrisHighScoreScreen highScoreScreen = new TetrisHighScoreScreen();
        highScoreScreen.setVisible(true);
    }
}

record PlayerScore(String name, int score) {

    @Override
    public String toString() {
        return "view.PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ']';
    }

}

