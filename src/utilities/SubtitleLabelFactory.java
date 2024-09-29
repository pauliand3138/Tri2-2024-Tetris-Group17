package utilities;
import javax.swing.*;
import java.awt.*;

public class SubtitleLabelFactory implements LabelFactory {

    @Override
    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Calibri", Font.BOLD, 16));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}

