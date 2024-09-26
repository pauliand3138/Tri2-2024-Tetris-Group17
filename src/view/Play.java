package view;

import Common.Common;
import com.google.gson.Gson;
import model.GameInfo;
import utilities.MusicPlayer;
import utilities.SoundEffectManager;
import view.panel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Collections.min;

public class Play extends JFrame {
    private static Board[] board = {null, null};
    public  GameInfoPanel[] gameInfoPanel = {null, null};
    boolean isPlayPaused = false;
    boolean isKeyAdapterResume = false;// checks if game paused
    private JLabel pauseOverlayLabel; // Pause Overlay message
    private JLayeredPane layeredPane;
    private JLabel musicLabel, soundLabel;
    private MusicPlayer musicPlayer = createMusicPlayer();
    static String[] soundFiles = {"src\\resources\\audio\\move.wav",
            "src\\resources\\audio\\levelup.wav",
            "src\\resources\\audio\\clearline.wav",
            "src\\resources\\audio\\gameover.wav"};
    public static SoundEffectManager soundManager = new SoundEffectManager(soundFiles);
    public static boolean isConnectionError = false;

    public Play() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // For generating random tetris block
        Common.gameSeed = System.currentTimeMillis();
        initialize();
        addElements();
        addKeybindControls();
        setVisible(true);

        if (isConnectionError) {
                JOptionPane.showMessageDialog(new JFrame(), "You need to start TetrisServer to use external player mode.", "Error",
                        JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        int width;
        if (Common.gameConfig.isExtendMode()) {
            width = ((365 + (Common.gameConfig.getFieldWidth() - 5) * 17) * 2);
        } else {
            width = 365 + (Common.gameConfig.getFieldWidth() - 5) * 17;
        }
        int height = 450 + (Common.gameConfig.getFieldHeight() - 15) * 16; //Fixed formula after trial and error

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);
        layeredPane = getLayeredPane(); // initializing the layered pane
        if (!Common.gameConfig.isSoundEffect()) {
            soundManager.close();
        }
    }

