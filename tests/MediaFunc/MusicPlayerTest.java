package MediaFunc;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MusicPlayerTest {

    Media media = null;
    MediaPlayer player = null;
    File file = null;

    @Test
    void initialize() {
        try{
            JFXPanel panel = new JFXPanel();
            String path = "media\\퇴사.mp3";
            file = new File(path);
            media = new Media(file.toURI().toString());
            player = new MediaPlayer(media);

            player.stop();
            player.play();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}