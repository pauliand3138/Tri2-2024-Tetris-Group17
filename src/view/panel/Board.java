package view.panel;

import Common.Common;
import controller.AIController;
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

import static Common.Common.gameConfig;

public class Board extends JPanel implements ActionListener {
    private final int ROW_COUNT = gameConfig.getFieldHeight();
    private final int COL_COUNT = gameConfig.getFieldWidth();
    private final int TETRIS_BLOCK_COUNT = 7;
    private int gridCellSize;
    public int[][] board;
    private BlockInfo[] blocks;
    public Block block; // model.Block is the current block being dropped in the game.
    public Block nextBlock;
    public Color[][] droppedBlocks;
    public boolean isBoardActive = true;
    private final int playerNum;
    public Timer timer = null;
    public Timer AIdownTimer = null;
    public Timer ExternalPlayerTimer = null;
    public boolean isPaused; // checks if game is paused
    public boolean isGameOver = false;
    public boolean isAI = false;
    public boolean isExternalPlayer = false;
    private long currentBlockSeed = Common.gameSeed;
    private GameInfoPanel gameInfoPanel;
    private GameInfo gameInfo;
    public AIController aiController;
    public ExternalPlayerController externalPlayerController;
    public Game game;

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
        int playerType = playerNum == 0 ? gameConfig.getPlayerOneType() : gameConfig.getPlayerTwoType();
        gameInfo = gameInfoPanel.getGameInfo();
        game = new Game(gameInfo, board, block, nextBlock, droppedBlocks);
        isAI = gameInfo.getPlayerType() == 1;
        isExternalPlayer = gameInfo.getPlayerType() == 2;
        if (isAI) {
            aiController = new AIController();
            moveBlockDownAI();
        }
        if (isExternalPlayer) {
            externalPlayerController = new ExternalPlayerController("localhost", 3000);
            try {
                moveBlockExternalPlayer();
            } catch (IOException e) {
                System.out.println("Failed to connect to external player.");
                Play.isConnectionError = true;
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
            Play.gameController[playerNum].getGame().setCurrentBlock(block);
            drawTetrisBlock(g);
        }
        if (nextBlock != null) {
            Play.gameController[playerNum].getGame().setNextBlock(nextBlock);
            gameInfoPanel.nextBlockPanel.setBlock(nextBlock);
            gameInfoPanel.nextBlockPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            if (!Play.gameController[playerNum].getGame().isReachedBottom()) {
                Play.gameController[playerNum].getGame().dropBlockNaturally();
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
                Play.gameController[playerNum].getGame().setBlockAsDroppedBlock();
                Play.gameController[playerNum].getGame().clearLines();
                gameInfoPanel.repaint();
                Play.gameController[playerNum].getGame().updateBoardArray();
                if (Play.gameController[playerNum].getGame().isBlockOutside()) {
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
                    if (isAI && AIdownTimer != null)
                        AIdownTimer.stop();
                    if (isExternalPlayer && ExternalPlayerTimer != null)
                        ExternalPlayerTimer.stop();
                    currentBlockSeed++;
                    createTetrisBlock();
                    if (isAI) {
                        updateGameValue();
                        moveBlockDownAI();
                    }
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
    /** Unit Test START **/
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

    public void resumeGame() {
        isPaused = false;
        timer.start();
    }

    public void initializeBlockForTest() {
        createTetrisBlock();
    }

    /** Unit Test END **/

    public void moveBlockDownAI() {
        Move bestMove = aiController.findBestMove(board, block);

        for (int i = 0; i < bestMove.getRotation() + 1; i++) {
            game.rotateBlock();
            Play.soundManager.playSound("move.wav");
        }

        int leftCounter = 0;
        int loopBreaker1 = 0;
        while (block.getX() > bestMove.getCol() && leftCounter < 2 && loopBreaker1 < gameConfig.getFieldWidth() / 2) {
            game.moveBlockLeft();
            Play.soundManager.playSound("move.wav");
            if (Math.abs(bestMove.getCol() - block.getX()) == 1) {
                leftCounter++;
            }
            loopBreaker1++;
        }

        int rightCounter = 0;
        int loopBreaker2 = 0;
        while (block.getX() < bestMove.getCol() && rightCounter < 2 && loopBreaker2 < gameConfig.getFieldWidth() / 2) {
            game.moveBlockRight();
            Play.soundManager.playSound("move.wav");
            if (Math.abs(bestMove.getCol() - block.getX()) == 1) {
                rightCounter++;
            }
            loopBreaker2++;
        }
        if (AIdownTimer == null || !AIdownTimer.isRunning()) {
            AIdownTimer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (block != null || !isGameOver || game.isBlockOutside()) {
                        game.moveBlockDown();
                        repaint();
                    } else {
                        AIdownTimer.stop();
                    }
                }
            });
            AIdownTimer.start();
        }

    }
//
    public void moveBlockExternalPlayer() throws IOException {
        externalPlayerController.establishConnection();

        PureGame puregame = new PureGame(gameConfig.getFieldHeight(), gameConfig.getFieldWidth());
        puregame.setCells(board);
        puregame.setCurrentShape(block.getBlockInfo().getShape());
        puregame.setNextShape(nextBlock.getBlockInfo().getShape());

        OpMove externalMove = externalPlayerController.getNewMove(puregame);

        int opX = externalMove.getOpX();
        int opRotate = externalMove.getOpRotate();

        if (opX == -1 || opRotate == -1) {
            isGameOver = true;
            return;
        }
        final int[] opRotateRef = {opRotate};

        if (ExternalPlayerTimer == null || !ExternalPlayerTimer.isRunning()) {
            ExternalPlayerTimer = new Timer(100, e -> {
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
                            game.moveBlockDown();
                        }
                    }
                } else {
                    ExternalPlayerTimer.stop();
                }
            });
            ExternalPlayerTimer.start();
        }
    }

    private void updateGameValue() {
        game.setCurrentBlock(block);
        game.setNextBlock(nextBlock);
        game.setBoard(board);
        game.setDroppedBlocks(droppedBlocks);
    }
}