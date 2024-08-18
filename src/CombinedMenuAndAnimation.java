import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CombinedMenuAndAnimation extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private FallingBlocksWithPauseMenu animationPanel;

    public CombinedMenuAndAnimation() {
        setTitle("Combined Menu and Animation Example");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create the main menu panel
        JPanel menuPanel = createMenuPanel();

        // Create the animation panel with pause functionality
        animationPanel = new FallingBlocksWithPauseMenu();

        // Add panels to the main panel
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(animationPanel, "Animation");

        add(mainPanel);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());

        JButton startAnimationButton = new JButton("Start Animation");
        startAnimationButton.setFont(new Font("Arial", Font.PLAIN, 20));

        startAnimationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Animation");
                animationPanel.requestFocusInWindow(); // Ensure the animation panel has focus
            }
        });

        menuPanel.add(startAnimationButton, BorderLayout.CENTER);
        return menuPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CombinedMenuAndAnimation().setVisible(true);
            }
        });
    }

    // Inner class for the animation panel with pause functionality
    private class FallingBlocksWithPauseMenu extends JPanel implements ActionListener {

        private Timer timer;
        private boolean isPaused = false;
        private int blockX = 100;
        private int blockY = 0;
        private final int blockSize = 50;

        private JPanel pauseMenu;
        private JButton backButton;

        public FallingBlocksWithPauseMenu() {
            setLayout(null); // Use null layout for absolute positioning
            setBackground(Color.BLACK);
            setFocusable(true);

            // Timer to update the animation
            timer = new Timer(10, this);
            timer.start();

            // Key listener to pause and resume
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_P) {
                        togglePause();
                    }
                }
            });

            // Initialize the pause menu
            pauseMenu = new JPanel();
            pauseMenu.setBackground(new Color(0, 0, 0, 150)); // semi-transparent black
            pauseMenu.setBounds(0, 0, 400, 400);
            pauseMenu.setLayout(null);
            pauseMenu.setVisible(false);

            // Add a label to the pause menu
            JLabel pauseLabel = new JLabel("PAUSED", SwingConstants.CENTER);
            pauseLabel.setForeground(Color.WHITE);
            pauseLabel.setFont(new Font("Arial", Font.BOLD, 30));
            pauseLabel.setBounds(100, 100, 200, 50);
            pauseMenu.add(pauseLabel);

            // Add a "Back" button to the pause menu
            backButton = new JButton("Back");
            backButton.setFont(new Font("Arial", Font.PLAIN, 14));
            backButton.setBounds(10, 10, 60, 30); // Position the button at the top left corner

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    togglePause();
                    cardLayout.show(mainPanel, "Menu"); // Go back to the menu
                }
            });

            pauseMenu.add(backButton);
            add(pauseMenu);
        }

        private void togglePause() {
            isPaused = !isPaused;
            pauseMenu.setVisible(isPaused); // Show or hide the pause menu
            if (!isPaused) {
                requestFocusInWindow(); // Refocus on the animation panel when unpaused
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillRect(blockX, blockY, blockSize, blockSize);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isPaused) {
                blockY += 1;
                if (blockY > getHeight()) {
                    blockY = 0; // Reset block to the top
                }
                repaint(); // Request to redraw the panel
            }
        }
    }
}
