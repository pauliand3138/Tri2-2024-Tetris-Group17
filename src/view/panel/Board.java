package view.panel;

import Common.Common;
import controller.BoardEvaluator;
import controller.ExternalPlayerController;
import model.*;
import view.Play;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    private final int ROW_COUNT = Common.gameConfig.getFieldHeight();
    private final int COL_COUNT = Common.gameConfig.getFieldWidth();
    private final int TETRIS_BLOCK_COUNT = 7;
    private int gridCellSize;
    int[][] board;
    private BlockInfo[] blocks;
    public Block block; // model.Block is the current block being dropped in the game.
    public Block nextBlock;
    private Color[][] droppedBlocks;
    public boolean isBoardActive = true;
    private final int playerNum;
    public Timer timer = null;
    public Timer AIdownTimer = null;
    public Timer ExternalPlayerTimer = null;
    public boolean isPaused; // checks if game is paused
    public boolean isGameOver = false;
    public boolean isAI = false;
    public boolean isExternalPlayer = false;
    public BoardEvaluator evaluator = new BoardEvaluator();
    private long currentBlockSeed = Common.gameSeed;
    private GameInfoPanel gameInfoPanel;
    private GameInfo gameInfo;
    //private final model.PureGame pureGame = new model.PureGame(Common.gameConfig.getFieldHeight(), Common.gameConfig.getFieldWidth());
    //private model.OpMove externalMove;
    public ExternalPlayerController externalPlayerController;
    //private Socket socket;
    //private BufferedReader in;
    //private PrintWriter out;
    // model.OpMove move;

    public Board(int width, int height, int playerNum, GameInfoPanel gameInfoPanel){
        this.playerNum = playerNum;
        this.gameInfoPanel = gameInfoPanel;
        initialize(width, height);
        setVisible(true);
        timer = new Timer(50, this);
        timer.start();
    }

    private void initialize(int width, int height) {
        board = new int[ROW_COUNT][COL_COUNT];
        int boardWidth = (int)(width * 0.35F);
        gridCellSize = 17;
        int boardHeight = (int)(gridCellSize * ROW_COUNT);
        droppedBlocks = new Color[ROW_COUNT][COL_COUNT];
        setBounds(0, 0, boardWidth, boardHeight);
        setBorder(BorderFactory.createLoweredBevelBorder());
        blocks = new BlockInfo[TETRIS_BLOCK_COUNT];
        initBlockInfo();
        createTetrisBlock();
        int playerType = playerNum == 0 ? Common.gameConfig.getPlayerOneType() : Common.gameConfig.getPlayerTwoType();
        gameInfo = gameInfoPanel.getGameInfo();
        isAI = gameInfo.getPlayerType() == 1;
        isExternalPlayer = gameInfo.getPlayerType() == 2;
        if (isAI)
            moveBlockDownAI();
        if (isExternalPlayer) {
            externalPlayerController = new ExternalPlayerController("localhost", 3000);
            try {
                moveBlockExternalPlayer();
            } catch (IOException e) {
                System.out.println("Failed to connect to external player.");
                Play.isConnectionError = true;
                //throw new RuntimeException(e);
            }
        }
    }

    private void createTetrisBlock(){
        Random rand = new Random(currentBlockSeed);
        int index = rand.nextInt(TETRIS_BLOCK_COUNT);
        BlockInfo currentBlockInfo = new BlockInfo(blocks[index].getShape(), blocks[index].getColour(), blocks[index].getNumber());
        int y = currentBlockInfo.getRows();
        int x = currentBlockInfo.getColumns();
        block = new Block(currentBlockInfo, (COL_COUNT - x) / 2, -y);

        Random nextRand = new Random(currentBlockSeed + 1);
        int nextIndex = nextRand.nextInt(TETRIS_BLOCK_COUNT);
        BlockInfo nextBlockInfo = new BlockInfo(blocks[nextIndex].getShape(), blocks[nextIndex].getColour(), blocks[nextIndex].getNumber());
        nextBlock  = new Block(nextBlockInfo, 0, 0);
        //System.out.println(Arrays.deepToString(block.getBlockInfo().getShapes()));
    }

    public void initBlockInfo() {
        blocks[0] = new BlockInfo(new int[][]{{1, 1}, {1, 1}}, Color.yellow, 1); // O block
        blocks[1] = new BlockInfo(new int[][]{{1, 0}, {1, 0},{1, 1}}, Color.orange, 2); // l block
        blocks[2] = new BlockInfo(new int[][]{{0, 1}, {0, 1}, {1, 1}}, new Color(0, 0, 139), 3); // j block
        blocks[3] = new BlockInfo(new int[][]{{0, 1, 0}, {1, 1, 1}}, new Color(128,0,128), 4); // t block
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
            block.moveDownNaturally(gameInfo.getCurrLevel());
            repaint();
        }
    }

    public void moveBlockRight() {
        if (block != null && !isPaused && !isReachedRight()) {
            block.moveRight();
            Play.soundManager.playSound("move.wav");
            repaint();
        }
    }

    public void moveBlockLeft() {
        if (block != null && !isPaused && !isReachedLeft()) {
            block.moveLeft();
            Play.soundManager.playSound("move.wav");
            repaint();
        }
    }

    public void moveBlockDown(){
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
            Play.soundManager.playSound("move.wav");
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
        boolean completedLine = true;
        boolean shouldPlaySound = false;
        int linesCleared = 0;
        for (int row = ROW_COUNT - 1; row >= 0; row--) {
            completedLine = true;

            for (int col = 0; col < COL_COUNT; col++) {
                if (droppedBlocks[row][col] == null) {
                    completedLine = false;
                    break;
                }
            }

            if (completedLine) {
                shouldPlaySound = true;
                linesCleared++;
                clearLine(row);
                moveLinesDown(row);
                clearLine(0);
                row++;
                repaint();
            }
        }

        if (shouldPlaySound) {
            Play.soundManager.playSound("clearline.wav");
        }

        if (linesCleared == 1) {
            gameInfo.setScore(gameInfo.getScore() + 100);
        } else if (linesCleared == 2) {
            gameInfo.setScore(gameInfo.getScore() + 300);
        } else if (linesCleared == 3) {
            gameInfo.setScore(gameInfo.getScore() + 600);
        } else if (linesCleared == 4) {
            gameInfo.setScore(gameInfo.getScore() + 1000);
        }
        int currentLineErased = gameInfo.getLineErased();
        gameInfo.setLineErased(gameInfo.getLineErased() + linesCleared);
        int newLineErased = gameInfo.getLineErased();
        if (newLineErased / 10 != currentLineErased / 10) {
            gameInfo.setCurrLevel(gameInfo.getCurrLevel() + 1);
        }
        gameInfoPanel.repaint();
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
                    board[row][col] = 1;
                    int x = col * gridCellSize;
                    double y = row * gridCellSize;
                    g.setColor(color);
                    g.fillRect(x, (int)y, gridCellSize, gridCellSize);
                } else {
                    board[row][col] = 0;
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
        if (nextBlock != null) {
            gameInfoPanel.nextBlockPanel.setBlock(nextBlock);
            gameInfoPanel.nextBlockPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            if (!isReachedBottom()) {
                dropBlockNaturally();
                if (!isBoardActive) {
                    timer.stop();
                    timer = null;
                    if (isAI) {
                        AIdownTimer.stop();
                        AIdownTimer = null;
                    }
                    if (isExternalPlayer) {
                        ExternalPlayerTimer.stop();
                        ExternalPlayerTimer = null;
                    }
                }
            } else {
                setBlockAsDroppedBlock();
                clearLines();
                updateBoardArray();
                if (isBlockOutside()) {
                    if (isAI)
                        AIdownTimer.stop();
                    if (isExternalPlayer)
                        ExternalPlayerTimer.stop();
                    isGameOver = true;
                    System.out.println("Game Over");
                    timer.stop();
                    Play.soundManager.playSound("gameover.wav");
                    if (Play.isAllGameEnded()) {
                        try {
                            Thread.sleep(100);
                            Play.soundManager.close();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    if (isAI && AIdownTimer.isRunning())
                        AIdownTimer.stop();
                    if (isExternalPlayer && ExternalPlayerTimer != null && ExternalPlayerTimer.isRunning())
                        ExternalPlayerTimer.stop();
                    currentBlockSeed++;
                    createTetrisBlock();
                    if (isAI)
                        moveBlockDownAI();
                    if (isExternalPlayer) {
                        try {
                            moveBlockExternalPlayer();
                        } catch (IOException ex) {
                            System.out.println("No connection to external player after block drop.");
                        }
                    }
                }
            }
            repaint();
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

    public void updateBoardArray() {
        for(int row = 0; row < ROW_COUNT; row++) {
            for(int col = 0; col < COL_COUNT; col++) {
                if (droppedBlocks[row][col] != null) {
                    board[row][col] = 1;
                } else {
                    board[row][col] = 0;
                }
            }
        }
    }
    // AI Code
    public Move findBestMove(int[][] board, Block block) {
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (int rotation = 0; rotation < 4; rotation++) {
            for(int col = 0; col < Common.gameConfig.getFieldWidth(); col++) {
                Block simulatedBlock = new Block(block);
                simulatedBlock.rotate();

                int[][] simulatedBoard = simulateDrop(board, simulatedBlock, col);
                int score = evaluator.evaluateBoard(simulatedBoard);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Move(col, rotation);
                }
            }
        }

        return bestMove;
    }

    private int[][] simulateDrop(int[][] board, Block block, int col) {
        int[][] simulatedBoard = copyBoard(board);
        int dropRow = getDropRow(simulatedBoard, block, col);
        placePiece(simulatedBoard, block, col, dropRow);
        return simulatedBoard;
    }

    private int getDropRow(int[][] board, Block block, int col) {
        int row = 0;
        while (canPlacePiece(board, block, col, row)) {
            row++;
        }
        return row - 1;
    }

    private boolean canPlacePiece(int[][] board, Block block, int testCol, int testRow) {
        int[][] shape = block.getBlockInfo().getShape();
        int height = block.getBlockInfo().getRows();
        int width = block.getBlockInfo().getColumns();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1) {
                    int x = col + testCol;
                    int y = row + testRow;
                    if (x < 0 || x >= board[0].length || y < 0 || y >= board.length || board[y][x] != 0)  {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placePiece(int[][] board, Block block, int testCol, int testRow) {
        int[][] shape = block.getBlockInfo().getShape();
        int height = block.getBlockInfo().getRows();
        int width = block.getBlockInfo().getColumns();
        int x = block.getX() + testCol;
        int y = (int)block.getY() + testRow;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1 && row + y >= 0 && col + x >= 0 && row + y < board.length && col + x < board[row].length) {
                    board[row + y][col + x] = 1;
                }
            }
        }
    }


    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            System.arraycopy(board[y], 0, newBoard[y], 0, board[y].length);
        }
        return newBoard;
    }

    public void moveBlockDownAI(){
        Move bestMove = findBestMove(board, block);

        for(int i = 0; i < bestMove.getRotation() + 1; i++) {
            rotateBlock();
            Play.soundManager.playSound("move.wav");
        }

        int leftCounter = 0;
        int loopBreaker1 = 0;
        while (block.getX() > bestMove.getCol() && leftCounter < 2 && loopBreaker1 < Common.gameConfig.getFieldWidth() / 2) {
            //System.out.println("model.Block X: " + block.getX());
            moveBlockLeft();
            Play.soundManager.playSound("move.wav");
            if (Math.abs(bestMove.getCol() - block.getX()) == 1) {
                leftCounter++;
            }
            loopBreaker1++;
        }

        int rightCounter = 0;
        int loopBreaker2 = 0;
        while (block.getX() < bestMove.getCol() && rightCounter < 2 && loopBreaker2 < Common.gameConfig.getFieldWidth() / 2) {
            //System.out.println("model.Block X: " + block.getX());
            //System.out.println(bestMove.getCol());
            moveBlockRight();
            Play.soundManager.playSound("move.wav");
            if (Math.abs(bestMove.getCol() - block.getX()) == 1) {
                rightCounter++;
            }
            loopBreaker2++;
        }

        AIdownTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (block != null || !isGameOver || isBlockOutside()) {
                    moveBlockDown();
                } else {
                    AIdownTimer.stop();
                }
            }
        });
        AIdownTimer.start();
    }

    public void moveBlockExternalPlayer() throws IOException {
        externalPlayerController.establishConnection();

        PureGame game = new PureGame(Common.gameConfig.getFieldHeight(), Common.gameConfig.getFieldWidth());
        game.setCells(board);
        game.setCurrentShape(block.getBlockInfo().getShape());
        game.setNextShape(nextBlock.getBlockInfo().getShape());

        OpMove externalMove = externalPlayerController.getNewMove(game);

        int opX = externalMove.getOpX();
        int opRotate = externalMove.getOpRotate();

        if (opX == -1 || opRotate == -1) {
            isGameOver = true;
            return;
        }
        final int[] opRotateRef = {opRotate};
        ExternalPlayerTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    if (block != null) {
                        if (opRotateRef[0] > 0) {
                            block.rotate();
                            opRotateRef[0]--;
                            Play.soundManager.playSound("move.wav");
                        } else if (block.getX() < opX) {
                            block.moveRight();
                            Play.soundManager.playSound("move.wav");
                        } else if (block.getX() > opX) {
                            block.moveLeft();
                            Play.soundManager.playSound("move.wav");
                        } else {
                            moveBlockDown();
                        }
                    }
                } else {
                    ExternalPlayerTimer.stop();
                }
            }
        });
        ExternalPlayerTimer.start();
    }

}