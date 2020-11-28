package MediaFunc;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Objects;


public class MusicPlayer {
    Media media = null;
    MediaPlayer player = null;
    File file = null;

    public void initialize(String url){
        try{
            JFXPanel panel = new JFXPanel();
            file = new File(url);
            media = new Media(file.toURI().toString());
            player = new MediaPlayer(media);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play(boolean curState){
        if(!Objects.isNull(media)){
            if(curState)
                player.play();
            else
                player.pause();
        }else{
            System.out.println("ERROR");
        }
    }

    public void stop(){
        if(!Objects.isNull(media)){
            System.out.println();
            player.stop();
        }else{
            System.out.println("ERROR");

        }
    }

    public void reload(){
        player.seek(player.getStartTime());
        player.play();
    }
}
