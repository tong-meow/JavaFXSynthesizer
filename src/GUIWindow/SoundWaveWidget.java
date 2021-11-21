package GUIWindow;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class SoundWaveWidget extends WidgetGen{
    SoundWaveWidget(AnchorPane center, String widgetType) {
        super(center, widgetType);
    }

    @Override
    protected void DragCell(MouseEvent e) {
        Bounds bounds = center_.getBoundsInParent();
        AnchorPane.setTopAnchor( this, e.getSceneY() - bounds.getMinY() - 10 );
        AnchorPane.setLeftAnchor( this, e.getSceneX() - bounds.getMinX() - 60 );
        if (outputLine_ != null) {
            Bounds outputCircle = outputCircle_.localToScene(outputCircle_.getBoundsInLocal());
            outputLine_.setStartY(outputCircle.getCenterY() - bounds.getMinY());
            outputLine_.setStartX(outputCircle.getCenterX() - bounds.getMinX());
        }
        System.out.println("Dragging wave cell to: (x " + e.getSceneX() + ", y " + e.getSceneY() + ") ...");
    }

    @Override
    protected void CloseACell() {
        center_.getChildren().remove(this);
        MyApp.widgets_.remove(this);

        for (int i=0; i<MyApp.filters_.size(); i++){
            int index = MyApp.filters_.get(i).filterConnectIndex;
            if (MyApp.sounds_.get(index) == this){
                MyApp.filters_.get(i).inputLine_ = null;
                MyApp.filters_.get(i).filterConnectIndex = -1;
                break;
            }
        }

        for (int i=0; i<MyApp.mixers_.size(); i++){
            WidgetGen mixer = MyApp.mixers_.get(i);
            System.out.println("before length: " + mixer.mixerConnectIndex.size());
            for (int j=0; j<mixer.mixerConnectIndex.size(); j++) {
                int index = mixer.mixerConnectIndex.get(j);
                if (MyApp.sounds_.get(index) == this) {
                    Integer target = mixer.mixerConnectIndex.get(j);
                    mixer.mixerConnectIndex.remove(target);
                    for (Integer integer: mixerConnectIndex){
                        if (integer > target){
                            integer = integer-1;
                        }
                    }
                    System.out.println("after length: " + mixer.mixerConnectIndex.size());
                    break;
                }
            }
        }
        MyApp.sounds_.remove(this);

        if (outputLine_ != null){
            center_.getChildren().remove(outputLine_);
            System.out.println("delete ...");
            toSpeaker = false;
            outputLine_ = null;
            filterConnectIndex = -1;
        }
        System.out.println("Sound wave cell closed ... ");
    }

    @Override
    protected void StopALine(MouseEvent e) {
        boolean connected = false;
        // distance to speaker
        Bounds speaker = MyApp.speaker_.localToScene( MyApp.speaker_.getBoundsInLocal() );
        double spkDistance = Math.sqrt(Math.pow(e.getSceneY()-speaker.getCenterY(), 2) +
                Math.pow(e.getSceneX()-speaker.getCenterX(), 2));

        // check if the end of the line is connected to the speaker
        if (spkDistance <= 15) {
            connected = true;
            toSpeaker = true;
            System.out.println(widgetType_ + " is connected to the speaker ...");
        }

        // check if line connects to a filter
        if (!connected){
            for (int i=0; i<MyApp.filters_.size(); i++) {
                Circle fltCircle = MyApp.filters_.get(i).inputCircle_;
                Bounds filter = fltCircle.localToScene(fltCircle.getBoundsInLocal());
                double fltDistance = Math.sqrt(Math.pow(e.getSceneY() - filter.getCenterY(), 2) +                                                 Math.pow(e.getSceneX() - filter.getCenterX(), 2));
                if (fltDistance <= 10){
                    if (MyApp.filters_.get(i).inputLine_ == null) {
                        System.out.println(widgetType_ + " is connected to the filter ...");
                        MyApp.filters_.get(i).inputLine_ = outputLine_;
                        connected = true;
                        int index = MyApp.sounds_.indexOf(this);
                        MyApp.filters_.get(i).filterConnectIndex = index;
                        break;
                    }
                }
            }
        }

        // check if line connects to a mixer
        if (!connected){
            for (int i=0; i<MyApp.mixers_.size(); i++) {
                Circle mixCircle = MyApp.mixers_.get(i).inputCircle_;
                Bounds mixer = mixCircle.localToScene(mixCircle.getBoundsInLocal());
                double mixDistance = Math.sqrt(Math.pow(e.getSceneY() - mixer.getCenterY(), 2) +
                                               Math.pow(e.getSceneX() - mixer.getCenterX(), 2));
                if (mixDistance <= 10){
                    System.out.println(widgetType_ + " is connected to the mixer ...");
                    MyApp.mixers_.get(i).inputLine_ = outputLine_;
                    connected = true;
                    int index = MyApp.sounds_.indexOf(this);
                    MyApp.mixers_.get(i).mixerConnectIndex.add(index);
                    break;
                }
            }
        }

        // line does not connect to any widget, remove it
        if (!connected){
            center_.getChildren().remove(outputLine_);
            outputLine_ = null;
        }
    }
}
