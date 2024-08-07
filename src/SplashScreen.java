import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private int m_Duration;

    public SplashScreen(int duration){
        m_Duration = duration;
        setup();
    }
    private void setup(){
        JPanel panel = (JPanel) getContentPane();
        // Centre splash screen on screen

        int width = 300;
        int height = 500;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setBounds(x, y, width, height);

        setLayout(new FlowLayout());
        JLabel creators = new JLabel("Creators: Janet Chimwayange, Karan Singde, Paul Ian Lim, Darcy McIntosh and Kacey Boyle.");
        panel.add(creators);
        JLabel gameDescription = new JLabel("Description: ");
        panel.add(gameDescription);
    }
    private void showSplash(){
        setVisible(true);
        try{
            Thread.sleep(m_Duration);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void displayAndExit(){
        showSplash();
        System.exit(0);
    }
}
