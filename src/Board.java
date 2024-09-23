import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    private final int ROW_COUNT = Common.gameConfig.getFieldHeight();
    private final int COL_COUNT = Common.gameConfig.getFieldWidth();
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

    private final Timer timer;
    public boolean isPaused; // checks if game is paused
    public boolean isGameOver = false;

    private ExternalPlayerController externalPlayerController;

    public Board(int width, int height){
        initialize(width, height);
        setVisible(true);
        timer = new Timer(50, this);
        timer.start();

        // Initializing external player controller for external player mode
        if (Common.gameConfig.getPlayerOneType() == 2) { // External Player
            try {
                externalPlayerController = new ExternalPlayerController("localhost", 3000);
            } catch (IOException e) {
                System.out.println("Failed to connect to external player.");
                isPaused = true; // Pausing the game if connection fails
            }
        }
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

    public boolean isBlockOutside() {
        if (block.getY() < 0) {
            block = null;
            return true;
        }
        return false;
    }

    public void dropBlockNaturally() {
        if (block != null && !isPaused) {
            block.moveDownNaturally();
            repaint();
        }
    }

    public void moveBlockRight() {
        if (block != null && !isPaused && !isReachedRight()) {
            block.moveRight();
            repaint();
        }
    }

    public void moveBlockLeft() {
        if (block != null && !isPaused && !isReachedLeft()) {
            block.moveLeft();
            repaint();
        }
    }

    public void moveBlockDown() {
        if (block != null && !isPaused && !isReachedBottom()) {
            block.moveDown();
            repaint();
        }
    }

    public void rotateBlock() {
        if (block != null && !isPaused) {
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

    public void clearLines() {
        boolean completedLine;
        
        for (int row = ROW_COUNT - 1; row >= 0; row--) {
            completedLine = true;

            for (int col = 0; col < COL_COUNT; col++) {
                if (droppedBlocks[row][col] == null) {
                    completedLine = false;
                    break;
                }
            }

            if (completedLine) {
                clearLine(row);
                moveLinesDown(row);
                clearLine(0);
                row++;
                repaint();
                Play.soundManager.playSound("clearline.wav");
            }
        }
    }

    private void moveLinesDown(int row) {
        for(int r = row; r > 0; r-- ) {
            for (int col = 0; col < COL_COUNT; col++) {
                droppedBlocks[r][col] = droppedBlocks[r-1][col];
            }
        }
    }

    private void clearLine(int row) {
        for(int i = 0; i < COL_COUNT; i++) {
            droppedBlocks[row][i] = null;
        }
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
                if (shape[row][col] == 1 && row + y >= 0 && col + x >= 0 ) {
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
        if (block!= null) {
            drawTetrisBlock(g);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
                if (!isReachedBottom()) {
                    dropBlockNaturally();
                } else {
                    setBlockAsDroppedBlock();
                    clearLines();
                    if (isBlockOutside()) {
                        isGameOver = true;
                        System.out.println("Game Over");
                        timer.stop();
                        Play.soundManager.playSound("gameover.wav");
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e1) {

                            Play.soundManager.close();
                        }
                    } else {
                        createTetrisBlock();
                    }
                }
                repaint();
        }
    }

    private void processCommand(String command) {
        switch (command) {
            case "MOVE_LEFT":
                moveBlockLeft();
                break;
            case "MOVE_RIGHT":
                moveBlockRight();
                break;
            case "ROTATE":
                rotateBlock();
                break;
            case "DROP":
                moveBlockDown();
                break;
            default:
                System.out.println("Unknown command " + command);
                break;
        }
    }

    public Block getBlock() {
        return block;
    }

    public int getROW_COUNT() {
        return ROW_COUNT;
    }

    public int getCOL_COUNT() {
        return COL_COUNT;
    }

    public void pauseGame() {
        isPaused = true;
        timer.stop();
    }

    public void initializeBlockForTest() {
        createTetrisBlock();
    }

    public void resumeGame() {
        isPaused = false;
        timer.start();
    }
}