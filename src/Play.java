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

    public Play() {
        initialize();
        addElements();
        addKeybindControls();
        //board.startGame(board); // Starts the game here
        setVisible(true);

        // board focus on startup and after resuming game
//        //board.addFocusListener(new java.awt.event.FocusAdapter() {
//            public void focusGained(java.awt.event.FocusEvent evt) {
//                board.requestFocusInWindow();
//            }
//        });
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        int width = max((int)(Common.gameConfig.getFieldWidth() / 10.0 * 500), 500);
        int height = 425 + (Common.gameConfig.getFieldHeight() - 15) * 16; //Fixed formula after trial and error
        System.out.println(width);
        System.out.println(height);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);

        //isPaused = false;

        layeredPane = getLayeredPane(); // initializing the layered pane
    }

    private void addElements() {
        int width = getWidth();
        int height = getHeight();

        float titlePanelPct = 0.20F;
        float boardPanelPct = 0.50F;
        float creatorsPanelPct = 1 - boardPanelPct - titlePanelPct;

        int titlePanelHeight = (int) (height * titlePanelPct);
        JPanel titlePanel = new JPanel();
        titlePanel.setSize(width, titlePanelHeight);
        JLabel titleLabel = new JLabel("Tetris");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

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
                        mainScreen.setVisible(true);
                    } else {
                        resumeGame();
                    }
                } else {
                    setVisible(false);
                    TetrisMainScreen mainScreen = new TetrisMainScreen();
                    mainScreen.setVisible(true);
                }
            }
        });

        // Scrolling text
        ScrollingTextPanel scrollingTextPanel = new ScrollingTextPanel();
        scrollingTextPanel.setPreferredSize(new Dimension(width, 30));
        creatorsPanel.add(scrollingTextPanel, BorderLayout.CENTER);

        add(creatorsPanel, BorderLayout.SOUTH);

        //JLabel creatorsLabel = new JLabel("Creators: Paul Ian Lim, Karan Singde, Darcy McIntosh, Janet Chimwayange, and Kacey Boyle", SwingConstants.CENTER);
        //creatorsLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        //creatorsPanel.add(creatorsLabel, BorderLayout.SOUTH);

        //add(creatorsPanel, BorderLayout.SOUTH);

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

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused) board.moveBlockRight();
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused) board.moveBlockLeft();
            }
        });

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused) board.rotateBlock();
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlayPaused) board.moveBlockDown();
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
//                if (board.isPaused) resumeGame();
//                if (!board.isPaused) pauseGame();
//                if (isPlayPaused) resumeGame();
//
//                if (!isPlayPaused && !pauseOverlayLabel.isVisible()) {
//                    System.out.println("Paused!");
//                    pauseGame();
//                    return;
//                }
//                System.out.println(isPaused);
//                System.out.println(pauseOverlayLabel.isVisible());
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
}