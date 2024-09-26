package Common;// A class that is used to access other classes that has been instantiated in the program.

import model.GameConfig;

public class Common {
    private static Common instance;
    public GameConfig gameConfig;
    public long gameSeed;

    private Common(GameConfig gameConfig, long gameSeed) {
        this.gameConfig = gameConfig;
        this.gameSeed = gameSeed;
    }

    public static Common getInstance() {
        if (instance == null) {
            instance = new Common(new GameConfig(), System.currentTimeMillis());
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
}
