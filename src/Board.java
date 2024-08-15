import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private final int ROW_COUNT = 20;
    private final int COL_COUNT = 10;
    private int gridCellSize;
    private int[][] board;
    private Block block;

    public Board(int width, int height){
        initialize(width, height);
        setVisible(true);
    }
    private void initialize(int width, int height){
        int boardWidth = (int)(width * 0.35F);
        gridCellSize = boardWidth / COL_COUNT;
        int boardHeight = (int)(gridCellSize * ROW_COUNT);
        setBounds(width / 2 - boardWidth / 2, 0, boardWidth, boardHeight);
        setBorder(BorderFactory.createLoweredBevelBorder());

        board = new int[ROW_COUNT][COL_COUNT];
    }
    private void spawnBlock(){

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}
