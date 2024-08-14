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

        JLabel playLabel = new JLabel("Play");
        playLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playLabel.setBounds(halfWidth - 25, 10, 50, 25);
        add(playLabel);

        Board board = new Board(width, height);
        add(board);
        board.setVisible(true);

        /*
        JLabel authorsLabel = new JLabel("Authors: Paul Ian Lim, Karan Singde, Janet Chimwayange, Darcy McIntosh and Kacey Boyle.");
        authorsLabel.setFont(new Font("Arial", Font.BOLD, 10));
        Dimension authorsLabelDimension = authorsLabel.getPreferredSize();
        authorsLabel.setBounds(width - authorsLabelDimension.width, y + board.getBoardHeight() + 15, authorsLabelDimension.width, authorsLabelDimension.height);
        add(authorsLabel);
         */
    }
}
