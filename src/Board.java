import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel {
    private final int ROW_COUNT = 20;
    private final int COL_COUNT = 10;
    private final int TETRIS_BLOCK_COUNT = 7;
    private int gridCellSize;
    private int[][] board;
    private BlockInfo[] blocks;
    private Block block;

    public Board(int width, int height){
        initialize(width, height);
        setVisible(true);
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
    }
    private void spawnBlock(){
        Random rand = new Random();
        int index = rand.nextInt(TETRIS_BLOCK_COUNT);
        block = new Block(blocks[index]);
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
}