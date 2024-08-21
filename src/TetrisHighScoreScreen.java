import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class TetrisHighScoreScreen extends JFrame {

    public TetrisHighScoreScreen() {
        Font HEADER_FONT = new Font("Calibri", Font.BOLD, 16);

        List<PlayerScore> playerScores = new ArrayList<>();

        setTitle("Tetris High Score Screen");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("High Scores");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        JPanel panel = new JPanel(new GridLayout(11, 2));

        JLabel nameLabel = new JLabel("Name");
        JLabel scoreLabel = new JLabel("Score");

        nameLabel.setFont(HEADER_FONT);
        scoreLabel.setFont(HEADER_FONT);

        centerJLabel(nameLabel);
        centerJLabel(scoreLabel);

        panel.add(nameLabel);
        panel.add(scoreLabel);

        for (int i = 1; i <= 10; i++) {
            playerScores.add(new PlayerScore("Player " + i, (int) (Math.random() * 100)));
        }

        playerScores.sort((a, b) -> Integer.compare(b.score(), a.score()));

        List<JLabel> playerScoreLabels = new ArrayList<>();

        for (PlayerScore playerScore : playerScores) {
            playerScoreLabels.add(new JLabel(playerScore.name()));
            playerScoreLabels.add(new JLabel(String.valueOf(playerScore.score())));
        }

        for(JLabel label : playerScoreLabels) {
            centerJLabel(label);
            panel.add(label);
        }

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 5));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button pressed. Going back to main menu...");

                setVisible(false);
                TetrisMainScreen mainScreen = new TetrisMainScreen();
                mainScreen.setVisible(true);
            }
        });

        JLabel footerLabel = new JLabel("Author: Group 17");
        footerLabel.setHorizontalAlignment(JLabel.CENTER);
        buttonPanel.add(backButton);
        buttonPanel.add(footerLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TetrisHighScoreScreen::run);
    }

    private static void run() {
        TetrisHighScoreScreen highScoreScreen = new TetrisHighScoreScreen();
        highScoreScreen.setVisible(true);
    }

    private static void centerJLabel(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
    }
}

record PlayerScore(String name, int score) {

    @Override
    public String toString() {
        return "PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ']';
    }

}

