import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SketchIt extends Application {
    // constants
    static final double WINDOW_MIN_WIDTH = 640;
    static final double  WINDOW_PREF_WIDTH = 1600;
    static final double  WINDOW_MAX_WIDTH = 1920;
    static final double WINDOW_MIN_HEIGHT = 480;
    static final double WINDOW_PREF_HEIGHT = 1200;
    static final double WINDOW_MAX_HEIGHT = 1440;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Sketch It");

        Model model = new Model();

        CanvasView canvasView = new CanvasView(model);
        PropertyView propertyView = new PropertyView(model);
        ToolbarView toolbarView = new ToolbarView(model);
        MenubarView menubarView = new MenubarView(model);

        VBox left = new VBox(toolbarView, propertyView);
        left.setPadding(new Insets(5.0));
        left.setSpacing(20.0);

        BorderPane root = new BorderPane();
        root.setTop(menubarView);
        root.setLeft(left);
        root.setCenter(canvasView);
        root.setMinSize(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT);
        root.setPrefSize(WINDOW_PREF_WIDTH, WINDOW_PREF_HEIGHT);
        root.setMaxSize(WINDOW_MAX_WIDTH, WINDOW_MAX_HEIGHT);

        Scene scene = new Scene(root, WINDOW_PREF_WIDTH, WINDOW_PREF_HEIGHT);
        stage.setScene(scene);

        scene.setOnKeyPressed(e -> {
            if (model.getCurrentTool() == ToolbarView.Tool.SELECT) {
                if (e.getCode() == KeyCode.ESCAPE) {
                    model.clearSelect();
                } else if (e.getCode() == KeyCode.DELETE
                        && model.getCurrentShape() != null) {
                    model.eraseShape(model.getCurrentShape());
                }
            }
        });
        stage.show();
    }
}
