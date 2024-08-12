import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TetrisHighScoreScreen extends JFrame {
    private final List<Integer> highScores;
    private final DefaultListModel<String> listModel;

    public TetrisHighScoreScreen() {
        highScores = new ArrayList<>();
        listModel = new DefaultListModel<>();

        setTitle("Tetris High Scores");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("High Score Ranking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        JList<String> scoreList = new JList<>(listModel);
        panel.add(new JScrollPane(scoreList), BorderLayout.CENTER);

        var addButton = getjButton();
        panel.add(addButton, BorderLayout.SOUTH);

        add(panel);
    }

    private JButton getjButton() {
        JButton addButton = new JButton("Add Score");
        addButton.addActionListener(_ -> {
            String scoreStr = JOptionPane.showInputDialog("Enter new score:");
            try {
                int score = Integer.parseInt(scoreStr);
                addHighScore(score);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid score. Please enter a number.");
            }
        });
        return addButton;
    }

    private void addHighScore(int score) {
        highScores.add(score);
        Collections.sort(highScores, Collections.reverseOrder());
        updateScoreList();
    }

    private void updateScoreList() {
        listModel.clear();
        for (int i = 0; i < highScores.size() && i < 10; i++) {
            listModel.addElement((i + 1) + ". " + highScores.get(i));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TetrisHighScoreScreen highScoreScreen = new TetrisHighScoreScreen();
                highScoreScreen.setVisible(true);
            }
        });
    }
}