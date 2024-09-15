import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        SplashScreen splash = new SplashScreen();
        splash.displaySplash();
        SwingUtilities.invokeLater(()->{
            TetrisMainScreen mainScreen = new TetrisMainScreen();
            Common.gameConfig = new GameConfig();
            loadGameConfig();
            mainScreen.setVisible(true);
        });
    }

    public static void loadGameConfig() {
        try {
            Common.gameConfig = Common.gameConfig.readGameConfigFile();
        } catch (FileNotFoundException e) {
            Common.gameConfig.resetGameConfig();
            try {
                Common.gameConfig.writeGameConfigFile(Common.gameConfig);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}