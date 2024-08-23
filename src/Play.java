import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Play extends JFrame {

    private Board board;
    private boolean isPaused; // Checks if game is paused.
    private JLabel pauseOverlayLabel; // Paused overlay message.
    private JLayeredPane layeredPane;

    public Play(){
        initialize();
        addElements();
        addKeybindControls();
        board.startGame(board); // Start the game here
        setVisible(true); // Ensure frame is visible to initialize the layeredPane

        // Ensuring the board gets focus on startup and after resume
        board.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                board.requestFocusInWindow();
            }
        });
    }
    private void initialize(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        int width = 500;
        int height = 500;

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);

        isPaused = false;

        layeredPane = getLayeredPane(); // Initialize layeredPane
    }
    private void addElements(){
        int width = getWidth();
        int height = getHeight();

        float titlePanelPct = 0.20F;
        float boardPanelPct = 0.50F;
        float creatorsPanelPct = 1 - boardPanelPct - titlePanelPct;

        int titlePanelHeight = (int)(height * titlePanelPct);
        JPanel titlePanel = new JPanel();
        titlePanel.setSize(width, titlePanelHeight);
        JLabel titleLabel = new JLabel("Play");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 25));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        int boardPanelHeight = (int)(height * boardPanelPct);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(null);
        boardPanel.setSize(width, boardPanelHeight);
        board = new Board(width, boardPanelHeight);
        boardPanel.add(board);
        add(boardPanel, BorderLayout.CENTER);

        int creatorsPanelHeight = (int)(height * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setLayout(new BorderLayout());
        creatorsPanel.setSize(width, creatorsPanelHeight);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(width, 20));
        backButton.setBackground(Color.WHITE);
        creatorsPanel.add(backButton, BorderLayout.NORTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseGame(); //Pause game when back button is pressed

                // Confirmation dialogue screen
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to go back to the main screen?", "Confirm Exit", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // If 'Yes' is selected
                    setVisible(false);
                    TetrisMainScreen mainScreen = new TetrisMainScreen();
                    mainScreen.setVisible(true);
                } else {
                    // If 'No' is selected
                    showPauseOverlay();
                }

                //System.out.println("Back button pressed. Going back to main menu...");
                //setVisible(false);
                //TetrisMainScreen mainScreen = new TetrisMainScreen();
                //mainScreen.setVisible(true);
            }
        });

        JLabel creatorsLabel = new JLabel("Creators: Paul Ian Lim, Karan Singde, Darcy McIntosh, Janet Chimwayange and Kacey Boyle.", SwingConstants.CENTER);
        creatorsLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        creatorsPanel.add(creatorsLabel, BorderLayout.SOUTH);

        add(creatorsPanel, BorderLayout.SOUTH);

        // Initializing the pause overlay without activating
        pauseOverlayLabel = new JLabel("Press any key to continue the game", SwingConstants.CENTER);
        pauseOverlayLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        pauseOverlayLabel.setForeground(Color.RED);
        pauseOverlayLabel.setBounds(0, boardPanelHeight / 2 - 20, width, 40); // Center positioning for overlay
        layeredPane.add(pauseOverlayLabel, JLayeredPane.POPUP_LAYER);
        pauseOverlayLabel.setVisible(false);
    }

    public void addKeybindControls() {
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) board.moveBlockRight();
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) board.moveBlockLeft();
            }
        });

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) board.rotateBlock();
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) board.moveBlockDown();
            }
        });

        // Listener for resuming the game
        pauseOverlayLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isPaused && pauseOverlayLabel.isVisible()) {
                    resumeGame();
                }
            }
        });

        pauseOverlayLabel.setFocusable(true); // Ensures pause overlay is focused and can detect key presses
    }

    private void showPauseOverlay() {
        pauseOverlayLabel.setVisible(true); // Overlay message displayed
        pauseOverlayLabel.repaint(); // ensure its painted
        layeredPane.moveToFront(pauseOverlayLabel);
        pauseOverlayLabel.requestFocusInWindow();
    }

    private void pauseGame() {
        isPaused = true; // Game state paused
        board.pauseGame();
        showPauseOverlay();
    }

    private void resumeGame() {
        isPaused = false; // game resumed
        pauseOverlayLabel.setVisible(false); // Hide overlay
        layeredPane.moveToFront(board); // Move overlay to the back
        board.resumeGame();
        board.requestFocusInWindow(); // Ensure the board has focus to capture key movement
    }
}