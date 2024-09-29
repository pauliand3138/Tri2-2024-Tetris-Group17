package view;

import Common.Common;
import utilities.*;
import view.panel.ScrollingTextPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import static view.MainScreen.common;

public class Configuration extends JFrame {
    private int fieldWidth; // Width of the game field
    private int fieldHeight; // Height of the game field

    public Configuration() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);

        LabelFactory titleFactory = new TitleLabelFactory();
        JLabel titleLabel = titleFactory.createLabel("Configuration");

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(8, 3, 15, 0));

        LabelFactory subtitleFactory = new SubtitleLabelFactory();

        // Width
        JLabel widthLabel = subtitleFactory.createLabel("Field Width (No of cells):");
        JSlider widthSlider = new JSlider(5, 15, common.gameConfig.getFieldWidth());
        JLabel widthValue = subtitleFactory.createLabel("" + widthSlider.getValue());
        widthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                widthValue.setText("" + widthSlider.getValue());
                common.gameConfig.setFieldWidth(widthSlider.getValue());
            }
        });
        sliderDesign(widthSlider);

        // Height
        JLabel heightLabel = subtitleFactory.createLabel("Field Height (No of cells):");
        JSlider heightSlider = new JSlider(15, 30, common.gameConfig.getFieldHeight());
        JLabel heightValue = subtitleFactory.createLabel("" + heightSlider.getValue());
        heightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                heightValue.setText("" + heightSlider.getValue());
                common.gameConfig.setFieldHeight(heightSlider.getValue());
            }
        });
        sliderDesign(heightSlider);

        // Game Level
        JLabel gameLevelLabel = subtitleFactory.createLabel("Game Level:");
        JSlider gameLevelSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, common.gameConfig.getGameLevel());
        JLabel gameLevelValue = subtitleFactory.createLabel("" + gameLevelSlider.getValue());
        gameLevelSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                gameLevelValue.setText("" + gameLevelSlider.getValue());
                common.gameConfig.setGameLevel(gameLevelSlider.getValue());
            }
        });
        sliderDesign(gameLevelSlider);

        // Music Toggle
        JLabel musicLabel = subtitleFactory.createLabel("Music (On/Off):");
        JCheckBox musicCheckBox = new JCheckBox("",common.gameConfig.isMusic());
        JLabel musicValue = subtitleFactory.createLabel(musicCheckBox.isSelected() ? "On" : "Off");
        musicCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicValue.setText(musicCheckBox.isSelected() ? "On" : "Off");
                common.gameConfig.setMusic(musicCheckBox.isSelected());
            }
        });

        // Sound Toggle
        JLabel soundLabel = subtitleFactory.createLabel("Sound Effect (On/Off):");
        JCheckBox soundCheckBox = new JCheckBox("", common.gameConfig.isSoundEffect());
        JLabel soundValue = subtitleFactory.createLabel(soundCheckBox.isSelected() ? "On" : "Off");
        soundCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                soundValue.setText(soundCheckBox.isSelected() ? "On" : "Off");
                common.gameConfig.setSoundEffect(soundCheckBox.isSelected());
            }
        });

        JLabel p1Label = subtitleFactory.createLabel("Player One Type:");
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
        int p1Enabled = common.gameConfig.getPlayerOneType();
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
                    common.gameConfig.setPlayerOneType(0);
                }
            }
        });
        p1Ai.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    common.gameConfig.setPlayerOneType(1);
                }
            }
        });
        p1External.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    common.gameConfig.setPlayerOneType(2);
                }
            }
        });

        JLabel p2Label = subtitleFactory.createLabel("Player Two Type:");
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
        int p2Enabled = common.gameConfig.getPlayerTwoType();
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
                    common.gameConfig.setPlayerTwoType(0);
                }
            }
        });
        p2Ai.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    common.gameConfig.setPlayerTwoType(1);
                }
            }
        });
        p2External.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    common.gameConfig.setPlayerTwoType(2);
                }
            }
        });

        // Extend Mode Toggle
        JLabel extendLabel = subtitleFactory.createLabel("Extend Mode (On/Off):");
        JCheckBox extendCheckBox = new JCheckBox("", common.gameConfig.isExtendMode());
        JLabel extendValue = subtitleFactory.createLabel(extendCheckBox.isSelected() ? "On" : "Off");
        extendCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extendValue.setText(extendCheckBox.isSelected() ? "On" : "Off");
                if (!extendCheckBox.isSelected()) {
                    p2Human.setSelected(true);
                    common.gameConfig.setPlayerTwoType(0);
                    p2Human.setEnabled(false);
                    p2Ai.setEnabled(false);
                    p2External.setEnabled(false);
                } else {
                    p2Human.setEnabled(true);
                    p2Ai.setEnabled(true);
                    p2External.setEnabled(true);
                }
                common.gameConfig.setExtendMode(extendCheckBox.isSelected());
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
                    common.gameConfig.writeGameConfigFile(common.gameConfig);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
                MainScreen mainScreen = new MainScreen();
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

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public int getPointsPerLine() {
        return 100;
    }

    private static void sliderDesign(JSlider slider) {
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(5);
    }

    public static void main(String[] args) {
        Configuration c = new Configuration();
        c.setVisible(true);
    }
}
