package view.panel;

public record PlayerScore(String name, int score, int config) {

    @Override
    public String toString() {
        return "view.panel.PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ", " + "config=" + config +']';
    }
}
