package editor;

import java.io.Serializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import networking.ClientThread;

public class Settings extends PageProperties {

    static String PARENT_FILE_NAME = "";
    static String APP_FILE_NAME = "Language of the Quran";
    static String SERVER_NAME = "192.168.0.100";
    static int SERVER_PORT = 12431;

    static Label headerText;
    static Label servName;
    static Label servPort;
    static TextField nameIn;
    static TextField portIn;

    @Override
    void setContent() {

    }

    @Override
    Parent getContent() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        servName = new Label("Server Name:");
        grid.add(servName, 0, 0);
        servPort = new Label("Server Port");
        grid.add(servPort, 0, 1);
        nameIn = new TextField(SERVER_NAME);
        nameIn.setOnAction(event -> {
            SERVER_NAME = nameIn.getText();
        });
        grid.add(nameIn, 1, 0);
        portIn = new TextField(String.valueOf(SERVER_PORT));
        portIn.setOnAction(event -> {
            SERVER_PORT = Integer.valueOf(portIn.getText());
        });
        grid.add(portIn, 1, 1);

        Button update = new Button("Update");
        update.setOnAction(event -> {
            new ClientThread(Settings.nameIn.getText(), Integer.valueOf(Settings.portIn.getText()), Settings.APP_FILE_NAME, Settings.PARENT_FILE_NAME);
        });
        grid.add(update, 0, 2);

        return grid;
    }

    @Override
    void setEditor() {

    }

    @Override
    Parent getEditor() {
        return null;
    }

    @Override
    void setReader() {

    }

}
