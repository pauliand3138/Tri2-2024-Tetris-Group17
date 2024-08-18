import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private Timer timer;

    public Board(int width, int height){
        initialize(width, height);
        setVisible(true);
        startGame(this);
        timer = new Timer(30, this);
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
        setBounds(width / 2 - boardWidth / 2, 0, boardWidth, boardHeight);
        setBorder(BorderFactory.createLoweredBevelBorder());

        blocks = new BlockInfo[TETRIS_BLOCK_COUNT];
        blocks[0] = new BlockInfo(new int[][]{{1, 1}, {1, 1}}, Color.yellow, 1); // O block
        blocks[1] = new BlockInfo(new int[][]{{1, 0}, {1, 0},{1, 1}}, Color.orange, 2); // l block
        blocks[2] = new BlockInfo(new int[][]{{0, 1}, {0, 1}, {1, 1}}, new Color(0, 0, 139), 3); // j block
        blocks[3] = new BlockInfo(new int[][]{{1, 1, 1}, {0, 1, 0}}, new Color(128,0,128), 4); // t block
        blocks[4] = new BlockInfo(new int[][]{{0, 1, 1}, {1, 1, 0}}, Color.green, 5); // s block
        blocks[5] = new BlockInfo(new int[][]{{1, 1, 0}, {0, 1, 1}}, Color.red, 6); // z block
        blocks[6] = new BlockInfo(new int[][]{{1},{1},{1},{1}}, Color.cyan, 7);

        createTetrisBlock();
    }
    private void createTetrisBlock(){
        Random rand = new Random();
        int index = rand.nextInt(TETRIS_BLOCK_COUNT);
        int x = (int)(COL_COUNT / 2) - 1; // Subtract 1 to make the block more centered
        block = new Block(blocks[index], x, 0);
    }

    public void dropBlock() {
        block.moveDown();
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
    private int findTetrisBlock(int blockNumber){
        for(int i = 0; i < TETRIS_BLOCK_COUNT ; i++){
            if(blocks[i].getNumber() == blockNumber){
                return i;
            }
        }
        return -1;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawTetrisBlock(g);
        for(int row = 0; row < ROW_COUNT; row++){
            for(int col = 0; col < COL_COUNT ; col++){
                if(board[row][col] != 0){ // if there is a block on the board draw it to the screen
                    int index = findTetrisBlock(board[row][col]);
                    g.setColor(blocks[index].getColour()); // Need to set the colour of the block
                    g.fillRect(row * gridCellSize, col * gridCellSize, gridCellSize, gridCellSize);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dropBlock();
        repaint();
    }
}