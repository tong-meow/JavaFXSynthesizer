package GUIWindow;

import AudioComponents.AudioClip;

import javax.sound.sampled.*;

public class Speaker {

    public static void speaker() {
        System.out.println("Playing sound ... ");
        for (AudioClip clip: MyApp.audios_) {
            Clip c = null;
            try {
                c = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                System.out.println("Exception occurs: get clip failed...");
                System.exit(-1);
            }
            AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);
            try {
                c.open(format16, clip.getData(), 0, clip.getData().length);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                System.out.println("Exception occurs: clip open failed...");
                System.exit(-1);
            }
            LineListener listener = event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    ((Clip) (event.getSource())).close();
                }
            };
            c.addLineListener(listener);
            c.start();
        }
    }
}

