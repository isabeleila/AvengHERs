package Engine;

import javax.sound.sampled.Clip;
import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    
    public Sound(){
        soundURL[0] = getClass().getResource("/sound/AvengersThemeSong.wav");
        //soundURL[1] = getClass().getResource("/sound/death.wav");
        //soundURL[2] = getClass().getResource("/sound/fireball.wav");
         soundURL[1] = getClass().getResource("/sound/ChooseYourCharacter.wav");

    }

    public void setFile(int i){
        try{

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch(Exception e){

        }
    }
    public void play(){

        clip.start();

    }
    public void loop(){

        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }
    public void stop(){

        clip.stop();

    }

}
