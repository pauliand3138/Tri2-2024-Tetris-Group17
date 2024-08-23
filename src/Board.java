import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    private final int ROW_COUNT = 20;
    private final int COL_COUNT = 10;
    private final int TETRIS_BLOCK_COUNT = 7;
    private int gridCellSize;
    private int[][] board;
    /*
        Board will store the tetris blocks unique number to keep track of where each was placed.
    */
    private BlockInfo[] blocks;
    /*
        Blocks will store the different types of tetris blocks that can be generated. There are TETRIS_BLOCK_COUNT number of blocks
        each with their own shape, colour, and unique number
    */
    private Block block; // Block is the current block being dropped in the game.
    private Color[][] droppedBlocks;

    private Timer timer;
    private boolean isPaused; // checks if game is paused

    public Board(int width, int height){
        initialize(width, height);
        setVisible(true);
        //startGame(this);
        timer = new Timer(50, this);
        timer.start();
    }

    public void startGame(Board board) {
        new TetrisThread(board).start();
    }

    private void initialize(int width, int height){
        board = new int[ROW_COUNT][COL_COUNT];
        int boardWidth = (int)(width * 0.35F);
        gridCellSize = boardWidth / COL_COUNT;
        int boardHeight = (int)(gridCellSize * ROW_COUNT);

        droppedBlocks = new Color[ROW_COUNT][COL_COUNT];

        setBounds(width / 2 - boardWidth / 2, 0, boardWidth, boardHeight);
        setBorder(BorderFactory.createLoweredBevelBorder());

        blocks = new BlockInfo[TETRIS_BLOCK_COUNT];
        initBlockInfo();
        createTetrisBlock();
    }

    private void createTetrisBlock(){
        Random rand = new Random();
        int index = rand.nextInt(TETRIS_BLOCK_COUNT);
        BlockInfo currentBlockInfo = blocks[index];
        int y = currentBlockInfo.getRows();
        int x = currentBlockInfo.getColumns();
        block = new Block(currentBlockInfo, (COL_COUNT - x) / 2, -y);
        System.out.println(Arrays.deepToString(block.getBlockInfo().getShapes()));
    }

    public void initBlockInfo() {
        blocks[0] = new BlockInfo(new int[][]{{1, 1}, {1, 1}}, Color.yellow, 1); // O block
        blocks[1] = new BlockInfo(new int[][]{{1, 0}, {1, 0},{1, 1}}, Color.orange, 2); // l block
        blocks[2] = new BlockInfo(new int[][]{{0, 1}, {0, 1}, {1, 1}}, new Color(0, 0, 139), 3); // j block
        blocks[3] = new BlockInfo(new int[][]{{1, 1, 1}, {0, 1, 0}}, new Color(128,0,128), 4); // t block
        blocks[4] = new BlockInfo(new int[][]{{0, 1, 1}, {1, 1, 0}}, Color.green, 5); // s block
        blocks[5] = new BlockInfo(new int[][]{{1, 1, 0}, {0, 1, 1}}, Color.red, 6); // z block
        blocks[6] = new BlockInfo(new int[][]{{1, 1, 1, 1}}, Color.cyan, 7);
    }

    public void dropBlockNaturally() {
        if (!isPaused) {
            block.moveDownNaturally();
            repaint();
        }
    }

    public void moveBlockRight() {
        if (!isPaused && !isReachedRight()) {
            block.moveRight();
            repaint();
        }
    }

    public void moveBlockLeft() {
        if (!isPaused && !isReachedLeft()) {
            block.moveLeft();
            repaint();
        }
    }

    public void moveBlockDown() {
        if (!isPaused && !isReachedBottom()) {
            block.moveDown();
            repaint();
        }
    }

    public void rotateBlock() {
        if (!isPaused) {
            block.rotate();
            if (block.getX() < 0) block.setX(0);
            if (block.getX() + block.getBlockInfo().getColumns() >= COL_COUNT)
                block.setX(COL_COUNT - block.getBlockInfo().getColumns());
            if ((int) (block.getBlockInfo().getRows() + block.getY()) >= ROW_COUNT)
                block.setY(ROW_COUNT - block.getBlockInfo().getRows());
            repaint();
        }
    }

    private boolean isReachedRight() {
        if (block.getX() + block.getBlockInfo().getColumns() == COL_COUNT) return true;

        int[][] shape = block.getBlockInfo().getShape();
        int width = block.getBlockInfo().getColumns();
        int height = block.getBlockInfo().getRows();

        for (int row = 0; row < height; row++) {
            for (int col = width - 1; col >= 0; col--) {
                if(shape[row][col] != 0) {
                    int x = col + block.getX() + 1;
                    int y = row + (int)Math.floor(block.getY());
                    if (y < 0) break; // when the block still hasn't entered the frame
                    if (droppedBlocks[y][x] != null) return true;
                    break;
                }
            }
        }

        return false;
    }

    private boolean isReachedLeft() {
        if (block.getX() == 0) return true;

        int[][] shape = block.getBlockInfo().getShape();
        int width = block.getBlockInfo().getColumns();
        int height = block.getBlockInfo().getRows();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if(shape[row][col] != 0) {
                    int x = col + block.getX() - 1;
                    int y = row + (int)Math.floor(block.getY());
                    if (y < 0) break; // when the block still hasn't entered the frame
                    if (droppedBlocks[y][x] != null) return true;
                    break;
                }
            }
        }

        return false;
    }

    private boolean isReachedBottom() {
        if ((int)(block.getBlockInfo().getRows() + block.getY()) == ROW_COUNT) {
            return true;
        }

        int[][] shape = block.getBlockInfo().getShape();
        int width = block.getBlockInfo().getColumns();
        int height = block.getBlockInfo().getRows();

        for (int col = 0; col < width; col++) {
            for (int row = height - 1; row >= 0; row--) {
                if(shape[row][col] != 0) {
                    int x = col + block.getX();
                    int y = row + (int)Math.floor(block.getY()) + 1;
                    if (y < 0) break; // when the block still hasn't entered the frame
                    if (droppedBlocks[y][x] != null) return true;
                    break;
                }
            }
        }

        return false;
    }

    private void setBlockAsDroppedBlock(){
        int[][] shape = block.getBlockInfo().getShape();
        int height = block.getBlockInfo().getRows();
        int width = block.getBlockInfo().getColumns();
        int x = block.getX();
        int y = (int)block.getY();
        Color color = block.getBlockInfo().getColour();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1) {
                    droppedBlocks[row + y][col + x] = color;
                }
            }
        }
    }

    private void drawTetrisBlock(Graphics g){
        // rows and cols start at zero
        BlockInfo blockInfo = block.getBlockInfo();

        for(int row = 0 ; row < blockInfo.getRows() ; row++){
            for(int col = 0; col < blockInfo.getColumns() ; col++){
                if(blockInfo.getShape()[row][col] != 0){
                    int x = (block.getX() + col) * gridCellSize;
                    double y = (block.getY() + row) * gridCellSize;

                    g.setColor(blockInfo.getColour());
                    g.fillRect(x, (int)y, gridCellSize, gridCellSize);
                }
            }
        }
    }

    private void drawDroppedBlocks(Graphics g) {
        Color color;

        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                color = droppedBlocks[row][col];

                if (color != null) {
                    int x = col * gridCellSize;
                    double y = row * gridCellSize;

                    g.setColor(color);
                    g.fillRect(x, (int)y, gridCellSize, gridCellSize);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawDroppedBlocks(g);
        drawTetrisBlock(g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            if (!isReachedBottom()) {
                dropBlockNaturally();
            } else {
                setBlockAsDroppedBlock();
                createTetrisBlock();
            }
            repaint();
        }
    }

    public synchronized void pauseGame() {
        isPaused = true;
        timer.stop();
    }

    public synchronized void resumeGame() {
        isPaused = false;
        timer.start();
    }
}