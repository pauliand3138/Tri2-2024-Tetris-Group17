import javax.swing.*;
import java.awt.*;


public class Play extends JPanel {
    public Play(int width, int height){
        initialize(width, height);
        addElements();
    }
    private void initialize(int width, int height){
        setSize(new Dimension(width, height));
        setLayout(null);
    }
    private void addElements(){
        int width = 500;
        int height = 500;
        int halfWidth = width / 2;
        int x, y;

        // Play Title Label
        JLabel playLabel = new JLabel("Play");
        playLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playLabel.setBounds(halfWidth - 25, 10, 50, 25);
        add(playLabel);

        // Create board
        Board board = new Board();
        int boardWidth = (int) (width * 0.35F);
        int boardHeight = (int) (height * 0.70F);
        x = halfWidth - boardWidth / 2;
        y = (int) (0.10F * height);
        add(board);
        board.setBounds(x, y, boardWidth, boardHeight);
        board.setVisible(true);
    }
}
