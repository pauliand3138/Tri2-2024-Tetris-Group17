import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private final int m_Duration = 3000;
    public SplashScreen(){
    }
    public void displaySplashAndExit(){
        displaySplash();
        System.exit(0);
    }
    private void displaySplash(){
        JPanel panel  = (JPanel) getContentPane();
        // Centre panel on splash screen
        int width = 300;
        int height = 500;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);
        // Create and add components to splash screen
        JLabel splashImage = new JLabel(new ImageIcon());
        JLabel creators = new JLabel("Creators: Janet Chimwayange, Paul Ian Lim, Karan Singde, Darcy McIntosh, Kacey Boyle.");
        JLabel description = new JLabel();

        setVisible(true);
    }
}
