package utilities;

import Common.Common;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static view.MainScreen.common;

public class MusicPlayer implements Runnable {
    private Player player;
    private boolean isPaused;
    private boolean isStopped;
    private boolean repeat;

    public MusicPlayer() {
        this.isPaused = !common.gameConfig.isMusic();
        this.isStopped = false;
        this.repeat = true;
    }

    private synchronized void initPlayer() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\resources\\audio\\bgm.mp3");
            player = new Player(fileInputStream);
        } catch (FileNotFoundException e) {
            System.out.println("BGM file not found");
        } catch (JavaLayerException e) {
            System.out.println("Error playing the BGM file: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        do {
            if (!isPaused) {
                try {
                    initPlayer();
                    player.play();
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Error during repeat: " + e.getMessage());
                }
            }
            System.out.print(""); // BUG: Removing this line causes unable to resume music
        } while (repeat && !isStopped);
    }

    public synchronized void pause() {
        isPaused = true;
        try {
            player.close();
        } catch (Exception e) {
            System.out.println("Error pausing the BGM file: " + e.getMessage());
        }
    }

    public synchronized void resume() {
        isPaused = false;
    }
    public synchronized void stop() {
        isStopped = true;
        player.close();
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
    public boolean isPaused() {
        return isPaused;
    }
}
