import javax.swing.*;
import java.awt.*;


public class Play extends JPanel {
    public Play(int width, int height){
        initialize(width, height);
        addElements();
    }
    private void initialize(int width, int height){
        setSize(new Dimension(width, height));
        setLayout(new BorderLayout());
    }
    private void addElements(){
        int width = 500;
        int height = 500;
        int halfWidth = width / 2;

        float titlePanelPct = 0.30F;
        float boardPanelPct = 0.60F;
        float creatorsPanelPct = 1 - boardPanelPct - titlePanelPct;

        int titlePanelHeight = (int)(height * titlePanelPct);
        JPanel titlePanel = new JPanel();
        titlePanel.setSize(width, titlePanelHeight);
        JLabel titleLabel = new JLabel("Play");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 25));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        int boardPanelHeight = (int)(height * boardPanelPct);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setSize(width, boardPanelHeight);
        Board board = new Board(width, boardPanelHeight);
        boardPanel.add(board);
        add(boardPanel, BorderLayout.CENTER);

        int creatorsPanelHeight = (int)(height * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setSize(width, creatorsPanelHeight);
        JLabel creatorsLabel = new JLabel("Creators: Paul Ian Lim, Karan Singde, Darcy McIntosh, Janet Chimwayange and Kacey Boyle.", SwingConstants.CENTER);
        creatorsLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        creatorsPanel.add(creatorsLabel);
        add(creatorsPanel, BorderLayout.SOUTH);
    }
}
