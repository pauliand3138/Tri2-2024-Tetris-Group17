package test;

import Common.Common;
import model.Block;
import model.BlockInfo;
import model.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.panel.Board;
import view.panel.GameInfoPanel;

import static org.junit.jupiter.api.Assertions.*;

public class TestCases {
    private Board board;

    @BeforeEach
    public void setup() {
        // initialize gameConfig with dummy values for testing
        Common.gameConfig = new GameConfig(
                10, // fieldWidth
                20,     // fieldHeight
                1,      //gameLevel
                true,   // music enabled
                true,   // sound effects enabled
                false,  // extendMode disabled
                0,      // playerOneType (Human)
                0       // playerTwoType (Human)
        );

        //initializing prior to test case
        board = new Board(300, 600, 0, new GameInfoPanel(0, 0));
        board.initializeBlockForTest();
    }

    @Test
    public void testBlockLeftOutOfBounds() {
        Block block = board.getBlock(); // accessing block directly
        block.setX(0); // set block at left edge
        board.moveBlockLeft();
        assertEquals(0, block.getX(), "model.Block should not move left out of the boundary");
    }

    @Test
    public void testBlockRightOutOfBounds() {
        Block block = board.getBlock();
        block.setX(board.getCOL_COUNT() - block.getBlockInfo().getColumns());
        board.moveBlockRight();
        assertEquals(board.getCOL_COUNT() - block.getBlockInfo().getColumns(), block.getX(),
                "model.Block should not move right out of the boundary");
    }

    @Test
    public void testBlockDownOutOfBounds() {
        Block block = board.getBlock();
        block.setY(board.getROW_COUNT() - block.getBlockInfo().getRows());
        board.moveBlockDown();
        assertEquals(board.getROW_COUNT() - block.getBlockInfo().getRows(), (int) block.getY(),
                "model.Block should not move down out of the boundary");
    }

    @Test
    public void testBlockSpawnOutOfBounds() {
        Block block = board.getBlock();
        BlockInfo currentBlockInfo = block.getBlockInfo();
        assertTrue(block.getY() >= -currentBlockInfo.getRows(), "model.Block should spawn within top boundary");
    }

    @Test
    public void testRotationOutOfBounds() {
        Block block = board.getBlock();
        block.setX(board.getCOL_COUNT() - block.getBlockInfo().getColumns());
        board.rotateBlock();
        assertTrue(block.getX() + block.getBlockInfo().getColumns() <= board.getCOL_COUNT(),
                "model.Block rotation should not push the blocks out of bounds");
    }
}
