import javax.swing.*;
import java.awt.*;

public class NextBlockPanel extends JPanel {
    private Block block;

    public NextBlockPanel() {
        setPreferredSize(new Dimension(95, 65));
        setBorder(BorderFactory.createBevelBorder(1));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (block != null) {
            drawBlock(g2d);
        }
    }

    private void drawBlock(Graphics2D g2d) {
        int[][] shape = this.block.getBlockInfo().getShape();
        int gridCellSize = 16;
        int x = gridCellSize * (6 - (shape[0]).length) / 2;
        int y = gridCellSize * (4 - shape.length) / 2;
        g2d.setColor(this.block.getBlockInfo().getColour());
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < (shape[0]).length; c++) {
                if (shape[r][c] == 1)
                    g2d.fillRect(c * gridCellSize + x, r * gridCellSize + y, gridCellSize, gridCellSize);
            }
        }
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
