package controller;

import model.Game;

public class GameController {
    private Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void moveBlockLeft() {
        game.moveBlockLeft();
    }

    public void moveBlockRight() {
        game.moveBlockRight();
    }

    public void rotateBlock() {
        game.rotateBlock();
    }

    public void moveBlockDown() {
        game.moveBlockDown();
    }

    public boolean isGameOver() {
        return game.isGameOver();
    }

    public void pauseGame() {
        game.pauseGame();
    }

    public void resumeGame() {
        game.resumeGame();
    }
}
