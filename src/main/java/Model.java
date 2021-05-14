import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Model {
    // constants
    private static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
    private static final Color DEFAULT_FILL_COLOR = Color.BLACK;
    private static final PropertyView.Thickness DEFAULT_THICKNESS = PropertyView.Thickness.THICKNESS3;
    private static final PropertyView.Style DEFAULT_STYLE = PropertyView.Style.STYLE3;

    private Map<String, List<Shape>> files = new HashMap<>(); // only contains saved files
    private List<IView> views = new ArrayList<IView>();
    private List<Shape> shapes = new ArrayList<Shape>();

    private String fileName;
    private boolean saved = true;
    private Shape copiedShape;
    private int pasteTime;
    private boolean isCopy;
    private Shape currentShape;
    private ToolbarView.Tool currentTool;
    private Color strokeColor;
    private Color fillColor;
    private PropertyView.Thickness thickness;
    private PropertyView.Style style;

    // getters and setters
    public List<Shape> getShapes() {
        return shapes;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public ToolbarView.Tool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(ToolbarView.Tool currentTool) {
        this.currentTool = currentTool;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public PropertyView.Thickness getThickness() {
        return thickness;
    }

    public void setThickness(PropertyView.Thickness thickness) {
        this.thickness = thickness;
    }

    public PropertyView.Style getStyle() {
        return style;
    }

    public void setStyle(PropertyView.Style style) {
        this.style = style;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // private helpers
    private void notifyObservers() {
        for (IView view : this.views) {
            view.updateView();
        }
        saved = false;
    }

    private void patchProperties() {
        strokeColor = (strokeColor == null) ? DEFAULT_STROKE_COLOR : strokeColor;
        fillColor = (fillColor == null) ? DEFAULT_FILL_COLOR : fillColor;
        thickness = (thickness == null) ? DEFAULT_THICKNESS : thickness;
        style = (style == null) ? DEFAULT_STYLE : style;
    }

    private void clearProperties() {
        strokeColor = null; // TODO: may cause error in PropertyView.updateView
        fillColor = null; // TODO: may cause error in PropertyView.updateView
        thickness = null;
        style = null;
    }

    private void clearFile() {
        clearProperties();
        shapes = new ArrayList<Shape>();
        fileName = null;
        saved = true;
        currentShape = null;
        currentTool = null;
    }

    private void updateProperties() {
        strokeColor = (Color) currentShape.getStroke();
        fillColor = (currentShape instanceof Line) ? null : (Color) currentShape.getFill(); // TODO: may cause error in PropertyView.updateView
        thickness = PropertyView.Thickness.fromValue((int) currentShape.getStrokeWidth());
        style = PropertyView.Style.fromValue(currentShape.getStrokeDashArray().size());
    }

    private void copyProperties(Shape newShape, Shape oldShape) {
        newShape.setStroke(oldShape.getStroke());
        newShape.setFill(oldShape.getFill());
        newShape.setStrokeWidth(oldShape.getStrokeWidth());
        newShape.getStrokeDashArray().addAll(oldShape.getStrokeDashArray());
    }

    private void cutProperties(Shape newShape, Shape oldShape) {
        newShape.setStroke(oldShape.getStroke());
        newShape.setFill(oldShape.getFill());
        newShape.setStrokeWidth(oldShape.getStrokeWidth()-5);
        newShape.getStrokeDashArray().addAll(oldShape.getStrokeDashArray());
    }

    private void setDrawProperties() {
        currentShape.setStroke(strokeColor);
        currentShape.setFill(fillColor);
        currentShape.setStrokeWidth(thickness.getValue());
        switch (style) {
            case STYLE1:
                currentShape.getStrokeDashArray().addAll(10d);
                break;
            case STYLE2:
                currentShape.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);
                break;
        }
    }

    private void setCurrentShape(Shape shape) { // and bring it front
        shapes.remove(shape);
        shapes.add(shape);
        currentShape = shape;
    }

    //public helpers
    public void addView(IView view) {
        views.add(view);
    }

    public void drawingShape(double x1, double y1, double x2, double y2) {
        double deltaX = abs(x2 - x1);
        double deltaY = abs(y2 - y1);
        shapes.remove(currentShape);
        patchProperties();
        switch (currentTool) {
            case LINE:
                currentShape = new Line(x1, y1, x2, y2);
                break;
            case CIRCLE:
                double radius = max(deltaX, deltaY);
                double centerX = x2;
                double centerY = y2;
                currentShape = new Circle(centerX, centerY, radius, fillColor);
                break;
            case RECTANGLE:
                currentShape = new Rectangle(x1, y1, deltaX, deltaY);
                break;
        }
        setDrawProperties();
        shapes.add(currentShape);
        notifyObservers();
    }

    public void finishDrawing() {
        currentShape = null;
        currentTool = ToolbarView.Tool.SELECT;
        clearProperties();
        notifyObservers();
    }

    public Shape pointedShape(double x, double y) {
        for (int i=shapes.size()-1; i >= 0; i--) {
            if (shapes.get(i).contains(x, y)) {
                return shapes.get(i);
            }
        }
        return null;
    }

    public void selectShape(Shape shape) {
        setCurrentShape(shape);
        updateProperties();
        currentShape.setStrokeWidth(currentShape.getStrokeWidth()+5);
        notifyObservers();
    }

    public void clearSelect() {
        currentShape.setStrokeWidth(currentShape.getStrokeWidth()-5);
        currentShape.setStroke(strokeColor);
        currentShape = null;
        clearProperties();
        notifyObservers();
    }

    public void eraseShape(Shape shape) {
        shapes.remove(shape);
        currentShape = null;
        currentTool = ToolbarView.Tool.SELECT;
        clearProperties();
        notifyObservers();
    }

    public void fillShape(Shape shape) {
        setCurrentShape(shape);
        currentShape.setFill(fillColor);
        currentTool = ToolbarView.Tool.SELECT;
        updateProperties();
        notifyObservers();
    }

    public void newFile() {
        clearFile();
        notifyObservers();
    }

    public void loadFile(String name) {
        List<Shape> currentFile = files.get(name);
        shapes = currentFile;
        clearProperties();
        notifyObservers();
    }

    public void saveFile() {
        files.put(fileName, shapes);
        saved = true;
    }

    public List<String> getSavedFileNames() {
        List<String> names = new ArrayList<>();
        names.addAll(files.keySet());
        return names;
    }

    public void copyShape() {
        pasteTime = 0;
        isCopy = true;
        copiedShape = currentShape;
    }

    public void cutShape() {
        pasteTime = 0;
        isCopy = false;
        copiedShape = currentShape;
        eraseShape(currentShape);
    }

    public void pasteShape() {
        pasteTime++;
        boolean isLine = copiedShape instanceof Line;
        boolean isCircle = copiedShape instanceof Circle;
        boolean isRectangle = copiedShape instanceof Rectangle;
        double offset = 50 * pasteTime;

        if (isLine) {
            double startX = ((Line) copiedShape).getStartX();
            double startY = ((Line) copiedShape).getStartY();
            double endX = ((Line) copiedShape).getEndX();
            double endY = ((Line) copiedShape).getEndY();
            Line newLine = new Line(startX+offset, startY+offset, endX+offset, endY+offset);
            if (isCopy) {
                copyProperties(newLine, copiedShape);
            } else {
                cutProperties(newLine, copiedShape);
            }
            shapes.add(newLine);
        } else if (isCircle) {
            double centerX = ((Circle) copiedShape).getCenterX();
            double centerY = ((Circle) copiedShape).getCenterY();
            double radius = ((Circle) copiedShape).getRadius();
            Paint fill = ((Circle) copiedShape).getFill();
            Circle newCircle = new Circle(centerX+offset, centerY+offset, radius, fill);
            if (isCopy) {
                copyProperties(newCircle, copiedShape);
            } else {
                cutProperties(newCircle, copiedShape);
            }
            shapes.add(newCircle);
        } else if (isRectangle) {
            double x = ((Rectangle) copiedShape).getX();
            double y = ((Rectangle) copiedShape).getY();
            double width = ((Rectangle) copiedShape).getWidth();
            double height = ((Rectangle) copiedShape).getHeight();
            Rectangle newRectangle = new Rectangle(x+offset, y+offset, width, height);
            if (isCopy) {
                copyProperties(newRectangle, copiedShape);
            } else {
                cutProperties(newRectangle, copiedShape);
            }
            shapes.add(newRectangle);
        }
        if (currentShape != null) {
            clearSelect();
        }
        notifyObservers();
    }
}
