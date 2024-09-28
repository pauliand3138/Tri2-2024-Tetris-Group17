package model;

public class Score {
    private String name;
    private int score;
    private GameConfig config;

    public Score(String name, int score, GameConfig config) {
        this.name = name;
        this.score = score;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameConfig getConfig() {
        return config;
    }

    public void setConfig(GameConfig config) {
        this.config = config;
    }

    public String toString() {
        return name + " " + score + " " + config;
    }
}
