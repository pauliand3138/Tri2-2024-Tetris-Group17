package view.panel;

import utilities.*;
import utilities.LabelFactory;
import view.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TetrisHighScoreScreen extends JFrame {

    public TetrisHighScoreScreen() {
        Font HEADER_FONT = new Font("Calibri", Font.BOLD, 16);

        List<PlayerScore> playerScores = new ArrayList<>();

        setTitle("Tetris High Score Screen");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        LabelFactory titleFactory = new TitleLabelFactory();
        JLabel titleLabel = titleFactory.createLabel("High Score");


        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel panel = new JPanel(new GridLayout(11, 2));

        LabelFactory subtitleFactory = new SubtitleLabelFactory();
        JLabel nameLabel = subtitleFactory.createLabel("Name");
        JLabel scoreLabel = subtitleFactory.createLabel("Score");
//
//        nameLabel.setFont(HEADER_FONT);
//        scoreLabel.setFont(HEADER_FONT);

//        centerJLabel(nameLabel);
//        centerJLabel(scoreLabel);

        panel.add(nameLabel);
        panel.add(scoreLabel);

        for (int i = 1; i <= 10; i++) {
            playerScores.add(new PlayerScore("Player " + i, (int) (Math.random() * 100)));
        }

        playerScores.sort((a, b) -> Integer.compare(b.score(), a.score()));

        List<JLabel> playerScoreLabels = new ArrayList<>();
        LabelFactory centerLabelFactory = new CenterLabelFactory();

        for (PlayerScore playerScore : playerScores) {
            JLabel name = centerLabelFactory.createLabel(playerScore.name());
            JLabel score = centerLabelFactory.createLabel(String.valueOf(playerScore.score()));
            playerScoreLabels.add(name);
            playerScoreLabels.add(score);
        }

        for(JLabel label : playerScoreLabels) {
            //centerJLabel(label);
            panel.add(label);
        }

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

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Moving up back button slightly
        backButtonPanel.add(backButton);
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
        return "view.panel.PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ']';
    }

}

