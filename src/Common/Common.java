package Common;// A class that is used to access other classes that has been instantiated in the program.

import model.GameConfig;
import model.ScoreList;

public class Common {
    private static Common instance;
    public GameConfig gameConfig;
    public long gameSeed;
    public ScoreList scoreList;

    private Common(GameConfig gameConfig, long gameSeed, ScoreList scoreList) {
        this.gameConfig = gameConfig;
        this.gameSeed = gameSeed;
        this.scoreList = scoreList;
    }

    public static Common getInstance() {
        if (instance == null) {
            instance = new Common(new GameConfig(), System.currentTimeMillis(), new ScoreList());
        }
        return instance;
    }

    public long getGameSeed() {
        return gameSeed;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public void setGameSeed(long gameSeed) {
        this.gameSeed = gameSeed;
    }

    public ScoreList getScoreList() {
        return scoreList;
    }

    public void setScoreList(ScoreList scoreList) {
        this.scoreList = scoreList;
    }
}
