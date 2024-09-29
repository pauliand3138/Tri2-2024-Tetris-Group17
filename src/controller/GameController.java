package controller;

import model.Game;
import utilities.SoundEffectManager;

public class GameController {
    private final Game game;
    private final SoundEffectManager soundManager;

    public GameController(Game game, SoundEffectManager soundManager) {
        this.game = game;
        this.soundManager = soundManager;
    }

    public Game getGame() {
        return game;
    }

    public void moveBlockLeft() {
        game.moveBlockLeft();
        soundManager.playSound("move.wav");
    }

    public void moveBlockRight() {
        game.moveBlockRight();
        soundManager.playSound("move.wav");
    }

    public void rotateBlock() {
        game.rotateBlock();
        soundManager.playSound("move.wav");
    }

    public void moveBlockDown() {
        game.moveBlockDown();
    }

    public void clearLines() {
        if(game.clearLines()) {
            soundManager.playSound("clearline.wav");
        }
    }

    public boolean isGameOver() {
        return game.isGameOver();
    }

    public void gameOver() {
        soundManager.playSound("gameover.wav");
    }

    public void pauseGame() {
        game.pauseGame();
    }

    public void resumeGame() {
        game.resumeGame();
    }
}

