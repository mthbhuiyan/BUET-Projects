package editor;

import networking.*;
import java.io.File;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Reader extends Application {

    BorderPane root;
    MainPage app;

    public void createContent() {

        FileChooser folderChooser = new FileChooser();
        folderChooser.getExtensionFilters().add(new ExtensionFilter("All files", "File Folder"));

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter("WeLearn files", "*.wlrn"));

        Tooltip openTip = new Tooltip("Open App");
        Tooltip backTip = new Tooltip("Back");
        Tooltip forwardTip = new Tooltip("Forward");

        Button load = new Button();
        ImageView loadImg = new ImageView(new Image("file:Button Faces/Open File.png"));
        loadImg.setFitWidth(30);
        loadImg.setFitHeight(30);
        load.setGraphic(loadImg);
        load.setTooltip(openTip);
        load.setOnAction(e -> {
            File file = chooser.showOpenDialog(new Stage());
            if (file != null) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    File temp = new File(file.getParent());
                    Settings.APP_FILE_NAME = temp.getName();
                    Settings.PARENT_FILE_NAME = temp.getParent();
                    app = (MainPage) in.readObject();
                    app.refresh();
                    PageProperties.pageTransition.clear();
                    PageProperties.pageTransition.add(app);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });

        PageProperties.goBack = new Button();
        ImageView goBackImg = new ImageView(new Image("file:Button Faces/Back2.png"));
        goBackImg.setFitWidth(30);
        goBackImg.setFitHeight(30);
        PageProperties.goBack.setGraphic(goBackImg);
        PageProperties.goBack.setTooltip(backTip);
        PageProperties.goBack.setDisable(true);
        PageProperties.goBack.setOnMouseClicked(event -> {
            if (PageProperties.classPosition > 0) {
                PageProperties.classPosition--;
                PageProperties.pageTransition.get(PageProperties.classPosition).refresh();
                if (PageProperties.classPosition == 0) {
                    PageProperties.goBack.setDisable(true);
                }
                if (PageProperties.goForward.isDisable()) {
                    PageProperties.goForward.setDisable(false);
                }
            }
        });

        PageProperties.goForward = new Button();
        ImageView goForwardImg = new ImageView(new Image("file:Button Faces/Forward2.png"));
        goForwardImg.setFitWidth(30);
        goForwardImg.setFitHeight(30);
        PageProperties.goForward.setGraphic(goForwardImg);
        PageProperties.goForward.setTooltip(forwardTip);
        PageProperties.goForward.setDisable(true);
        PageProperties.goForward.setOnMouseClicked(event -> {
            if (PageProperties.classPosition < (PageProperties.pageTransition.size() - 1)) {
                PageProperties.classPosition++;
                PageProperties.pageTransition.get(PageProperties.classPosition).refresh();
                if (PageProperties.classPosition == (PageProperties.pageTransition.size() - 1)) {
                    PageProperties.goForward.setDisable(true);
                }
                if (PageProperties.goBack.isDisable()) {
                    PageProperties.goBack.setDisable(false);
                }
            }
        });

        PageProperties.isEditable = false;

        Settings.headerText = new Label();
        Settings.headerText.setFont(Font.font("Times new roman", 20));
        Settings.headerText.setAlignment(Pos.CENTER);
        HBox barSpacer = new HBox(Settings.headerText);
        HBox.setHgrow(
                barSpacer,
                Priority.ALWAYS
        );
        barSpacer.setAlignment(Pos.CENTER);

        ToolBar menu = new ToolBar(PageProperties.goBack, PageProperties.goForward, barSpacer, load);

        root.setTop(menu);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        PageProperties.root = this.root;

        createContent();

        if (new File("temporaryReader").exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("temporaryReader"))) {
                Settings.APP_FILE_NAME = in.readUTF();
                Settings.PARENT_FILE_NAME = in.readUTF();
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            File source = new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + Settings.APP_FILE_NAME + ".wlrn");

            if (source.exists()) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(source))) {
                    app = (MainPage) in.readObject();
                    app.refresh();
                    PageProperties.pageTransition.add(app);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
        Scene scene = new Scene(this.root, 800, 450);

        primaryStage.setTitle("Learning App Reader");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("temporaryReader"))) {
            out.writeUTF(Settings.APP_FILE_NAME);
            out.writeUTF(Settings.PARENT_FILE_NAME);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
