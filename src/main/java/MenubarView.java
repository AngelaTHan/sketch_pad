import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class MenubarView extends Pane implements IView {
    private Model model;

    MenubarView(Model model) {
        // attributes
        this.model = model;

        // menu bar
        MenuBar menubar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        MenuItem loadFile = new MenuItem("Load");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem quitFile = new MenuItem("Quit");
        file.getItems().addAll(newFile, loadFile, saveFile, quitFile);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().add(about);

        Menu edit = new Menu("Edit");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        edit.getItems().addAll(cut, copy, paste);

        menubar.getMenus().addAll(file, help, edit);

        // set up the view
        setMinWidth(SketchIt.WINDOW_MIN_WIDTH);
        setPrefWidth(SketchIt.WINDOW_PREF_WIDTH);
        setMaxWidth(SketchIt.WINDOW_MAX_WIDTH);
        getChildren().add(menubar);

        newFile.setOnAction(e -> {
            if (model.isSaved()) {
                model.newFile();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sketch It");
                alert.setHeaderText("New file");
                alert.setContentText("You have unsaved work. You will lose your changes if you proceed to create a new file. Are you ok with this?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.newFile();
                }
            }
        });

        loadFile.setOnAction(e -> {
            List<String> choices = model.getSavedFileNames();
            if (choices.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sketch It");
                alert.setHeaderText("Load file");
                alert.setContentText("You do not have any saved files");
                alert.showAndWait();
            } else {
                ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                dialog.setTitle("Sketch It");
                dialog.setHeaderText("Load file");
                dialog.setContentText("choose from saved files:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    model.loadFile(result.get());
                }
            }
        });

        saveFile.setOnAction(e -> {
            if (model.getFileName() == null) {
                TextInputDialog dialog = new TextInputDialog("untitled");
                dialog.setTitle("Sketch It");
                dialog.setHeaderText("Save file");
                dialog.setContentText("Please enter the file name:");

                Optional<String> result = dialog.showAndWait();
                List<String> fileNames = model.getSavedFileNames();
                if (result.isPresent()){
                    String userGivenName = result.get();
                    if (fileNames.contains(userGivenName)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Sketch It");
                        alert.setHeaderText("Save file");
                        alert.setContentText("Name already exist. The current file will override the previous. Is it okay?");
                        Optional<ButtonType> overrideResult = alert.showAndWait();
                        if (overrideResult.get() != ButtonType.OK) {
                            return;
                        }
                    }
                    model.setFileName(userGivenName);
                }
            }
            model.saveFile();
        });

        quitFile.setOnAction(e -> {
            Platform.exit();
        });

        about.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sketch It");
            alert.setHeaderText("About us: Sketch It");
            alert.setContentText("Tingqian Han, t37han");
            alert.showAndWait();
        });

        cut.setOnAction(e -> {
            if (model.getCurrentShape() != null) {
                model.cutShape();
//                model.clearSelect();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sketch It");
                alert.setHeaderText("Cut shape");
                alert.setContentText("No shape has been selected. Please retry after selecting a shape");
                alert.showAndWait();
            }
        });

        copy.setOnAction(e -> {
            if (model.getCurrentShape() != null) {
                model.copyShape();
                model.clearSelect();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sketch It");
                alert.setHeaderText("Copy shape");
                alert.setContentText("No shape has been selected. Please retry after selecting a shape");
                alert.showAndWait();
            }
        });

        paste.setOnAction(e -> {
            model.pasteShape();
        });

        model.addView(this);
    }

    public void updateView() {
        // do nothing
    }
}
