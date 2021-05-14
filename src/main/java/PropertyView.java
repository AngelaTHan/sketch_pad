import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PropertyView extends Pane implements IView {
    private static final double IMAGE_WIDTH = 15;
    private static final double IMAGE_HEIGHT = 15;

    private Model model;
    private ColorPicker strokeColor, fillColor;
    private LineButton thickness1, thickness2, thickness3, thickness4, thickness5;
    private LineButton style1, style2, style3;

    enum Thickness {
        THICKNESS1 (1), THICKNESS2 (2), THICKNESS3 (3), THICKNESS4 (4), THICKNESS5 (5);
        private int value;
        Thickness(int value) {
            this.value = value;
        }
        public static Thickness fromValue(int value) {
            for (Thickness type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
        public int getValue() {
            return value;
        }
        public void exit() {
            System.exit(value);
        }
    }
    enum Style {
        STYLE1 (1), STYLE2 (5), STYLE3 (0);
        private int value;
        Style(int value) {
            this.value = value;
        }
        public static Style fromValue(int value) {
            for (Style type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
        public int getValue() {
            return value;
        }
        public void exit() {
            System.exit(value);
        }
    }

    PropertyView(Model model) {
        this.model = model;

        Image lineImage = new Image("line.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        ImageView lineView = new ImageView(lineImage);
        strokeColor = new ColorPicker();
        strokeColor.setValue(Color.TRANSPARENT);
        Image fillImage = new Image("fill.png", IMAGE_WIDTH, IMAGE_HEIGHT, false, true);
        ImageView fillView = new ImageView(fillImage);
        fillColor = new ColorPicker();
        fillColor.setValue(Color.TRANSPARENT);

        GridPane colorGrid = new GridPane();
        colorGrid.add(lineView, 0, 0);
        colorGrid.add(strokeColor, 1, 0);
        colorGrid.add(fillView, 0, 1);
        colorGrid.add(fillColor, 1, 1);
        colorGrid.setAlignment(Pos.CENTER);
        colorGrid.setHgap(10.0);
        colorGrid.setVgap(5.0);

        Image thickness1Image = new Image("thickness1.png", IMAGE_WIDTH * 8, IMAGE_HEIGHT, false, true);
        thickness1 = new LineButton(thickness1Image);
        Image thickness2Image = new Image("thickness2.png", IMAGE_WIDTH * 8, IMAGE_HEIGHT, false, true);
        thickness2 = new LineButton(thickness2Image);
        Image thickness3Image = new Image("thickness3.png", IMAGE_WIDTH * 8, IMAGE_HEIGHT, false, true);
        thickness3 = new LineButton(thickness3Image);
        Image thickness4Image = new Image("thickness4.png", IMAGE_WIDTH * 8, IMAGE_HEIGHT, false, true);
        thickness4 = new LineButton(thickness4Image);
        Image thickness5Image = new Image("thickness5.png", IMAGE_WIDTH * 8, IMAGE_HEIGHT, false, true);
        thickness5 = new LineButton(thickness5Image);

        ToggleGroup thickness = new ToggleGroup();
        thickness1.setToggleGroup(thickness);
        thickness2.setToggleGroup(thickness);
        thickness3.setToggleGroup(thickness);
        thickness4.setToggleGroup(thickness);
        thickness5.setToggleGroup(thickness);

        VBox thicknessBox = new VBox(thickness1, thickness2, thickness3, thickness4, thickness5);
        thicknessBox.setAlignment(Pos.CENTER);
        thicknessBox.setSpacing(5.0);

        Image style1Image = new Image("style1.png", IMAGE_WIDTH * 9, IMAGE_HEIGHT, false, true);
        style1 = new LineButton(style1Image);
        Image style2Image = new Image("style2.png", IMAGE_WIDTH * 7, IMAGE_HEIGHT, false, true);
        style2 = new LineButton(style2Image);
        Image style3Image = new Image("style3.png", IMAGE_WIDTH * 9, IMAGE_HEIGHT, false, true);
        style3 = new LineButton(style3Image);

        ToggleGroup style = new ToggleGroup();
        style1.setToggleGroup(style);
        style2.setToggleGroup(style);
        style3.setToggleGroup(style);

        VBox styleBox = new VBox(style1, style2, style3);
        styleBox.setAlignment(Pos.CENTER);
        styleBox.setSpacing(5.0);

        VBox properties = new VBox(colorGrid, thicknessBox, styleBox);
        properties.setPadding(new Insets(5.0));
        properties.setSpacing(10.0);

        getChildren().add(properties);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        strokeColor.setOnAction(e -> {
            model.setStrokeColor(strokeColor.getValue());
        });

        fillColor.setOnAction(e -> {
            model.setFillColor(fillColor.getValue());
        });

        thickness.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle selectedToggle) {
                if (thickness1.isSelected()) {
                    model.setThickness(Thickness.THICKNESS1);
                } else if (thickness2.isSelected()) {
                    model.setThickness(Thickness.THICKNESS2);
                } else if (thickness3.isSelected()) {
                    model.setThickness(Thickness.THICKNESS3);
                } else if (thickness4.isSelected()) {
                    model.setThickness(Thickness.THICKNESS4);
                } else if (thickness5.isSelected()){
                    model.setThickness(Thickness.THICKNESS5);
                } else {
                    model.setThickness(null);
                }
            }
        });

        style.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle selectedToggle) {
                if (style1.isSelected()) {
                    model.setStyle(Style.STYLE1);
                } else if (style2.isSelected()) {
                    model.setStyle(Style.STYLE2);
                } else if (style3.isSelected()){
                    model.setStyle(Style.STYLE3);
                } else {
                    model.setStyle(null);
                }
            }
        });

        model.addView(this);
    }

    public class LineButton extends ToggleButton {
        LineButton(Image image) {
            super();
            setVisible(true);
            setMinSize(IMAGE_WIDTH * 9-5, IMAGE_HEIGHT-5);
            setPrefSize(IMAGE_WIDTH * 9, IMAGE_HEIGHT);
            setMaxSize(IMAGE_WIDTH * 9+5, IMAGE_HEIGHT+5);
            ImageView imageView = new ImageView(image);
            setGraphic(imageView);
        }
    }

    private void setThicknessButton(Thickness thickness) {
        if (thickness == null) {
            thickness1.setSelected(false);
            thickness2.setSelected(false);
            thickness3.setSelected(false);
            thickness4.setSelected(false);
            thickness5.setSelected(false);
            return;
        }
        switch (thickness) {
            case THICKNESS1:
                thickness1.setSelected(true);
                break;
            case THICKNESS2:
                thickness2.setSelected(true);
                break;
            case THICKNESS3:
                thickness3.setSelected(true);
                break;
            case THICKNESS4:
                thickness4.setSelected(true);
                break;
            case THICKNESS5:
                thickness5.setSelected(true);
                break;
        }
    }

    private void setStyleButton(Style style) {
        if (style == null) {
            style1.setSelected(false);
            style2.setSelected(false);
            style3.setSelected(false);
            return;
        }
        switch (style) {
            case STYLE1:
                style1.setSelected(true);
                break;
            case STYLE2:
                style2.setSelected(true);
                break;
            case STYLE3:
                style3.setSelected(true);
                break;
        }
    }

    public void updateView() {
        strokeColor.setValue(model.getStrokeColor());
        fillColor.setValue(model.getFillColor());
        setThicknessButton(model.getThickness());
        setStyleButton(model.getStyle());
    }
}
