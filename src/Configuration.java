import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Configuration extends JFrame {

    public Configuration() {
        Font VALUE_FONT = new Font("Calibri", Font.BOLD, 16);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Configuration");
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(7, 3, 15, 0));

//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridLayout(2, 1, 0, 5));



        // Width
        JLabel widthLabel = new JLabel("Field Width (No of cells):");
        widthLabel.setHorizontalAlignment(JLabel.CENTER);
        JSlider widthSlider = new JSlider(5, 15, 10);
        JLabel widthValue = new JLabel("" + widthSlider.getValue());
        widthValue.setFont(VALUE_FONT);
        widthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                widthValue.setText("" + widthSlider.getValue());
            }
        });
        widthSlider.setPaintTrack(true);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        widthSlider.setMajorTickSpacing(1);
        widthSlider.setMinorTickSpacing(5);


        // Height
        JLabel heightLabel = new JLabel("Field Height (No of cells):");
        heightLabel.setHorizontalAlignment(JLabel.CENTER);
        JSlider heightSlider = new JSlider(15, 30, 20);
        JLabel heightValue = new JLabel("" + heightSlider.getValue());
        heightValue.setFont(VALUE_FONT);
        heightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                heightValue.setText("" + heightSlider.getValue());
            }
        });
        heightSlider.setPaintTrack(true);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        heightSlider.setMajorTickSpacing(1);
        heightSlider.setMinorTickSpacing(5);


        // Game Level
        JLabel gameLevelLabel = new JLabel("Field Height (No of cells):");
        gameLevelLabel.setHorizontalAlignment(JLabel.CENTER);
        JSlider gameLevelSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        JLabel gameLevelValue = new JLabel("" + gameLevelSlider.getValue());
        gameLevelValue.setFont(VALUE_FONT);
        gameLevelSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                gameLevelValue.setText("" + gameLevelSlider.getValue());
            }
        });
        gameLevelSlider.setPaintTrack(true);
        gameLevelSlider.setPaintTicks(true);
        gameLevelSlider.setPaintLabels(true);
        gameLevelSlider.setMajorTickSpacing(1);
        gameLevelSlider.setMinorTickSpacing(5);


        // Music Toggle
        JLabel musicLabel = new JLabel("Music (On/Off):");
        musicLabel.setHorizontalAlignment(JLabel.CENTER);
        JCheckBox musicCheckBox = new JCheckBox("",true);
        JLabel musicValue = new JLabel("On");
        musicValue.setFont(VALUE_FONT);
        musicCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicValue.setText(musicCheckBox.isSelected() ? "On" : "Off");
            }
        });


        // Sound Toggle
        JLabel soundLabel = new JLabel("Sound Effect (On/Off):");
        soundLabel.setHorizontalAlignment(JLabel.CENTER);
        JCheckBox soundCheckBox = new JCheckBox("", true);
        JLabel soundValue = new JLabel("On");
        soundValue.setFont(VALUE_FONT);
        soundCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                soundValue.setText(soundCheckBox.isSelected() ? "On" : "Off");
            }
        });


        // AI Toggle
        JLabel aiLabel = new JLabel("AI Play (On/Off):");
        aiLabel.setHorizontalAlignment(JLabel.CENTER);
        JCheckBox aiCheckBox = new JCheckBox("", false);
        JLabel aiValue = new JLabel("Off");
        aiValue.setFont(VALUE_FONT);
        aiCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aiValue.setText(aiCheckBox.isSelected() ? "On" : "Off");
            }
        });


        // Extend Mode Toggle
        JLabel extendLabel = new JLabel("Extend Mode (On/Off):");
        extendLabel.setHorizontalAlignment(JLabel.CENTER);
        JCheckBox extendCheckBox = new JCheckBox("", false);
        JLabel extendValue = new JLabel("Off");
        extendValue.setFont(VALUE_FONT);
        extendCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extendValue.setText(extendCheckBox.isSelected() ? "On" : "Off");
            }
        });

        gridPanel.add(widthLabel);
        gridPanel.add(widthSlider);
        gridPanel.add(widthValue);

        gridPanel.add(heightLabel);
        gridPanel.add(heightSlider);
        gridPanel.add(heightValue);

        gridPanel.add(gameLevelLabel);
        gridPanel.add(gameLevelSlider);
        gridPanel.add(gameLevelValue);

        gridPanel.add(musicLabel);
        gridPanel.add(musicCheckBox);
        gridPanel.add(musicValue);

        gridPanel.add(soundLabel);
        gridPanel.add(soundCheckBox);
        gridPanel.add(soundValue);

        gridPanel.add(aiLabel);
        gridPanel.add(aiCheckBox);
        gridPanel.add(aiValue);

        gridPanel.add(extendLabel);
        gridPanel.add(extendCheckBox);
        gridPanel.add(extendValue);

//        JButton backButton = new JButton("Back");
//
        float titlePanelPct = 0.20F;
        float configPanelPct = 0.50F;
        float creatorsPanelPct = 1 - configPanelPct - titlePanelPct;

        int creatorsPanelHeight = (int) (720 * creatorsPanelPct);
        JPanel creatorsPanel = new JPanel();
        creatorsPanel.setLayout(new BorderLayout());
        creatorsPanel.setSize(1080, creatorsPanelHeight);

        ScrollingTextPanel scrollingTextPanel = new ScrollingTextPanel();
        scrollingTextPanel.setPreferredSize(new Dimension(1080, 30));
        creatorsPanel.add(scrollingTextPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(360, 30));
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button pressed. Going back to main menu...");

                setVisible(false);
                TetrisMainScreen mainScreen = new TetrisMainScreen();
                mainScreen.setVisible(true);
            }
        });
        //creatorsPanel.add(backButton, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Moving up back button slightly
        backButtonPanel.add(backButton);
        creatorsPanel.add(backButtonPanel, BorderLayout.NORTH);

//        JLabel footerLabel = new JLabel("Author: Group 17");
//        footerLabel.setHorizontalAlignment(JLabel.CENTER);
////        buttonPanel.add(backButton);
////        buttonPanel.add(footerLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(creatorsPanel, BorderLayout.SOUTH);

    }
    public static void main(String[] args) {
        Configuration c = new Configuration();
        c.setVisible(true);
    }
}
