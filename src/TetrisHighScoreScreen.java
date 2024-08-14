import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class TetrisHighScoreScreen extends JFrame {

    public TetrisHighScoreScreen() {
        List<PlayerScore> playerScores = new ArrayList<>();

        setTitle("Tetris High Score Screen");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(11, 2));

        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Score"));

        for (int i = 1; i <= 10; i++) {
            playerScores.add(new PlayerScore("Player " + i, (int) (Math.random() * 100)));
        }

        playerScores.sort((a, b) -> Integer.compare(b.score(), a.score()));

        for (PlayerScore playerScore : playerScores) {
            panel.add(new JLabel(playerScore.name()));
            panel.add(new JLabel(String.valueOf(playerScore.score())));
        }

        add(panel);
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
        return "PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ']';
    }

}

