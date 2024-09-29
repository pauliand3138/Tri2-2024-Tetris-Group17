package test;

import static org.junit.jupiter.api.Assertions.*;
import model.*;
import utilities.SoundEffectManager;
import view.*;
import controller.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

public class TestCases {

    private Game game;
    private GameController gameController;
    private ScoreList scoreList;
    private Configuration config;

    @BeforeEach
    public void setUp() {
        // initialising the necessary classes for testing
        config = new Configuration();
        GameInfo gameInfo = new GameInfo(1, 0);
        Block currentBlock = new Block(new BlockInfo(new int[][]{{1}}, Color.BLUE, 1), 0, 0);
        Block nextBlock = new Block(new BlockInfo(new int[][]{{1}}, Color.BLUE, 1), 1, 0);

        game = new Game(gameInfo, new int[20][10], currentBlock, nextBlock, new Color[20][10]);

        String[] soundFiles = {"path/to/soun1.wav", "path/to/soun2.wav"};
        gameController = new GameController(game, new SoundEffectManager(soundFiles));
        scoreList = new ScoreList();
    }

    // 1. Testing the block movements and rotation
    @Test
    public void testBlockMovement() {
        Block block = game.getCurrentBlock();
        int initialX = block.getX();
        gameController.moveBlockLeft();
        assertEquals(initialX - 1, block.getX(), "Block should move left");

        gameController.moveBlockRight();
        assertEquals(initialX, block.getX(), "Block should move right back to initial position");

        double initialY = block.getY();
        gameController.moveBlockDown();
        assertEquals(initialY + 1, block.getY(), "Block should move down");
    }

    @Test
    public void testBlockRotation() {
        Block block = game.getCurrentBlock();
        int initialRotation = block.getBlockInfo().getCurrentRotation();
        gameController.rotateBlock();
        assertEquals((initialRotation + 1) % 4, block.getBlockInfo().getCurrentRotation(), "Block should rotate correctly");
    }

    // 2. Testing the Collision Detection
    @Test
    public void testCollisionDetection() {
        Block block = game.getCurrentBlock();
        game.setBlockPosition(block, game.getFieldWidth() - block.getBlockInfo().getColumns(), 0);
        gameController.moveBlockRight();
        assertEquals(game.getFieldWidth() - block.getBlockInfo().getColumns(), block.getX(), "Block should not move outside right boundary");
    }

    // 3. Test Row Erasure and Score Update
    @Test
    public void testRowErasureAndScore() {
        // filling up a row to trigger erasure
        for (int i = 0; i < game.getFieldWidth(); i++) {
            game.setBlockPosition(new Block(new BlockInfo(new int[] []{{1}}, Color.BLUE, 1), i,
                    game.getFieldHeight() - 1), i, game.getFieldHeight() - 1);
        }
        int initialScore = game.getScore();
        gameController.clearLines();
        assertEquals(initialScore + config.getPointsPerLine(), game.getScore(),
                "Score should increase by points per line erased");
    }

    // 4. Test Game Over
    @Test
    public void testGameOver() {
        // Stacking blocks till game over (to top of screen)
        for (int y = 0; y < game.getFieldHeight(); y++) {
            for (int x = 0; x < game.getFieldWidth(); x++) {
                game.setBlockPosition(new Block(new BlockInfo(new int[][]{{1}}, Color.BLUE, 1), x, y), x, y);
            }
        }
        assertTrue(game.isGameOver(), "Game should be over when blocks reach the top");
    }

    // 6. Testing Score Management
    @Test
    public void testScoreManagement() {
        Score score = new Score("Player1", 500, game.getGameConfig());
        scoreList.addNewScore(game.getGameConfig(), game.getGameInfo(), "Player1");
        List<Score> scores = scoreList.getScores();
        assertTrue(scores.contains(score), "Score should be added to the high scores list");
    }
}