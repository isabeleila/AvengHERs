package Engine;

import java.net.URL;
import javax.sound.sampled.*;

public class SoundEffect {
    // Pre-loaded clips — indexed to match soundURL slots
    private Clip[] clips = new Clip[30];
    private int currentIndex = -1;
    URL soundURL[] = new URL[30];

    public SoundEffect(){
        soundURL[0] = getClass().getResource("/sound/death.wav");
        soundURL[1] = getClass().getResource("/sound/fireball.wav");

        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] != null) {
                try {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
                    clips[i] = AudioSystem.getClip();
                    clips[i].open(ais);
                } catch (Exception e) {
                }
            }
        }
    }

    public void setFile(int i){
        currentIndex = i;
        if (clips[i] != null) {
            clips[i].stop();
            clips[i].setFramePosition(0);
        }
    }

    public void play(){
        if (currentIndex >= 0 && clips[currentIndex] != null) {
            clips[currentIndex].start();
        }
    }

    public void loop(){
        if (currentIndex >= 0 && clips[currentIndex] != null) {
            clips[currentIndex].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(){
        if (currentIndex >= 0 && clips[currentIndex] != null) {
            clips[currentIndex].stop();
        }
    }
}
