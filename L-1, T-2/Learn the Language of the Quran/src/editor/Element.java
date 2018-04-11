package editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Element extends PageProperties implements Serializable {

    TitledPane it = null;
    TitledPane xm = null;
    TitledPane pn = null;
    VBox content = new VBox();

    SerializedButton item;
    SerializedButton pronunciation;

    SerializedButton parentBtn;

    ArrayList<SerializedButton> example = new ArrayList<>();
    int exampleNo = 0;

    SerializedButton nwButton;
    private Pagination pagination;

    Element() {
        item = new SerializedButton();
        item.setFont(Font.font("Times new roman", 50));
        pronunciation = new SerializedButton();
        pronunciation.setFont(Font.font("Times new roman", 30));

        parentBtn = new SerializedButton();
        setContent();
        pagination.setPageCount(1);
    }

    //Creates the content for a single page
    private SerializedButton createEample(int pageIndex) {
        if (example.size() <= 1) {
            pageIndex = 0;
        } else {
            pageIndex %= example.size();
        }
        SerializedButton button = example.get(pageIndex);
        button.setAlignment(Pos.CENTER);
        return example.get(pageIndex);
    }

    @Override
    void setContent() {
        content = new VBox();
        pagination = new Pagination();
        pagination.setPageCount(exampleNo);
        item.setStyle("-fx-background-color: transparent;");
        item.setFont(Font.font("Times new roman", 50));

        pronunciation.setStyle("-fx-background-color: transparent;");
        pronunciation.setFont(Font.font("Times new roman", 30));

        if (exampleNo > 0 || isEditable) {
            pagination.setPageFactory((Integer pageIndex) -> createEample(pageIndex));
        }
        //Style can be numeric page indicators or bullet indicators

        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        content.getChildren().addAll(item, pronunciation, pagination);
        VBox.setVgrow(pagination, Priority.ALWAYS);
    }

    @Override
    Parent getContent() {
        return content;
    }

    @Override
    void setReader() {
        if (nwButton != null) {
            example.remove(nwButton);
        }
        pagination.setPageCount(exampleNo);
        if (exampleNo == 0) {
            pagination.setVisible(false);
        }

        item.setOnMouseClicked(event -> {
            AudioClip alphAudio = new AudioClip(new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + parentBtn.audio).toURI().toString());
            alphAudio.play();
        });
        pronunciation.setOnMouseClicked(event -> {
            AudioClip audioClip = new AudioClip(new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + parentBtn.audio).toURI().toString());
            audioClip.play();
        });

        example.forEach(event -> {
            event.setOnMouseClicked(e -> {
                AudioClip audioClip = new AudioClip(new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + event.audio).toURI().toString());
                audioClip.play();
            });
        });

    }

    @Override
    void setEditor() {
        
        pronunciation.createContent(pn,false);
        pronunciation.delete.setVisible(false);
        item.createContent(it,false);
        item.delete.setVisible(false);
        
        example.forEach(item -> {
            item.setOnAction(value -> {
                item.createContent(xm,true);
            });
            item.delete.setOnMouseClicked(event -> {
                example.remove(item);
                exampleNo--;
                xm.setContent(null);
                pagination.setPageCount(exampleNo + 1);
            });
        });

        if (nwButton != null) {
            example.remove(nwButton);
        }
        nwButton = new SerializedButton();
        nwButton.setText("+");
        nwButton.setFont(Font.font(50));
        nwButton.setPrefHeight(300);
        nwButton.setPrefWidth(300);
        example.add(nwButton);
        nwButton.setOnMouseClicked(clk -> {
            SerializedButton xmpl = new SerializedButton(300, 300);
            xmpl.setOnAction(value -> {
                xmpl.createContent(xm,true);
            });
            xmpl.createContent(xm,true);
            example.set(exampleNo, xmpl);
            example.add(nwButton);
            exampleNo++;
            pagination.setPageCount(exampleNo + 1);
            pagination.setCurrentPageIndex(exampleNo - 1);
        });
        pagination.setPageCount(exampleNo + 1);
    }

    @Override
    Parent getEditor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeObject(item);
        s.writeObject(parentBtn);
        s.writeObject(pronunciation);
        s.writeInt(exampleNo);
        for (int i = 0; i < exampleNo; i++) {
            try {
                s.writeObject(example.get(i));
            } catch (IOException ex) {
                Logger.getLogger(Element.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        example = new ArrayList<>();
        item = new SerializedButton();
        pronunciation = new SerializedButton();
        parentBtn = new SerializedButton();

        item = (SerializedButton) s.readObject();
        parentBtn = (SerializedButton) s.readObject();
        pronunciation = (SerializedButton) s.readObject();
        exampleNo = s.readInt();
        for (int i = 0; i < exampleNo; i++) {
            SerializedButton button = (SerializedButton) s.readObject();
            example.add(button);
        }
        setContent();
    }

}
