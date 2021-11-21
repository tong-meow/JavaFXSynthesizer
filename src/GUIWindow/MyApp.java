package GUIWindow;

import AudioComponents.*;
import AudioComponents.Mixer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyApp extends Application {

    public final AnchorPane center_ = new AnchorPane();
    public static Circle speaker_;

    private final HBox top_ = new HBox();
    private final HBox bottom_ = new HBox();

    public static ArrayList<WidgetGen> widgets_ = new ArrayList<>();
    public static ArrayList<WidgetGen> sounds_ = new ArrayList<>();
    public static ArrayList<WidgetGen> filters_ = new ArrayList<>();
    public static ArrayList<WidgetGen> mixers_ = new ArrayList<>();

    public static ArrayList<AudioClip> audios_ = new ArrayList<>();

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Beep Boop Synthesizer");
        BorderPane bp = new BorderPane();

        setTop();
        setBottom();
        setCenter();

        bp.setTop(top_);
        bp.setCenter(center_);
        bp.setBottom(bottom_);

        primaryStage.setScene(new Scene(bp, 800, 600));
        primaryStage.show();
    }


    private void setTop() {
        top_.setAlignment(Pos.CENTER);
        top_.setStyle("-fx-background-color: #576457");
        top_.setSpacing(15);
        top_.setPadding(new Insets(5));
        Label name = new Label("Audio Component Generator");
        name.setUnderline(true);
        name.setTextFill(Color.WHITE);
        top_.getChildren().add(name);
        String[] btnArr = new String[5];
        btnArr[0] = "Sine Wave";
        btnArr[1] = "Square Wave";
        btnArr[2] = "VF Sine Wave";
        btnArr[3] = "Filter";
        btnArr[4] = "Mixer";
        for (String str: btnArr){
            Button btn = new Button(str);
            btn.setStyle("-fx-pref-width: 100px");
            btn.setOnAction(e -> GenerateWidget(str));
            top_.getChildren().add(btn);
        }
        Button playBtn = new Button("Play");
        playBtn.setOnAction(e -> PlaySounds());
        top_.getChildren().add(playBtn);

    }


    private void setBottom(){
        bottom_.setAlignment(Pos.CENTER);
        bottom_.setStyle("-fx-background-color: #576457");
        bottom_.setPadding(new Insets(5));
        bottom_.setSpacing(15);
        bottom_.setPadding(new Insets(5));
        String[] arr = new String[]{"C1", "D1", "E1", "F1", "G1", "A1", "B1", "C2"};
        for (String c: arr){
            Button testButton = new Button(c);
            testButton.setOnAction(e -> NotePlay(c));
            bottom_.getChildren().add(testButton);
        }
    }

    private void setCenter() {
        center_.setStyle("-fx-background-color: #818f81; -fx-border-color: #b8b9b6; -fx-border-width: 2");
        VBox speakerBox = new VBox();
        speakerBox.setAlignment(Pos.CENTER);
        speakerBox.setSpacing(10);
        speaker_ = new Circle(15);
        speaker_.setFill(Color.WHITE);
        Label name = new Label("Speaker");
        name.setTextFill(Color.WHITE);
        speakerBox.getChildren().add(name);
        speakerBox.getChildren().add(speaker_);
        center_.getChildren().add(speakerBox);
        AnchorPane.setLeftAnchor(speakerBox, 700.0);
        AnchorPane.setTopAnchor(speakerBox, 250.0);
    }

    private void GenerateWidget(String str) {
        System.out.println("Creating a " + str + " widget ...");
        switch (str){
            case("Sine Wave"):
                WidgetGen widget1 = new SoundWaveWidget(center_, "Sine Wave");
                widgets_.add(widget1);
                sounds_.add(widget1);
                break;
            case("Square Wave"):
                WidgetGen widget2 = new SoundWaveWidget(center_, "Square Wave");
                widgets_.add(widget2);
                sounds_.add(widget2);
                break;
            case("VF Sine Wave"):
                WidgetGen widget3 = new SoundWaveWidget(center_, "VF Sine Wave");
                widgets_.add(widget3);
                sounds_.add(widget3);
                break;
            case("Filter"):
                WidgetGen widget4 = new FilterWidget(center_, "Filter");
                widgets_.add(widget4);
                filters_.add(widget4);
                break;
            case("Mixer"):
                WidgetGen widget5 = new MixerWidget(center_, "Mixer");
                widgets_.add(widget5);
                mixers_.add(widget5);
                break;
            default:
                System.out.println("Error occurs when creating widget ... ");
        }
    }

    private void PlaySounds() {
        for (int i = 0; i < widgets_.size(); i++) {
            if (widgets_.get(i).toSpeaker) {
                WidgetGen widget = widgets_.get(i);
                if (widget.widgetType_.equals("Sine Wave") || widget.widgetType_.equals("Square Wave") || widget.widgetType_.equals("VF Sine Wave")) {
                    AudioComponent gen = GetAudioComponent(widget);
                    audios_.add(gen.getClip());
                } else if (widget.widgetType_.equals("Filter")) {
                    if (widget.inputLine_ != null && widget.filterConnectIndex != -1) {
                        float volume = (float) ((float) widget.widgetSlider_.getValue() / 50.0);
                        Filter filter = new Filter(volume);
                        WidgetGen connect = sounds_.get(widget.filterConnectIndex);
                        AudioComponent origin = GetAudioComponent(connect);
                        filter.connectInput(origin);
                        AudioClip ac = filter.getClip();
                        audios_.add(ac);
                    }
                } else if (widget.widgetType_.equals("Mixer")) {
                    if (widget.inputLine_ != null && widget.mixerConnectIndex.size() != 0) {
                        float volume = (float) ((float) widget.widgetSlider_.getValue() / 50.0);
                        Mixer mixer = new Mixer();
                        for (int j = 0; j < widget.mixerConnectIndex.size(); j++) {
                            Filter filter = new Filter(volume / 2);
                            int index = widget.mixerConnectIndex.get(j);
                            WidgetGen connect = sounds_.get(index);
                            AudioComponent origin = GetAudioComponent(connect);
                            filter.connectInput(origin);
                            mixer.connectInput(filter);
                        }
                        AudioClip ac = mixer.getClip();
                        audios_.add(ac);
                    }
                }
            }
            Speaker.speaker();
            audios_.clear();
        }
    }

    public AudioComponent GetAudioComponent(WidgetGen widget){
        AudioComponent ac = null;
        if (widget.widgetType_.equals("Sine Wave")){
            float frequency = (float) widget.widgetSlider_.getValue();
            ac = new SineWave(frequency);
        }else if (widget.widgetType_.equals("Square Wave")){
            float frequency = (float) widget.widgetSlider_.getValue();
            ac = new SquareWave(frequency);
        }else if (widget.widgetType_.equals("VF Sine Wave")) {
            AudioComponent linearRamp = new LinearRamp();
            AudioComponent gen = new VFSineWave();
            gen.connectInput(linearRamp);
            float volume = (float) widget.widgetSlider_.getValue();
            volume = (float) (volume / 50.0);
            Filter filter = new Filter(volume);
            filter.connectInput(gen);
            ac = filter;
        }
        return ac;
    }

    private void NotePlay(String c) {
        Map<String, Float> keyNote = new HashMap<>();
        keyNote.put("C1", 327.0F);
        keyNote.put("D1", 367.1F);
        keyNote.put("E1", 412.0F);
        keyNote.put("F1", 436.5F);
        keyNote.put("G1", 490.0F);
        keyNote.put("A1", 550.0F);
        keyNote.put("B1", 617.4F);
        keyNote.put("C2", 654.1F);
        AudioComponent key = new SineWave(keyNote.get(c));
        System.out.println("Playing " + c + " ... ");
        AudioClip clip = key.getClip();
        playKey(clip);
    }

    private void playKey(AudioClip clip){
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
