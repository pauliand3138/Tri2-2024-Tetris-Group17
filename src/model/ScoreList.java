package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import view.panel.GameInfoPanel;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import static view.MainScreen.common;

public class ScoreList {
    private final ArrayList<Score> scores = new ArrayList<>();

    public ScoreList() {

    }

    public ScoreList readScoreListFile() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader reader = new FileReader("TetrisHighScore.json");
            try {
                int ch;
                // Characters found
                while ((ch = reader.read()) != -1)
                    sb.append((char)ch);
                String json = sb.toString();
                Gson gson = new Gson();
                ScoreList scoreList = gson.fromJson(json, ScoreList.class);
                reader.close();
                //System.out.println(scoreList.scores.getFirst());
                return scoreList;
            } catch (Exception e) {
                try {
                    reader.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Reading failed! : " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            System.out.println("JSON error: " + e.getMessage());
            return null;
        }
        return null;
    }

    private static void writeScoreListFile(ScoreList scoreList) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter fw = new FileWriter("TetrisHighScore.json");
        fw.write(gson.toJson(scoreList));
        fw.close();
    }

    public ScoreList getScoreList() throws IOException {
        ScoreList scoreList = readScoreListFile();
        if (scoreList == null) {
            scoreList = new ScoreList();
            writeScoreListFile(scoreList);
        }
        return scoreList;
    }

    public ArrayList<Score> getScores() {
        return scores;
    }

    public boolean isHighScore(int score) {
        if (scores.isEmpty() || scores.size() < 10) {
            return true;
        }
        for(Score s : scores) {
            if (score > s.getScore()) {
                return true;
            }
        }
        return false;
    }

    public void addNewScore(GameConfig gameConfig, GameInfo gameInfo, String name) {
        int playerNum = gameInfo.getPlayerNum();
        GameConfig tempGameConfig = new GameConfig(gameConfig);
        if (playerNum == 2) {
            tempGameConfig.setPlayerOneType(gameConfig.getPlayerTwoType());
        }
        Score score = new Score(name, gameInfo.getScore(), tempGameConfig);
        if (scores.size() == 10) {
            scores.removeLast();
        }
        scores.add(score);
        sortScores();
        try {
            writeScoreListFile(this);
        } catch (IOException e) {
            System.out.println("Error when adding new high score after game ends: " + e.getMessage());
        }
    }

    public void sortScores() {
        common.scoreList.scores.sort((Score a1, Score a2) -> a1.getScore() - a2.getScore());
        Collections.reverse(common.scoreList.scores);
    }

    public void clearScores() {
        common.scoreList.scores.removeAll(scores);
        try {
            writeScoreListFile(common.scoreList);
        } catch (IOException e) {
            System.out.println("Error when clearing high score: " + e.getMessage());
        }
    }
}
