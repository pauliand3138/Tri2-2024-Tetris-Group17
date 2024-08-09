import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SplashScreen extends JWindow {
    private final int m_Duration = 3000;
    public SplashScreen(){
        setup();
    }
    public void displaySplashAndExit(){
        displaySplash();
        System.exit(0);
    }
    private void setup(){
        JPanel panel  = (JPanel) getContentPane();
        panel.setLayout(new FlowLayout());
        // Centre panel on splash screen
        int width = 325;
        int height = 500;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);
        // Create and add components to splash screen
        int borderThickness = 5;
        panel.setBorder(BorderFactory.createLineBorder(Color.RED, borderThickness));
        width -= borderThickness * 2;
        JTextArea creators = new JTextArea("Creators: Janet Chimwayange, Paul Ian Lim, Karan Singde, Darcy McIntosh and Kacey Boyle.");
        creators.setPreferredSize(new Dimension(width, 100));
        creators.setEditable(false);
        creators.setLineWrap(true);
        creators.setOpaque(false); // can't see background colour of JTextArea
        panel.add(creators);

        JTextArea description = new JTextArea("Description: Tetris board is 10 blocks wide and 20 blocks high. One of seven tetris blocks will be generated. The player will need to fit the tetris block into the puzzle. The game ends when a tetris block exceeds the board's height.");
        description.setPreferredSize(new Dimension(width, 100));
        description.setEditable(false);
        description.setLineWrap(true);
        description.setOpaque(false);
        panel.add(description);
    }
    private void displaySplash(){
        setVisible(true);
        try{
            Thread.sleep(m_Duration);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        setVisible(false);
    }
}
