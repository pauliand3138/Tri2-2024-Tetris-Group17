import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Play extends JFrame {

    private Board board;

    public Play(){
        initialize();
        addElements();
        addKeybindControls();
    }
    private void initialize(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        int width = 500;
        int height = 500;

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);
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
                System.out.println("Back button pressed. Going back to main menu...");

                setVisible(false);

                TetrisMainScreen mainScreen = new TetrisMainScreen();
                mainScreen.setVisible(true);
            }
        });

        JLabel creatorsLabel = new JLabel("Creators: Paul Ian Lim, Karan Singde, Darcy McIntosh, Janet Chimwayange and Kacey Boyle.", SwingConstants.CENTER);
        creatorsLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
        creatorsPanel.add(creatorsLabel, BorderLayout.SOUTH);

        add(creatorsPanel, BorderLayout.SOUTH);
    }

    public void addKeybindControls() {
        InputMap inputMap = this.getRootPane().getInputMap();
        ActionMap actionMap = this.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.moveBlockRight();
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.moveBlockLeft();
            }
        });

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.rotateBlock();
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.moveBlockDown();
            }
        });
    }
}