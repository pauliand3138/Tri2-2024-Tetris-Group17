import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    public Board(){
        initialize();
    }
    private void initialize(){
        setBorder(BorderFactory.createLoweredBevelBorder());
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.fillRect(0,0, 10, 10);
    }
}
