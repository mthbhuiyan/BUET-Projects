package editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class PageProperties {

    Rectangle btnShape = new Rectangle(100, 50);
    static BorderPane root;
    static boolean isEditable;
    static ArrayList<PageProperties> pageTransition = new ArrayList<>();
    static int classPosition = 0;
    static Button goBack;
    static Button goForward;

    static void putNewPage(PageProperties page) {
        classPosition++;
        ArrayList<PageProperties> temp = new ArrayList<>(pageTransition.subList(0, classPosition));
        pageTransition = temp;
        pageTransition.add(page);
        goBack.setDisable(false);
        goForward.setDisable(true);
    }

    static Button done;

    Font txtFont = Font.getDefault();
    Paint txtColor = Color.BLACK;
    String btnBaseColor = "/";
    int arcWidth = 0;
    int arcHeight = 0;
    int spacing = 0;
    String backColor = "/";
    String backImg = "/";

    public void setOkButtonAction() {
        done.setOnAction(event -> {
            setEditable(false);
        });
    }

    public void setEditorTab() {
        setEditor();
        ScrollPane scrl = new ScrollPane(getContent());
        scrl.setFitToWidth(true);
        scrl.setFitToHeight(true);
        scrl.setStyle("-fx-background-color:transparent;");
        root.setCenter(scrl);
        root.setRight(getEditor());
        setOkButtonAction();
        StackPane bottom = new StackPane(done);
        bottom.setPadding(new Insets(10, 10, 10, 10));
        root.setBottom(bottom);
    }

    public void setReaderTab() {
        setReader();
        ScrollPane scrl = new ScrollPane(getContent());
        scrl.setFitToWidth(true);
        scrl.setFitToHeight(true);
        scrl.setStyle("-fx-background-color:transparent;");
        root.setCenter(scrl);
        root.setBottom(null);
    }

    public void refresh() {
        if (isEditable) {
            setEditor();
            setEditorTab();
        } else {
            setReaderTab();
        }
    }

    public void setEditable(boolean editability) {
        isEditable = editability;
        if (isEditable) {
            pageTransition.get(classPosition).refresh();
        } else {
            pageTransition.get(classPosition).refresh();
            root.setRight(null);
        }
    }

    abstract void setContent();

    abstract Parent getContent();

    abstract void setEditor();

    abstract Parent getEditor();

    abstract void setReader();
}
