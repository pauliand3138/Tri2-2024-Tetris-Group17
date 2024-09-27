package utilities;

import javax.swing.*;
import java.awt.*;

public class CenterLabelFactory implements LabelFactory{

    @Override
    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}
