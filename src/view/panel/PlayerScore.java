package view.panel;

public class PlayerScore{

    String name;
    int score;
    int config;

    public PlayerScore(String name, int score, int config){
        this.name = name;
        this.score = score;
        this.config = config;
    }

    public String getName(){ return name; }

    public int getScore(){ return score; }

    public int getConfig(){ return config; }

    public void setName(String name){ this.name = name; }

    public void setScore(int score){ this.score = score; }

    public void setConfig(int config){ this.config = config; }

    @Override
    public String toString() {
        return "view.panel.PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ", " + "config=" + config +']';
    }
}
