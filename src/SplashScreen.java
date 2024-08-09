import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen(){
        setup();
    }
    public void displaySplashAndExit(){
        displaySplash();
        System.exit(0);
    }
    private void setup(){
        JPanel panel  = (JPanel) getContentPane();
        panel.setLayout(null);
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
        height -= borderThickness * 2;
        float creatorsHeightPct = 0.10F;
        int creatorsHeight = (int)(creatorsHeightPct * height);
        JTextArea creators = new JTextArea("Creators: Janet Chimwayange, Paul Ian Lim, Karan Singde, Darcy McIntosh and Kacey Boyle.");
        creators.setEditable(false);
        creators.setLineWrap(true);
        creators.setOpaque(false); // can't see background colour of JTextArea
        creators.setBounds(borderThickness, 0, width, creatorsHeight);
        creators.setBorder(BorderFactory.createEmptyBorder());
        panel.add(creators);

        float descriptionHeightPct = 0.2F;
        int descriptionHeight = (int)(descriptionHeightPct * height);
        JTextArea description = new JTextArea("Description: Tetris board is 10 blocks wide and 20 blocks high. One of seven tetris blocks will be generated. The player will need to fit the tetris block into the puzzle. The game ends when a tetris block exceeds the board's height.");
        description.setEditable(false);
        description.setLineWrap(true);
        description.setOpaque(false);
        description.setBounds(borderThickness, height - descriptionHeight, width, descriptionHeight);
        panel.add(description);

        int imageHeight = height - creatorsHeight - descriptionHeight; // remaining area left apart from description and creators
        ImageIcon splashImage = new ImageIcon("src/splash.png");
        Image resizedSplashImage = splashImage.getImage().getScaledInstance(width, imageHeight, Image.SCALE_SMOOTH);
        splashImage = new ImageIcon(resizedSplashImage);
        JLabel splash = new JLabel(splashImage);
        splash.setBounds(borderThickness, creatorsHeight, width, imageHeight);
        panel.add(splash);
    }
    private void displaySplash(){
        setVisible(true);
        try{
            int duration = 5000;
            Thread.sleep(duration);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        setVisible(false);
    }
}