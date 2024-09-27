package view;

import Common.Common;
import controller.GameController;
import utilities.*;
import view.panel.Board;
import view.panel.GameInfoPanel;
import view.panel.ScrollingTextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Collections.min;
import static view.MainScreen.common;

public class Play extends JFrame {
    public static GameController[] gameController = {null, null};
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
        common.setGameSeed(System.currentTimeMillis());
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
        if (common.gameConfig.isExtendMode()) {
            width = ((365 + (common.gameConfig.getFieldWidth() - 5) * 17) * 2);
        } else {
            width = 365 + (common.gameConfig.getFieldWidth() - 5) * 17;
        }
        int height = 450 + (common.gameConfig.getFieldHeight() - 15) * 16;

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);
        layeredPane = getLayeredPane(); // initializing the layered pane
        if (!common.gameConfig.isSoundEffect()) {
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
        LabelFactory titleFactory = new TitleLabelFactory();
        JLabel titleLabel = titleFactory.createLabel("Tetris");
        titlePanel.add(titleLabel);

        JPanel bottomTitlePanel = new JPanel();
        bottomTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10,0));

        musicLabel = new JLabel(common.gameConfig.isMusic() ? "Music(M): ON" : "Music(M): OFF");
        soundLabel = new JLabel(common.gameConfig.isSoundEffect() ? "Sound(N): ON" : "Sound(N): OFF");
        bottomTitlePanel.add(musicLabel);
        bottomTitlePanel.add(soundLabel);

        topPanel.add(titlePanel);
        topPanel.add(bottomTitlePanel);

        add(topPanel, BorderLayout.NORTH);

        JPanel soundPanel = new JPanel();
        soundPanel.setSize(width, height);

        int playerCount = common.gameConfig.isExtendMode() ? 2 : 1;

        int boardPanelHeight = (int) (height * boardPanelPct);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(1, playerCount));

        for (int i = 0; i < playerCount; i++) {
            JPanel outerPane = new JPanel();
            outerPane.setLayout(new GridLayout(1, 2));
            JPanel innerPanel = new JPanel(new BorderLayout());
            int playerType = i == 0 ? common.gameConfig.getPlayerOneType() : common.gameConfig.getPlayerTwoType();
            gameInfoPanel[i] = new GameInfoPanel(i + 1, playerType);
            JPanel tetrisBoardPanel = new JPanel();
            tetrisBoardPanel.setLayout(null);
            board[i] = new Board(common.gameConfig.getFieldWidth() * 50, boardPanelHeight, i, gameInfoPanel[i]);
            gameController[i] = new GameController(board[i].game);
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
                        backToMainMenuOperation();
                        board[0] = null;
                        board[1] = null;
                    } else {
                        resumeGame();
                    }
                } else {
                    backToMainMenuOperation();
                    board[0] = null;
                    board[1] = null;
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
                if (!isPlayPaused && !gameController[0].isGameOver()) {
                    gameController[0].moveBlockRight();
                }
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[0].isGameOver()) {
                    gameController[0].moveBlockLeft();
                }
            }
        });

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[0].isGameOver()) {
                    gameController[0].rotateBlock();
                }
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[0].isGameOver()) {
                    gameController[0].moveBlockDown();
                }
            }
        });

        actionMap.put("w", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[1].isGameOver()) {
                    gameController[1].rotateBlock();
                }
            }
        });

        actionMap.put("s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[1].isGameOver()) {
                    gameController[1].moveBlockDown();
                }
            }
        });

        actionMap.put("a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[1].isGameOver()) {
                    gameController[1].moveBlockLeft();
                }
            }
        });

        actionMap.put("d", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !gameController[1].isGameOver()) {
                    gameController[1].moveBlockRight();
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
                if (common.gameConfig.isSoundEffect()) {
                    soundManager.close();
                } else {
                    soundManager.loadSoundEffects(soundFiles);
                }
                common.gameConfig.setSoundEffect(!common.gameConfig.isSoundEffect());
                soundLabel.setText(common.gameConfig.isSoundEffect() ? "Sound(N): ON" : "Sound(N): OFF");

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

    private void showPauseOverlay() {
        pauseOverlayLabel.setVisible(true); // Overlay message displayed
        pauseOverlayLabel.repaint(); // ensure it's painted
        layeredPane.moveToFront(pauseOverlayLabel);
        pauseOverlayLabel.requestFocusInWindow();
    }

    private void pauseGame() {
        isPlayPaused = true; // Game is paused
        for(int i = 0; i < gameController.length; i++) {
            if (gameController[i] != null) {
                gameController[i].pauseGame();
            }
        }
        showPauseOverlay();
    }

    private void resumeGame() {
        isPlayPaused = false; // game resumed
        pauseOverlayLabel.setVisible(false); // hides the overlay
        layeredPane.moveToFront(board[0]); // moves overlay to back
        for(int i = 0; i < gameController.length; i++) {
            if (gameController[i] != null) {
                gameController[i].resumeGame();
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
        for(int i = 0; i < gameController.length; i++) {
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
        MainScreen mainScreen = new MainScreen();
        common.gameConfig.setFieldHeight(20);
        common.gameConfig.setFieldWidth(10);
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