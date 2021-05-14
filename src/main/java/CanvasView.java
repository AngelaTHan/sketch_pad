import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class CanvasView extends Pane implements IView {
    private Model model;
    private double startX;
    private double startY;

    CanvasView(Model model) {
        this.model = model;

        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
            switch (model.getCurrentTool()) {
                case LINE: case CIRCLE: case RECTANGLE:
                    model.drawingShape(startX, startY, startX, startY);
                    break;
            }
        });

        setOnMouseDragged(e -> {
            switch (model.getCurrentTool()) {
                case LINE: case CIRCLE: case RECTANGLE:
                    model.drawingShape(startX, startY, e.getX(), e.getY());
                    break;
            }
        });

        setOnMouseReleased(e -> {
            Shape shapeToAct = model.pointedShape(e.getX(), e.getY());
            switch (model.getCurrentTool()) {
                case SELECT:
                    if (shapeToAct != null && model.getCurrentShape() == null) {
                        model.selectShape(shapeToAct);
                    } else if (shapeToAct == null && model.getCurrentShape() != null) {
                        model.clearSelect();
                    }
                    break;
                case ERASE:
                    model.eraseShape(shapeToAct);
                    break;
                case LINE: case CIRCLE: case RECTANGLE:
                    model.finishDrawing();
                    break;
                case FILL:
                    model.fillShape(shapeToAct);
                    break;
            }
        });

        model.addView(this);
    }

    public void updateView() {
        getChildren().clear();
        getChildren().addAll(model.getShapes());
    }
}
