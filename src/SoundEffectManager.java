import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundEffectManager {
    private Map<String, Clip> soundEffects = new HashMap<>();

    public SoundEffectManager(String[] soundFiles) {
        soundEffects = new HashMap<>();
        loadSoundEffects(soundFiles);
    }

    public void loadSoundEffects(String[] soundFiles) {
        for (String filePath : soundFiles) {
            try {
                File soundFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundEffects.put(soundFile.getName(), clip);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                System.out.println("Error loading sound file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    public void playSound(final String soundName) {
        Clip clip = soundEffects.get(soundName);
        if (clip != null) {
            new Thread(() -> {
                clip.setFramePosition(0);
                clip.start();
            }).start();
        } else {
            System.err.println("Sound effect not found: " + soundName);
        }
    }

    public void close() {
        for (Clip clip : soundEffects.values()) {
            clip.close();
        }
    }
}
