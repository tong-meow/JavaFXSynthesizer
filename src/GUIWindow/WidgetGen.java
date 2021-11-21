package GUIWindow;import javafx.geometry.Bounds;import javafx.geometry.Insets;import javafx.geometry.Pos;import javafx.scene.control.Button;import javafx.scene.control.Slider;import javafx.scene.input.MouseEvent;import javafx.scene.layout.AnchorPane;import javafx.scene.layout.HBox;import javafx.scene.layout.Pane;import javafx.scene.layout.VBox;import javafx.scene.paint.Color;import javafx.scene.paint.Paint;import javafx.scene.shape.Circle;import javafx.scene.shape.Line;import javafx.scene.text.Text;import java.util.ArrayList;public class WidgetGen extends Pane {    protected final AnchorPane center_;    protected final HBox cell_;    protected VBox middlePart_;    protected VBox rightPart_;    protected String widgetType_;    protected Slider widgetSlider_;    // for every widget    protected Circle outputCircle_;    // for filters & mixers    protected Circle inputCircle_ = null;    // for every widget, the outputLine will connect to either filter/mixer or speaker    protected Line outputLine_;    // for filters & mixers, their inputLine equals to the outputLine of soundwave widget that connects to them    protected Line inputLine_ = null;    // for filters, let them know which widget they are connected with    protected int filterConnectIndex = -1;    // for mixers, let them know which widgets they are connected with    protected ArrayList<Integer> mixerConnectIndex = new ArrayList<>();    public boolean toSpeaker = false;    WidgetGen(AnchorPane center, String widgetType){        System.out.println("Creating a new widget ... ");        center_ = center;        widgetType_ = widgetType;        cell_ = new HBox();        cell_.setStyle("-fx-background-color: #566c56; -fx-border-color: #b8b9b6; -fx-border-width: 2");        /*         * a cell is the square of a widget.         * for widget like filter & mixer, it will have 3 sub parts.         * for widget like sine wave, square wave & VF sine wave, it will have 2 sub parts.         */        ///////////// MIDDLE PART ///////////////        middlePart_ = new VBox();        getStyle(middlePart_);        middlePart_.setAlignment(Pos.CENTER);        middlePart_.setOnMouseDragged(this::DragCell);        Text title = new Text(widgetType);        title.setFill(Color.WHITE);        middlePart_.getChildren().add(title);        if (widgetType.equals("Sine Wave") || widgetType.equals("Square Wave")){            widgetSlider_ = CreateSlider(163,988,440,75,5);        }else if (widgetType.equals("Filter") || widgetType.equals("Mixer") || widgetType.equals("VF Sine Wave")){            widgetSlider_ = CreateSlider(0,100,50,10,5);        }        middlePart_.getChildren().add(widgetSlider_);        ///////////////// RIGHT PART ////////////////////        rightPart_ = new VBox();        getStyle(rightPart_);        char cross = '\u2716';        String c = Character.toString(cross);        Button closeBtn = new Button(c);        closeBtn.setOnAction(e -> CloseACell());        rightPart_.getChildren().add(closeBtn);        outputCircle_ = new Circle(10);        outputCircle_.setFill(Paint.valueOf("WHITE"));        rightPart_.getChildren().add(outputCircle_);        outputCircle_.setOnMousePressed(this::StartALine);        outputCircle_.setOnMouseDragged(this::DrawALine);        outputCircle_.setOnMouseReleased(this::StopALine);        //////////////// LEFT PART /////////////////        if (widgetType.equals("Filter") || widgetType.equals("Mixer")){            setLeft();        }        //////////////// SETTINGS ///////////////////        cell_.getChildren().add(middlePart_);        cell_.getChildren().add(rightPart_);        this.getChildren().add(cell_);        center_.getChildren().add(this);        AnchorPane.setLeftAnchor(this, 80.0);        AnchorPane.setTopAnchor(this, 80.0);    }    private void setLeft(){        VBox leftPart = new VBox();        leftPart.setAlignment(Pos.BOTTOM_CENTER);        leftPart.setPadding(new Insets(6));        inputCircle_ = new Circle(10);        inputCircle_.setFill(Paint.valueOf("WHITE"));        leftPart.getChildren().add(inputCircle_);        cell_.getChildren().add(leftPart);    }    protected void DragCell(MouseEvent e) {        System.out.println("Dragging cell to: (x " + e.getSceneX() + ", y " + e.getSceneY() + ") ...");    }    protected void CloseACell() {        System.out.println("Cell window closed ... ");    }    private void StartALine(MouseEvent e) {        Bounds bounds = center_.getBoundsInParent();        if (outputLine_ == null ) {            outputLine_ = new Line();            outputLine_.setStrokeWidth(3);            outputLine_.setStroke(Color.WHITE);            outputLine_.setStartX(e.getSceneX() - bounds.getMinX());            outputLine_.setStartY(e.getSceneY() - bounds.getMinY());            outputLine_.setEndX(e.getSceneX() - bounds.getMinX());            outputLine_.setEndY(e.getSceneY() - bounds.getMinY());            center_.getChildren().add(outputLine_);        }    }    private void DrawALine(MouseEvent e) {        Bounds bounds = center_.getBoundsInParent();        if (outputLine_ != null){            outputLine_.setEndX(e.getSceneX() - bounds.getMinX());            outputLine_.setEndY(e.getSceneY() - bounds.getMinY());        }    }    protected void StopALine(MouseEvent e) {    }    private void getStyle(VBox box){        box.setSpacing(10);        box.setPadding(new Insets(2));        box.setAlignment(Pos.CENTER);    }    private Slider CreateSlider(int min, int max, int defaultValue, int majorTickUnit, int minorTickCount) {        Slider slider = new Slider();        slider.setMin(min);        slider.setMax(max);        slider.setValue(defaultValue);        slider.setShowTickLabels(true);        slider.setShowTickMarks(true);        slider.setMajorTickUnit(majorTickUnit);        slider.setMinorTickCount(minorTickCount);        slider.setBlockIncrement(10);        return slider;    }}