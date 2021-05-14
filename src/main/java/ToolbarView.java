import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ToolbarView extends Pane implements IView {
    private static final double GRID_WIDTH = 30;
    private static final double GRID_HEIGHT = 30;
    private static final double IMAGE_WIDTH = 20;
    private static final double IMAGE_HEIGHT = 20;

    private Model model;

    private ToolButton select, erase, line, circle, rectangle, fill;

    enum Tool {SELECT, ERASE, LINE, CIRCLE, RECTANGLE, FILL};

    ToolbarView(Model model) {
        this.model = model;

        // tools widget
        Image selectImage = new Image("selection.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        Image eraseImage = new Image("erase.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        Image lineImage = new Image("line.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        Image circleImage = new Image("circle.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        Image rectangleImage = new Image("rectangle.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        Image fillImage = new Image("fill.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        select = new ToolButton(selectImage);
        erase = new ToolButton(eraseImage);
        line = new ToolButton(lineImage);
        circle = new ToolButton(circleImage);
        rectangle = new ToolButton(rectangleImage);
        fill = new ToolButton(fillImage);

        ToggleGroup tools = new ToggleGroup();
        select.setToggleGroup(tools);
        erase.setToggleGroup(tools);
        line.setToggleGroup(tools);
        circle.setToggleGroup(tools);
        rectangle.setToggleGroup(tools);
        fill.setToggleGroup(tools);

        GridPane grid = new GridPane();
        grid.add(select, 0, 0);
        grid.add(erase, 1, 0);
        grid.add(line, 0, 1);
        grid.add(circle, 1, 1);
        grid.add(rectangle, 0, 2);
        grid.add(fill, 1, 2);
        grid.setPadding(new Insets(50.0));
        grid.setHgap(5.0);
        grid.setVgap(5.0);

        // set up the view
        getChildren().add(grid);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        tools.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle selectedToggle) {
                if (select.isSelected()) {
                    model.setCurrentTool(Tool.SELECT);
                } else if (erase.isSelected()) {
                    model.setCurrentTool(Tool.ERASE);
                } else if (line.isSelected()) {
                    model.setCurrentTool(Tool.LINE);
                } else if (circle.isSelected()) {
                    model.setCurrentTool(Tool.CIRCLE);
                } else if (rectangle.isSelected()) {
                    model.setCurrentTool(Tool.RECTANGLE);
                } else if (fill.isSelected()){
                    model.setCurrentTool(Tool.FILL);
                } else {
                    model.setCurrentTool(null);
                }
            }
        });

        model.addView(this);
    }

    private void setToolButton(Tool tool) {
        if (tool == null) {
            select.setSelected(false);
            erase.setSelected(false);
            line.setSelected(false);
            circle.setSelected(false);
            rectangle.setSelected(false);
            fill.setSelected(false);
            return;
        }
        switch (tool) {
            case SELECT:
                select.setSelected(true);
                break;
            case ERASE:
                erase.setSelected(true);
                break;
            case LINE:
                line.setSelected(true);
                break;
            case CIRCLE:
                circle.setSelected(true);
                break;
            case RECTANGLE:
                rectangle.setSelected(true);
                break;
            case FILL:
                fill.setSelected(true);
                break;
        }
    }

    public class ToolButton extends ToggleButton {
        ToolButton(Image image) {
            super();
            setVisible(true);
            setMinSize(GRID_WIDTH-5, GRID_HEIGHT-5);
            setPrefSize(GRID_WIDTH, GRID_HEIGHT);
            setMaxSize(GRID_WIDTH+5, GRID_HEIGHT+5);
            ImageView imageView = new ImageView(image);
            setGraphic(imageView);
        }

    }

    public void updateView() {
        setToolButton(model.getCurrentTool());
    }
}
