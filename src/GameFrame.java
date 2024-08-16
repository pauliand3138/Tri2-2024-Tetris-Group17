import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        initialize();
        addElements();
        setVisible(true);
    }
    private void initialize(){
        setTitle("Tetris Game");
        int width = 500;
        int height = 500;
        setSize(width, height);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2;
        setLocation(x, y);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void addElements(){
        m_PlayScreen = new Play(getWidth(), getHeight());
        add(m_PlayScreen);
        m_PlayScreen.setVisible(true);
    }
    private Play m_PlayScreen;
}
