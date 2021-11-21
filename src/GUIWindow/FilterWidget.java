package GUIWindow;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class FilterWidget extends WidgetGen{
    FilterWidget(AnchorPane center, String widgetType) {
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

        if (inputLine_ != null) {
            Bounds inputCircle = inputCircle_.localToScene(inputCircle_.getBoundsInLocal());
            inputLine_.setEndY(inputCircle.getCenterY() - bounds.getMinY());
            inputLine_.setEndX(inputCircle.getCenterX() - bounds.getMinX());
        }

        System.out.println("Dragging filter cell to: (x " + e.getSceneX() + ", y " + e.getSceneY() + ") ...");

    }

    @Override
    protected void CloseACell() {
        center_.getChildren().remove(this);
        MyApp.widgets_.remove(this);
        MyApp.filters_.remove(this);

        if (outputLine_ != null){
            center_.getChildren().remove(outputLine_);
            System.out.println("delete ...");
            toSpeaker = false;
            outputLine_ = null;
            filterConnectIndex = -1;
        }
        if (inputLine_ != null){
            center_.getChildren().remove(inputLine_);
            inputLine_ = null;
            filterConnectIndex = -1;
        }
        System.out.println("Filter cell closed ... ");
    }

    @Override
    protected void StopALine(MouseEvent e) {
        // distance to speaker
        Bounds speaker = MyApp.speaker_.localToScene( MyApp.speaker_.getBoundsInLocal() );
        double spkDistance = Math.sqrt(Math.pow(e.getSceneY()-speaker.getCenterY(), 2) +
                Math.pow(e.getSceneX()-speaker.getCenterX(), 2));

        // check if the end of the line is connected to the speaker
        if (spkDistance <= 15) {
            toSpeaker = true;
            System.out.println(widgetType_ + " is connected to the speaker ...");
        }else{
            center_.getChildren().remove(outputLine_);
            outputLine_ = null;
        }
    }
}
