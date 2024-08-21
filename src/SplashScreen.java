import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen(){
        setup();
    }
    public void displaySplash(){
        setVisible(true);
        try{
            int duration = 5000;
            Thread.sleep(duration);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        setVisible(false);

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

        float imageHeightPct = 0.70F;
        int imageHeight = (int)(imageHeightPct * height);
        ImageIcon splashIcon = new ImageIcon("src\\splash.png");
        // Splash image generated by Adobe Firefly
        Image resizedSplashIcon = splashIcon.getImage().getScaledInstance(width, imageHeight, Image.SCALE_SMOOTH);
        JLabel image = new JLabel(new ImageIcon(resizedSplashIcon));
        image.setBounds(borderThickness, borderThickness, width, imageHeight);
        panel.add(image);

        int remaining = height - imageHeight;
        int creatorsHeight = (int)(remaining * 0.40F);
        JTextArea creators = new JTextArea("Creators: Janet Chimwayange, Paul Ian Lim, Karan Singde, Darcy McIntosh and Kacey Boyle.");
        creators.setBounds(borderThickness, imageHeight + 5, width, creatorsHeight);
        creators.setEditable(false);
        creators.setLineWrap(true);
        creators.setWrapStyleWord(true);
        creators.setOpaque(false);
        panel.add(creators);

        int descriptionHeight = remaining - creatorsHeight;
        JTextArea description = new JTextArea("Description: Tetris board is 10 blocks wide and 20 blocks high. One of seven tetris blocks will be generated at the top of the board. The player will need to fit the tetris block into the puzzle. The game ends when a tetris block exceeds the board's height.");
        description.setBounds(borderThickness, imageHeight + creatorsHeight, width, descriptionHeight);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setOpaque(false);
        panel.add(description);
    }
}