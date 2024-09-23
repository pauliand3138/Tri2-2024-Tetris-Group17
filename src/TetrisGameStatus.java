import javax.swing.*;
import java.awt.*;

class GameStatus extends JPanel {
    private final String playerType;
    private final int initialLevel;
    private int currentLevel;
    private int currentScore;
    private int linesErased;

    public GameStatus(String playerType, int initialLevel) {
        this.playerType = playerType;
        this.initialLevel = initialLevel;
        this.currentLevel = initialLevel;
        this.currentScore = 0;
        this.linesErased = 0;
    }

    public void updateGameStatus(int currentLevel, int currentScore, int linesErased) {
        this.currentLevel = currentLevel;
        this.currentScore = currentScore;
        this.linesErased = linesErased;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Player Type: " + playerType, 10, 20);
        g.drawString("Initial Level: " + initialLevel, 10, 40);
        g.drawString("Current Level: " + currentLevel, 10, 60);
        g.drawString("Current Score: " + currentScore, 10, 80);
        g.drawString("Lines Erased: " + linesErased, 10, 100);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game Status");
        GameStatus statusPanel = new GameStatus("Single Player", 1);
        frame.add(statusPanel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Simulate game status updates
        statusPanel.updateGameStatus(2, 1500, 10);
    }
}

