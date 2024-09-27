package utilities;
import javax.swing.*;

public interface LabelFactory{
    JLabel createLabel(String text);
//
//    public JLabelFactory() {
//    }
//
//    public JLabel createLabel(String type, String text) {
//        JLabel label = new JLabel(text);
//        switch(type) {
//            case "Title":
//                label.setFont(new Font("Calibri", Font.BOLD, 24));
//                break;
//            case "Subtitle":
//                label.setFont(new Font("Calibri", Font.BOLD, 16));
//                break;
//            case "Normal":
//                label.setFont(new Font("Calibri", Font.PLAIN, 16));
//                break;
//        }
//        label.setHorizontalAlignment(JLabel.CENTER);
//        return label;
//    }
}