    private void addElements() {
        int width = getWidth();
        int height = getHeight();

        float titlePanelPct = 0.20F;
        float boardPanelPct = 0.50F;
        float creatorsPanelPct = 1 - boardPanelPct - titlePanelPct;

        int titlePanelHeight = (int) (height * titlePanelPct);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel titlePanel = new JPanel();
        titlePanel.setSize(width, 0);
        JLabel titleLabel = new JLabel("Tetris");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel bottomTitlePanel = new JPanel();
        bottomTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10,0));

        musicLabel = new JLabel(Common.gameConfig.isMusic() ? "Music(M): ON" : "Music(M): OFF");
        soundLabel = new JLabel(Common.gameConfig.isSoundEffect() ? "Sound(N): ON" : "Sound(N): OFF");
        bottomTitlePanel.add(musicLabel);
        bottomTitlePanel.add(soundLabel);

        topPanel.add(titlePanel);
        topPanel.add(bottomTitlePanel);

        add(topPanel, BorderLayout.NORTH);

        JPanel soundPanel = new JPanel();
        soundPanel.setSize(width, height);

        int playerCount = Common.gameConfig.isExtendMode() ? 2 : 1;

        int boardPanelHeight = (int) (height * boardPanelPct);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(1, playerCount));

        for (int i = 0; i < playerCount; i++) {
            JPanel outerPane = new JPanel();
            outerPane.setLayout(new GridLayout(1, 2));
            JPanel innerPanel = new JPanel(new BorderLayout());
            int playerType = i == 0 ? Common.gameConfig.getPlayerOneType() : Common.gameConfig.getPlayerTwoType();
            gameInfoPanel[i] = new GameInfoPanel(i + 1, playerType);
            JPanel tetrisBoardPanel = new JPanel();
            tetrisBoardPanel.setLayout(null);
            board[i] = new Board(Common.gameConfig.getFieldWidth() * 50, boardPanelHeight, i, gameInfoPanel[i]);
            tetrisBoardPanel.add(board[i]);
            innerPanel.add(gameInfoPanel[i], BorderLayout.WEST);
            innerPanel.add(tetrisBoardPanel, BorderLayout.CENTER);
            outerPane.add(innerPanel);
            boardPanel.add(outerPane);
        }

        add(boardPanel, BorderLayout.CENTER);

        int creatorsPanelHeight = (int) (height * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setLayout(new BorderLayout());
        creatorsPanel.setSize(width, creatorsPanelHeight);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(400, 30));
        backButton.setBackground(Color.WHITE);
        //creatorsPanel.add(backButton, BorderLayout.NORTH);

        // Moving up back button slightly
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        backButtonPanel.add(backButton);
        creatorsPanel.add(backButtonPanel, BorderLayout.NORTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isAllGameEnded()) {
                    pauseGame(); // Pauses the game when back button is pressed.
                    // Confirmation dialogue screen
                    int response = JOptionPane.showConfirmDialog(null, "Return to main menu?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        // if 'Yes'
//                        setVisible(false);
//                        view.TetrisMainScreen mainScreen = new view.TetrisMainScreen();
//                        Common.gameConfig.setFieldHeight(20);
//                        Common.gameConfig.setFieldWidth(10);
//                        mainScreen.setVisible(true);
//                        closeAllBoardTimer();
//                        if (!musicPlayer.isPaused()) {
//                            musicPlayer.stop();
//                        }

                        updateLeaderboard();

                        backToMainMenuOperation();


                    } else {
                        resumeGame();
                    }
                } else {
//                    setVisible(false);
//                    view.TetrisMainScreen mainScreen = new view.TetrisMainScreen();
//                    Common.gameConfig.setFieldHeight(20);
//                    Common.gameConfig.setFieldWidth(10);
//                    mainScreen.setVisible(true);
//                    closeAllBoardTimer();
//                    if (!musicPlayer.isPaused()) {
//                        musicPlayer.stop();
//                    }

                    updateLeaderboard();
                    backToMainMenuOperation();
                }
            }
        });

        // Scrolling text
        ScrollingTextPanel scrollingTextPanel = new ScrollingTextPanel();
        scrollingTextPanel.setPreferredSize(new Dimension(width, 30));
        creatorsPanel.add(scrollingTextPanel, BorderLayout.CENTER);

        add(creatorsPanel, BorderLayout.SOUTH);

        // Without activating, initialize pause overlay
        pauseOverlayLabel = new JLabel("Press P to continue the game", SwingConstants.CENTER);
        pauseOverlayLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
        pauseOverlayLabel.setForeground(Color.BLACK);
        pauseOverlayLabel.setBounds(0, boardPanelHeight / 2 - 20, width, 40); // Center position
        layeredPane.add(pauseOverlayLabel, JLayeredPane.POPUP_LAYER);
        pauseOverlayLabel.setVisible(false);
    }

    private void addKeybindControls() {
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");
        inputMap.put(KeyStroke.getKeyStroke("P"), "p");
        inputMap.put(KeyStroke.getKeyStroke("M"),"m");
        inputMap.put(KeyStroke.getKeyStroke("N"), "n");
        inputMap.put(KeyStroke.getKeyStroke("W"), "w");
        inputMap.put(KeyStroke.getKeyStroke("S"), "s");
        inputMap.put(KeyStroke.getKeyStroke("A"), "a");
        inputMap.put(KeyStroke.getKeyStroke("D"), "d");

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[0].isGameOver) {
                    board[0].moveBlockRight();
                }
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[0].isGameOver) {
                    board[0].moveBlockLeft();
                }
            }
        });

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[0].isGameOver) {
                    board[0].rotateBlock();
                }
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[0].isGameOver) {
                    board[0].moveBlockDown();
                }
            }
        });

        actionMap.put("w", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[1].isGameOver) {
                    board[1].rotateBlock();
                }
            }
        });

        actionMap.put("s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[1].isGameOver) {
                    board[1].moveBlockDown();
                }
            }
        });

        actionMap.put("a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[1].isGameOver) {
                    board[1].moveBlockLeft();
                }
            }
        });

        actionMap.put("d", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board[1].isGameOver) {
                    board[1].moveBlockRight();
                }
            }
        });

        actionMap.put("p", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isKeyAdapterResume) {
                    isKeyAdapterResume = false;
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });

        actionMap.put("m", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!musicPlayer.isPaused()) {
                    musicPlayer.pause();
                    System.out.println("Music paused!");
                    musicLabel.setText("Music(M): OFF");
                } else {
                    musicPlayer.resume();
                    System.out.println("Music resumed!");
                    musicLabel.setText("Music(M): ON");
                }
            }
        });

        actionMap.put("n", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Common.gameConfig.isSoundEffect()) {
                    soundManager.close();
                } else {
                    soundManager.loadSoundEffects(soundFiles);
                }
                Common.gameConfig.setSoundEffect(!Common.gameConfig.isSoundEffect());
                soundLabel.setText(Common.gameConfig.isSoundEffect() ? "Sound(N): ON" : "Sound(N): OFF");

            }
        });
        //Listener for resuming game
        pauseOverlayLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("key adapter");
                if (isPlayPaused && pauseOverlayLabel.isVisible() && e.getKeyCode() == KeyEvent.VK_P) {
                    isKeyAdapterResume = true;
                    resumeGame();
                }
            }
        });
        //pauseOverlayLabel.setFocusable(true); // Pause overlay is focused and is able to detect key presses
    }

    private void updateLeaderboard(){
        var players = TetrisHighScoreScreen.readJsonFile();
        if(players.isEmpty()){
            // Add players to leaderboard
            for(GameInfoPanel panel : gameInfoPanel){
                addPlayerToLeaderboard(panel.getGameInfo());
            }
        }else{
            for(GameInfoPanel panel : gameInfoPanel){
                GameInfo gameInfo = panel.getGameInfo();
                int score = gameInfo.getScore();
                for(PlayerScore player : players){
                    if(score > player.score()){
                        addPlayerToLeaderboard(gameInfo);
                        break;
                    }
                }
            }
        }
    }


    private void addPlayerToLeaderboard(GameInfo gameInfo){
        int playerNum = gameInfo.getPlayerNum();
        String name = JOptionPane.showInputDialog("Player " + playerNum + "'s score is on the highscore leaderboard, please enter your name Player " + playerNum + ": " );
        int score = gameInfo.getScore();
        int playerType = gameInfo.getPlayerType();
        var playerScores = TetrisHighScoreScreen.readJsonFile();
        try{
            playerScores.add(new PlayerScore(name, score, playerType));
            playerScores.sort((a, b) -> Integer.compare(b.score(), a.score()));

            if(playerScores.size() > 10){
                playerScores.removeLast();
            }
            FileWriter fileWriter = new FileWriter("leaderboard.json");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(new Gson().toJson(playerScores));
            fileWriter.close();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPauseOverlay() {
        pauseOverlayLabel.setVisible(true); // Overlay message displayed
        pauseOverlayLabel.repaint(); // ensure it's painted
        layeredPane.moveToFront(pauseOverlayLabel);
        pauseOverlayLabel.requestFocusInWindow();
    }

    private void pauseGame() {
        isPlayPaused = true; // Game is paused
        for(int i = 0; i < board.length; i++) {
            if (board[i] != null) {
                board[i].pauseGame();
            }
        }
        showPauseOverlay();
    }

    private void resumeGame() {
        isPlayPaused = false; // game resumed
        pauseOverlayLabel.setVisible(false); // hides the overlay
        layeredPane.moveToFront(board[0]); // moves overlay to back
        for(int i = 0; i < board.length; i++) {
            if (board[i] != null) {
                board[i].resumeGame();
            }
        }
        requestFocusInWindow(); // board focused to capture key movements
    }

    private MusicPlayer createMusicPlayer() {
        MusicPlayer musicPlayer = new MusicPlayer();
        Thread musicThread = new Thread(musicPlayer);
        musicThread.start();
        return musicPlayer;
    }

    public static boolean isAllGameEnded() {
        boolean isEnded = true;
        for(int i = 0; i < board.length; i++) {
            if (board[i] != null) {
                if(!board[i].isGameOver) {
                    isEnded = false;
                }
            }
        }
        return isEnded;
    }

    public void backToMainMenuOperation() {
        setVisible(false);
        TetrisMainScreen mainScreen = new TetrisMainScreen();
        Common.gameConfig.setFieldHeight(20);
        Common.gameConfig.setFieldWidth(10);
        mainScreen.setVisible(true);
        if (!musicPlayer.isPaused()) {
            musicPlayer.stop();
        }
        for (Board game : board) {
            if (game != null) {
                game.isBoardActive = false;
                game.timer.stop();
                game.timer = null;
                if (game.AIdownTimer != null)
                    game.AIdownTimer.stop();
                game.AIdownTimer = null;
                if (game.ExternalPlayerTimer != null)
                    game.ExternalPlayerTimer.stop();
                game.ExternalPlayerTimer = null;
            }
        }
    }
}