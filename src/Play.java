import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Collections.min;

public class Play extends JFrame {

    private Board board;
    boolean isPlayPaused = false;
    boolean isKeyAdapterResume = false;// checks if game paused
    private JLabel pauseOverlayLabel; // Pause Overlay message
    private JLayeredPane layeredPane;
    private JLabel musicLabel, soundLabel;
    private MusicPlayer musicPlayer = createMusicPlayer();
    static String[] soundFiles = {"sounds/move.wav", "sounds/levelup.wav", "sounds/clearline.wav", "sounds/gameover.wav"};
    static SoundEffectManager soundManager = new SoundEffectManager(soundFiles);


    public Play() {
        initialize();
        addElements();
        addKeybindControls();
        setVisible(true);
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        int width = max((int)(Common.gameConfig.getFieldWidth() / 10.0 * 500), 500);
        int height = 450 + (Common.gameConfig.getFieldHeight() - 15) * 16; //Fixed formula after trial and error
        System.out.println(width);
        System.out.println(height);
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

        int boardPanelHeight = (int) (height * boardPanelPct);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setSize(width, boardPanelHeight);
        board = new Board(width, boardPanelHeight);
        boardPanel.add(board);
        add(boardPanel, BorderLayout.CENTER);

        int creatorsPanelHeight = (int) (height * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setLayout(new BorderLayout());
        creatorsPanel.setSize(width, creatorsPanelHeight);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(400, 30));
        backButton.setBackground(Color.WHITE);
        //creatorsPanel.add(backButton, BorderLayout.NORTH);

        //JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Moving up back button slightly
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        backButtonPanel.add(backButton);
        creatorsPanel.add(backButtonPanel, BorderLayout.NORTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!board.isGameOver) {
                    pauseGame(); // Pauses the game when back button is pressed.

                    // Confirmation dialogue screen
                    int response = JOptionPane.showConfirmDialog(null, "Return to main menu?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        // if 'Yes'
                        setVisible(false);
                        TetrisMainScreen mainScreen = new TetrisMainScreen();
                        Common.gameConfig.setFieldHeight(20);
                        Common.gameConfig.setFieldWidth(10);
                        mainScreen.setVisible(true);
                        if (!musicPlayer.isPaused()) {
                            musicPlayer.stop();
                        }

                    } else {
                        resumeGame();
                    }
                } else {
                    setVisible(false);
                    TetrisMainScreen mainScreen = new TetrisMainScreen();
                    Common.gameConfig.setFieldHeight(20);
                    Common.gameConfig.setFieldWidth(10);
                    mainScreen.setVisible(true);
                    if (!musicPlayer.isPaused()) {
                        musicPlayer.stop();
                    }
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

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board.isGameOver) {
                    board.moveBlockRight();
                    soundManager.playSound("move.wav");
                }
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board.isGameOver) {
                    board.moveBlockLeft();
                    soundManager.playSound("move.wav");
                }
            }
        });

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board.isGameOver) {
                    board.rotateBlock();
                    soundManager.playSound("move.wav");
                }
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused && !board.isGameOver) {
                    board.moveBlockDown();
                    soundManager.playSound("move.wav");
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

    private void showPauseOverlay() {
        pauseOverlayLabel.setVisible(true); // Overlay message displayed
        pauseOverlayLabel.repaint(); // ensure it's painted
        layeredPane.moveToFront(pauseOverlayLabel);
        pauseOverlayLabel.requestFocusInWindow();
    }

    private void pauseGame() {
        isPlayPaused = true; // Game is paused
        board.pauseGame();
        showPauseOverlay();
    }

    private void resumeGame() {
        isPlayPaused = false; // game resumed
        pauseOverlayLabel.setVisible(false); // hides the overlay
        layeredPane.moveToFront(board); // moves overlay to back
        board.resumeGame();
        requestFocusInWindow(); // board focused to capture key movements
    }

    private MusicPlayer createMusicPlayer() {
        MusicPlayer musicPlayer = new MusicPlayer();
        Thread musicThread = new Thread(musicPlayer);
        musicThread.start();
        return musicPlayer;
    }
}