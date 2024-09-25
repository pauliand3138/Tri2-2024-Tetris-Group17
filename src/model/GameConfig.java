package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class GameConfig {
    private int fieldWidth;
    private int fieldHeight;
    private int gameLevel;
    private boolean music;
    private boolean soundEffect;
    private boolean extendMode;
    private int playerOneType;
    private int playerTwoType;
    // playerType [0] = Human, [1] = AI, [2] = External

    public GameConfig() {
    }

    public GameConfig(int fieldWidth, int fieldHeight, int gameLevel, boolean music, boolean soundEffect, boolean extendMode, int playerOneType, int playerTwoType) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.gameLevel = gameLevel;
        this.music = music;
        this.soundEffect = soundEffect;
        this.extendMode = extendMode;
        this.playerOneType = playerOneType;
        this.playerTwoType = playerTwoType;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(boolean soundEffect) {
        this.soundEffect = soundEffect;
    }

    public boolean isExtendMode() {
        return extendMode;
    }

    public void setExtendMode(boolean extendMode) {
        this.extendMode = extendMode;
    }

    public int getPlayerOneType() {
        return playerOneType;
    }

    public void setPlayerOneType(int playerOneType) {
        this.playerOneType = playerOneType;
    }

    public int getPlayerTwoType() {
        return playerTwoType;
    }

    public void setPlayerTwoType(int playerTwoType) {
        this.playerTwoType = playerTwoType;
    }

    public void resetGameConfig() {
        this.fieldWidth = 10;
        this.fieldHeight = 20;
        this.gameLevel = 1;
        this.music = true;
        this.soundEffect = true;
        this.extendMode = false;
        this.playerOneType = 0;
        this.playerTwoType = 0;
    }

    public GameConfig readGameConfigFile() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader br = new BufferedReader(new FileReader("TetrisGameConfig.json"));
        GameConfig gameConfig = gson.fromJson(br, GameConfig.class);
        return gameConfig;
    }

    public void writeGameConfigFile(GameConfig gameConfig) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter fw = new FileWriter("TetrisGameConfig.json");
        fw.write(gson.toJson(gameConfig));
        fw.close();
    }

    @Override
    public String toString() {
        return "model.GameConfig{" + "fieldWidth=" + fieldWidth + ", fieldHeight=" + fieldHeight + ", gameLevel=" + gameLevel + ", music=" + music + ", soundEffect=" + soundEffect + ", extendMode=" + extendMode + ", playerOneType=" + playerOneType + ", playerTwoType=" + playerTwoType + '}';
    }
}
