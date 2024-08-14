import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private final int ROW_COUNT = 20;
    private final int COL_COUNT = 10;
    private int gridCellSize;
    public Board(int width, int height){
        initialize(width, height);
    }
    public int getBoardHeight(){
        return getHeight();
    }
    private void initialize(int width, int height){
        int boardWidth = (int)(width * 0.35F);
        gridCellSize = boardWidth / COL_COUNT;
        int boardHeight = (int)(gridCellSize * ROW_COUNT);
        int x = width / 2 - boardWidth / 2;
        int y = (int)(height * 0.10F);
        setBounds(x, y, boardWidth, boardHeight);
        setBorder(BorderFactory.createLoweredBevelBorder());
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        // Draw's cell on's grid
        for(int row = 0 ; row < ROW_COUNT ; row++){
            for(int col = 0; col < COL_COUNT ; col++){
                g.drawRect(col * gridCellSize, row * gridCellSize, gridCellSize, gridCellSize);
            }
        }
    }
}
