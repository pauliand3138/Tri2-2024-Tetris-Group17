package view.panel;

import com.google.gson.Gson;
import javazoom.jl.player.Player;
import view.TetrisMainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TetrisHighScoreScreen extends JFrame {
    private JPanel panel;
    public TetrisHighScoreScreen() {


        setTitle("Tetris High Score Screen");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("High Scores");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        panel = new JPanel(new GridLayout(11, 4));



        Button clearButton = new Button("Clear High Score");
        titlePanel.add(clearButton);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    FileWriter fileWriter = new FileWriter("leaderboard.json");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write("");
                    bufferedWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                panel.removeAll();
                List<PlayerScore> playerScores = readJsonFile();
                createPlayerLabels(playerScores);
                panel.revalidate();
                panel.repaint();
            }
        });

        writeJsonFile();

        List<PlayerScore> playerScores = readJsonFile();

        createPlayerLabels(playerScores);

        float titlePanelPct = 0.20F;
        float highScorePanelPct = 0.50F;
        float creatorsPanelPct = 1 - highScorePanelPct - titlePanelPct;

        int creatorsPanelHeight = (int) (720 * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setLayout(new BorderLayout());
        creatorsPanel.setSize(1080, creatorsPanelHeight);

        ScrollingTextPanel scrollingTextPanel = new ScrollingTextPanel();
        scrollingTextPanel.setPreferredSize(new Dimension(1080, 30));
        creatorsPanel.add(scrollingTextPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(360, 30));
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button pressed. Going back to main menu...");

                setVisible(false);
                TetrisMainScreen mainScreen = new TetrisMainScreen();
                mainScreen.setVisible(true);
            }
        });
        creatorsPanel.add(backButton, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Moving up back button slightly
        backButtonPanel.add(backButton);
        creatorsPanel.add(backButtonPanel, BorderLayout.NORTH);

        add(titlePanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(creatorsPanel, BorderLayout.SOUTH);
    }

    public static List<PlayerScore> readJsonFile(){
        List<PlayerScore> playersList = new ArrayList<PlayerScore>();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("leaderboard.json");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            /*
             *   Check if the json file is empty because you won't be able to read
             *   any players or sort the players.
             * */

            String firstLine = bufferedReader.readLine();
            if(firstLine != null){
                PlayerScore[] players = gson.fromJson(firstLine, PlayerScore[].class);
                playersList = Arrays.asList(players);
                playersList.sort((a, b) -> Integer.compare(b.score(), a.score()));
            }

            fileReader.close();
        }catch(IOException e){
            System.out.println("Leader board json file cannot be read.");
            System.exit(0);
        }

        int playerCount = playersList.size();
        int MAX_PLAYERS = 10;

        if(playerCount != MAX_PLAYERS){
            // If there aren't enough players fill the remaining with dummy info
            for(int i = 0; i < MAX_PLAYERS - playerCount ; i++){
                playersList.add(new PlayerScore("----", 0, "----"));
            }
        }
        return playersList;
    }

    private void createPlayerLabels(List<PlayerScore> playerScores){
        List<JLabel> playerScoreLabels = new ArrayList<>();

        Font HEADER_FONT = new Font("Calibri", Font.BOLD, 16);

        JLabel numberLabel = new JLabel("#");
        JLabel nameLabel = new JLabel("Name");
        JLabel scoreLabel = new JLabel("Score");
        JLabel configLabel = new JLabel("Config");

        nameLabel.setFont(HEADER_FONT);
        scoreLabel.setFont(HEADER_FONT);
        numberLabel.setFont(HEADER_FONT);
        configLabel.setFont(HEADER_FONT);

        centerJLabel(numberLabel);
        centerJLabel(nameLabel);
        centerJLabel(scoreLabel);
        centerJLabel(configLabel);

        panel.add(numberLabel);
        panel.add(nameLabel);
        panel.add(scoreLabel);
        panel.add(configLabel);

        int playerNumber = 1;

        for (PlayerScore playerScore : playerScores) {
            playerScoreLabels.add(new JLabel("(" + playerNumber + ") "));
            playerScoreLabels.add(new JLabel(playerScore.name()));
            playerScoreLabels.add(new JLabel(String.valueOf(playerScore.score())));
            playerScoreLabels.add(new JLabel(playerScore.config()));
            playerNumber++;
        }

        for(JLabel label : playerScoreLabels) {
            centerJLabel(label);
            panel.add(label);
        }
    }

    private void writeJsonFile(){
        try{
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter("leaderboard.json");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ArrayList<PlayerScore> players = new ArrayList<>();
            String[] configTypes = {"Human", "AI"};
            for(int i = 1 ; i <= 10 ; i++){
                Random random = new Random();
                int configIndex = random.nextInt(configTypes.length);
                PlayerScore player = new PlayerScore("Player " + i, (int) (Math.random() * 100), configTypes[configIndex]);
                players.add(player);
            }
            bufferedWriter.write(gson.toJson(players));
            bufferedWriter.close();
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TetrisHighScoreScreen::run);
    }

    private static void run() {
        TetrisHighScoreScreen highScoreScreen = new TetrisHighScoreScreen();
        highScoreScreen.setVisible(true);
    }

    private static void centerJLabel(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
    }
}

record PlayerScore(String name, int score, String config) {

    @Override
    public String toString() {
        return "view.panel.PlayerScore[" +
                "name=" + name + ", " +
                "score=" + score + ", " + "config=" + config +']';
    }
}

