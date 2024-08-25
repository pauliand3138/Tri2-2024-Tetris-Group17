import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrollingTextPanel extends JPanel implements ActionListener {

    private String[] developers = {
            "Paul Ian Lim",
            "Karan Singde",
            "Darcy McIntosh",
            "Janet Chimwayange",
            "Kacey Boyle"
    };

    private int xOffset = 0; // current offset of the text
    private int textWidth; // total width of text
    private int speed = 2; // scrolling speed
    private Timer timer;

    public ScrollingTextPanel() {
        setFont(new Font("Calibri", Font.PLAIN, 13)); // Font Size

        // combining dev names into one string with spacing
        String combinedText = String.join("   ", developers); // spaces between names
        textWidth = getFontMetrics(getFont()).stringWidth(combinedText);

        timer = new Timer(40, this); // smoother scrolling by updating every 25 seconds
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // anti-aliasing for smoother text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw combined text at current offset
        g2d.drawString(String.join("   ", developers), xOffset, getHeight() / 2 + g2d.getFontMetrics().getAscent() / 3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        xOffset -= speed; // text moves to the left

        // reset the offset when the text goess off the left side of the panel
        if (xOffset + textWidth < 0) {
            xOffset = getWidth();
        }

        repaint(); // repaint the panel to show the updated positions
    }
}