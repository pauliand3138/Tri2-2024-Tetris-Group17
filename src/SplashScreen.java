import javax.swing.*;
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
        panel.setLayout(new BorderLayout());
        // Centre panel on splash screen
        int width = 300;
        int height = 500;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);
        // Create and add components to splash screen
        JLabel splashImage = new JLabel(new ImageIcon("src\\splash.png"));
        panel.add(splashImage, BorderLayout.CENTER);
        JLabel creators = new JLabel("Creators: Janet Chimwayange, Paul Ian Lim, Karan Singde, Darcy McIntosh, Kacey Boyle.");
        panel.add(creators, BorderLayout.NORTH);
        JLabel description = new JLabel("Description: One of seven tetrimino blocks are chosen. Once ");
        panel.add(description, BorderLayout.SOUTH);
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
