import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

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
        gridPanel.setLayout(new GridLayout(8, 3, 15, 0));

//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridLayout(2, 1, 0, 5));



        // Width
        JLabel widthLabel = new JLabel("Field Width (No of cells):");
        widthLabel.setHorizontalAlignment(JLabel.CENTER);
        JSlider widthSlider = new JSlider(5, 15, Common.gameConfig.getFieldWidth());
        JLabel widthValue = new JLabel("" + widthSlider.getValue());
        widthValue.setFont(VALUE_FONT);
        widthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                widthValue.setText("" + widthSlider.getValue());
                Common.gameConfig.setFieldWidth(widthSlider.getValue());
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
        JSlider heightSlider = new JSlider(15, 30, Common.gameConfig.getFieldHeight());
        JLabel heightValue = new JLabel("" + heightSlider.getValue());
        heightValue.setFont(VALUE_FONT);
        heightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                heightValue.setText("" + heightSlider.getValue());
                Common.gameConfig.setFieldHeight(heightSlider.getValue());
            }
        });
        heightSlider.setPaintTrack(true);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        heightSlider.setMajorTickSpacing(1);
        heightSlider.setMinorTickSpacing(5);


        // Game Level
        JLabel gameLevelLabel = new JLabel("Game Level:");
        gameLevelLabel.setHorizontalAlignment(JLabel.CENTER);
        JSlider gameLevelSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, Common.gameConfig.getGameLevel());
        JLabel gameLevelValue = new JLabel("" + gameLevelSlider.getValue());
        gameLevelValue.setFont(VALUE_FONT);
        gameLevelSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                gameLevelValue.setText("" + gameLevelSlider.getValue());
                Common.gameConfig.setGameLevel(gameLevelSlider.getValue());
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
        JCheckBox musicCheckBox = new JCheckBox("",Common.gameConfig.isMusic());
        JLabel musicValue = new JLabel(musicCheckBox.isSelected() ? "On" : "Off");
        musicValue.setFont(VALUE_FONT);
        musicCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicValue.setText(musicCheckBox.isSelected() ? "On" : "Off");
                Common.gameConfig.setMusic(musicCheckBox.isSelected());
            }
        });


        // Sound Toggle
        JLabel soundLabel = new JLabel("Sound Effect (On/Off):");
        soundLabel.setHorizontalAlignment(JLabel.CENTER);
        JCheckBox soundCheckBox = new JCheckBox("", Common.gameConfig.isSoundEffect());
        JLabel soundValue = new JLabel(soundCheckBox.isSelected() ? "On" : "Off");
        soundValue.setFont(VALUE_FONT);
        soundCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                soundValue.setText(soundCheckBox.isSelected() ? "On" : "Off");
                Common.gameConfig.setSoundEffect(soundCheckBox.isSelected());
            }
        });

        JLabel p1Label = new JLabel("Player One Type:");
        p1Label.setHorizontalAlignment(JLabel.CENTER);
        ButtonGroup p1 = new ButtonGroup();
        JRadioButton p1Human = new JRadioButton();
        p1Human.setText("Human");
        JRadioButton p1Ai = new JRadioButton();
        p1Ai.setText("AI");
        JRadioButton p1External = new JRadioButton();
        p1External.setText("External");
        p1.add(p1Human);
        p1.add(p1Ai);
        p1.add(p1External);
        JPanel p1Buttons = new JPanel();
        p1Buttons.setLayout(new BoxLayout(p1Buttons, BoxLayout.X_AXIS));
        p1Buttons.add(p1Human);
        p1Buttons.add(p1Ai);
        p1Buttons.add(p1External);
        int p1Enabled = Common.gameConfig.getPlayerOneType();
        if (p1Enabled == 0) {
            p1Human.setSelected(true);
        } else if (p1Enabled == 1) {
            p1Ai.setSelected(true);
        } else if (p1Enabled == 2) {
            p1External.setSelected(true);
        }
        p1Human.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Common.gameConfig.setPlayerOneType(0);
                }
            }
        });
        p1Ai.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Common.gameConfig.setPlayerOneType(1);
                }
            }
        });
        p1External.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Common.gameConfig.setPlayerOneType(2);
                }
            }
        });

        JLabel p2Label = new JLabel("Player Two Type:");
        p2Label.setHorizontalAlignment(JLabel.CENTER);
        ButtonGroup p2 = new ButtonGroup();
        JRadioButton p2Human = new JRadioButton();
        p2Human.setText("Human");
        JRadioButton p2Ai = new JRadioButton();
        p2Ai.setText("AI");
        JRadioButton p2External = new JRadioButton();
        p2External.setText("External");
        p2.add(p2Human);
        p2.add(p2Ai);
        p2.add(p2External);
        JPanel p2Buttons = new JPanel();
        p2Buttons.setLayout(new BoxLayout(p2Buttons, BoxLayout.X_AXIS));
        p2Buttons.add(p2Human);
        p2Buttons.add(p2Ai);
        p2Buttons.add(p2External);
        int p2Enabled = Common.gameConfig.getPlayerTwoType();
        if (p2Enabled == 0) {
            p2Human.setSelected(true);
        } else if (p2Enabled == 1) {
            p2Ai.setSelected(true);
        } else if (p2Enabled == 2) {
            p2External.setSelected(true);
        }
        p2Human.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Common.gameConfig.setPlayerTwoType(0);
                }
            }
        });
        p2Ai.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Common.gameConfig.setPlayerTwoType(1);
                }
            }
        });
        p2External.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Common.gameConfig.setPlayerTwoType(2);
                }
            }
        });

        // Extend Mode Toggle
        JLabel extendLabel = new JLabel("Extend Mode (On/Off):");
        extendLabel.setHorizontalAlignment(JLabel.CENTER);
        JCheckBox extendCheckBox = new JCheckBox("", Common.gameConfig.isExtendMode());
        JLabel extendValue = new JLabel(extendCheckBox.isSelected() ? "On" : "Off");
        extendValue.setFont(VALUE_FONT);
        extendCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extendValue.setText(extendCheckBox.isSelected() ? "On" : "Off");
                if (!extendCheckBox.isSelected()) {
                    p2Human.setSelected(true);
                    Common.gameConfig.setPlayerTwoType(0);
                    p2Human.setEnabled(false);
                    p2Ai.setEnabled(false);
                    p2External.setEnabled(false);
                } else {
                    p2Human.setEnabled(true);
                    p2Ai.setEnabled(true);
                    p2External.setEnabled(true);
                }
                Common.gameConfig.setExtendMode(extendCheckBox.isSelected());
            }
        });
        if (!extendCheckBox.isSelected()) {
            p2Human.setEnabled(false);
            p2Ai.setEnabled(false);
            p2External.setEnabled(false);
        }

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

        gridPanel.add(extendLabel);
        gridPanel.add(extendCheckBox);
        gridPanel.add(extendValue);

        gridPanel.add(p1Label);
        gridPanel.add(p1Buttons);
        gridPanel.add(new JLabel(""));

        gridPanel.add(p2Label);
        gridPanel.add(p2Buttons);


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
                try {
                    Common.gameConfig.writeGameConfigFile(Common.gameConfig);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
                TetrisMainScreen mainScreen = new TetrisMainScreen();
                mainScreen.setVisible(true);
            }
        });
        //creatorsPanel.add(backButton, BorderLayout.NORTH);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Moving up back button slightly
        backButtonPanel.add(backButton);
        creatorsPanel.add(backButtonPanel, BorderLayout.NORTH);

        add(titlePanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(creatorsPanel, BorderLayout.SOUTH);

    }
    public static void main(String[] args) {
        Configuration c = new Configuration();
        c.setVisible(true);
    }
}
