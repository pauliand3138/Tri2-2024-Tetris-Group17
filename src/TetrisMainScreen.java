import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;


public class TetrisMainScreen extends JFrame {

    private Image backgroundImage;

    public TetrisMainScreen() {
        setTitle("Tetris Main Screen"); //window title
        setSize(650, 700); //size of the window

        // Background image sourced from: https://unsplash.com/photos/the-night-sky-with-stars-and-the-milky-C7zKz_O02ic
        backgroundImage = Toolkit.getDefaultToolkit().getImage("Space Theme.jpg"); //loading background image.

        //close application function
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //layout manager
        setLayout(new BorderLayout());

        //adding components etc.,
        setupMainScreen();

        }

        public void setupMainScreen(){

        JPanel backgroundPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        //uploaded Tetris Logo to main menu
        // Tetris Logo sourced from: https://seeklogo.com/vector-logo/387138/tetris
        ImageIcon tetrisLogo = new ImageIcon("tetris-seeklogo.png");
        Image logoImage = tetrisLogo.getImage().getScaledInstance(200,150,Image.SCALE_SMOOTH); //scaling the size of logo
        tetrisLogo = new ImageIcon(logoImage);

        JLabel logoLabel = new JLabel(tetrisLogo);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(50,0,10,0));

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(logoLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        Font buttonFont = new Font("Lucida Console", Font.BOLD, 25); //selecting font style and size

        //defining the buttons
        JButton playButton = createStyledButton("Play", buttonFont);
        playButton.setFont(buttonFont);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton configButton = createStyledButton("Configuration", buttonFont);
        configButton.setFont(buttonFont);
        configButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton highScoresButton = createStyledButton("High Scores", buttonFont);
        highScoresButton.setFont(buttonFont);
        highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton exitButton = createStyledButton("Exit", buttonFont);
        exitButton.setFont(buttonFont);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //defining button size
        Dimension buttonSize = new Dimension(250,40);
        playButton.setPreferredSize(buttonSize);
        configButton.setPreferredSize(buttonSize);
        highScoresButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        //adding the buttons
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(configButton);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(highScoresButton);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(exitButton);

        //make the panel of buttons on the centre of the game window
        JPanel centrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        centrePanel.setOpaque(false);
        centrePanel.add(buttonPanel);

        backgroundPanel.add(centrePanel);

        //Adding in developer names!
        JPanel developerPanel = new JPanel();
        developerPanel.setLayout(new BoxLayout(developerPanel, BoxLayout.Y_AXIS));
        developerPanel.setOpaque(false);
        developerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10)); //padding

        JLabel titleLabel = new JLabel("Developers", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Lucida Console", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        developerPanel.add(titleLabel);
        developerPanel.add(Box.createVerticalStrut(10));

        JPanel firstRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        firstRowPanel.setOpaque(false);
        firstRowPanel.add(createDeveloperLabel("Darcy McIntosh"));
        firstRowPanel.add(createDeveloperLabel("Janet Chimwaynge"));
        firstRowPanel.add(createDeveloperLabel("Kacey Boyle"));
        developerPanel.add(firstRowPanel);

        JPanel secondRowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        secondRowPanel.setOpaque(false);
        secondRowPanel.add(createDeveloperLabel("Paul Ian Lim"));
        secondRowPanel.add(createDeveloperLabel("Karan Singde"));
        developerPanel.add(secondRowPanel);

        backgroundPanel.add(developerPanel, BorderLayout.SOUTH);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //starting new game, implement code into this. Currently got a script to show button works.
                System.out.println("Play button pressed. Starting game...");

                /*
                In order to make this button actually work I will need to first call in the Tetris Game logic class name.
                e.g., TetrisGameFile game = new TetrisGameFile();
                game.startGame();
                 */
            }
        });

        configButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //opening the configueration screen. Currently got a script to show the button works.
                System.out.println("Configuration button pressed. Opening screen...");

                /*
                In order to make this button actually work I will need to first call in the config screen class name.
                e.g., ConfigScreen configScreen = new ConfigScreen();
                configScreen.show();
                 */
            }
        });

        highScoresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //opening the High Scores screen. Currently got a script to show the button works.
                System.out.println("High scores button pressed. Opening High scores screen...");

                /*
                In order to make this button actually work I will need to first call in the high scores screen class name.
                e.g., HighScoresScreen highScoresScreen = new HighScoresScreen();
                highScoresScreen.show();
                 */

            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit game?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }

    //Styled buttons Method
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50, 128)); //transparent background
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(5,15,5,15)); //Remove border

        //hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 30, 30, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 50, 128));
            }
        });
        return button;
    }

    class RoundedBorder extends LineBorder {
        private int radius;

        RoundedBorder(int radius) {
            super(Color.WHITE, 2, true);
            this.radius = radius;
        }

        RoundedBorder(int radius, Color color) {
            super(color, 2, true);
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(lineColor);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    private JLabel createDeveloperLabel(String name) {
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        return label;
    }

    public static void main(String[] args) {
        //main window instance
        TetrisMainScreen mainWindow = new TetrisMainScreen();

        mainWindow.setVisible(true); //making window visible
    }
}

