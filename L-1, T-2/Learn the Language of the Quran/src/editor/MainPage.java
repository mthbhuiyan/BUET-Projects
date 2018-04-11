package editor;

import static editor.PageProperties.classPosition;
import static editor.PageProperties.putNewPage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import networking.ClientThread;

public class MainPage extends PageProperties implements Serializable {

    StackPane board;
    GridPane buttonContainer;
    SerializedButton chapter;
    SerializedButton quiz;
    SerializedButton settings;
    SerializedButton exit;

    String chapterTxt;
    String quizTxt;
    String settingsTxt;
    String exitTxt;

    ChaptersListPage chapterList;
    QuizListPage quizList;
    Settings settingsObj;

    String servIp;
    int servPort;

    Label space;
    Label arcW;
    Label arcH;

    Text header;
    Accordion accordion;

    MainPage() {
        chapterTxt = "অধ্যায়" + " - " + "قسم";
        quizTxt = "কুইজ" + " - " + "لغز";
        settingsTxt = "সেটিংস" + " - " + "إعدادات";
        exitTxt = "প্রস্থান" + " - " + "خروج";

        servIp = Settings.SERVER_NAME;
        servPort = Settings.SERVER_PORT;
        chapterList = new ChaptersListPage();
        quizList = new QuizListPage();
        setContent();
        setEditor();
    }

    @Override
    void setContent() {

        board = new StackPane();
        buttonContainer = new GridPane();
        btnShape = new Rectangle();

        btnShape.setWidth(100);
        btnShape.setHeight(50);

        chapter = new SerializedButton(chapterTxt);
        chapter.setMaxWidth(Double.MAX_VALUE);
        chapter.setShape(btnShape);

        quiz = new SerializedButton(quizTxt);
        quiz.setMaxWidth(Double.MAX_VALUE);
        quiz.setShape(btnShape);

        settings = new SerializedButton(settingsTxt);
        settings.setMaxWidth(Double.MAX_VALUE);
        settings.setShape(btnShape);

        exit = new SerializedButton(exitTxt);
        exit.setMaxWidth(Double.MAX_VALUE);
        exit.setShape(btnShape);

        settingsObj = new Settings();
        buttonContainer.addColumn(0, chapter, quiz, settings, exit);
        buttonContainer.setAlignment(Pos.CENTER);
        board.getChildren().add(buttonContainer);
    }

    @Override
    public Parent getContent() {
        Settings.headerText.setText(Settings.APP_FILE_NAME);
        return board;
    }

    @Override
    void setReader() {
        chapter.setOnMouseClicked(event -> {
            chapterList.setReaderTab();
            putNewPage(chapterList);
            Settings.headerText.setText(chapter.getText());
        });

        quiz.setOnMouseClicked(event -> {
            quizList.setReaderTab();
            putNewPage(quizList);
            Settings.headerText.setText(quiz.getText());
        });

        settings.setOnMouseClicked(event -> {
            PageProperties.root.setCenter(settingsObj.getContent());
            putNewPage(settingsObj);
            Settings.headerText.setText(settings.getText());
        });

        exit.setOnMouseClicked(event -> {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("temporary.wlrn"))) {
                out.writeObject(this);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            exit(0);
        });
    }

    @Override
    void setEditor() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        GridPane editor = new GridPane();

        Label textFont = new Label("Text Font");
        Label textSize = new Label("Text Size");
        Label textColor = new Label("Text Color");
        Label btnColor = new Label("Button Color");
        Label btnArcWidth = new Label("Arc Width");
        Label btnArcHeight = new Label("Arc Height");
        Label btnSpacing = new Label("Button Spacing");
        Label bgColor = new Label("Background Color");
        Label bgImage = new Label("Background Image");

        editor.addColumn(0, textFont, textSize, textColor, btnColor,
                btnArcWidth, btnArcHeight, btnSpacing, bgColor, bgImage);

        ObservableList<String> faces = FXCollections.observableArrayList(Font.getFontNames());
        ChoiceBox setfontFace = new ChoiceBox(faces);
        setfontFace.setValue(chapter.getFont().getName());
        setfontFace.setOnAction(e -> {
            txtFont = new Font((String) setfontFace.getValue(), (double) chapter.getFont().getSize());
            chapter.setFont(txtFont);
            quiz.setFont(txtFont);
            settings.setFont(txtFont);
            exit.setFont(txtFont);
        });

        Spinner setfontSize = new Spinner(5.0, 200.0, chapter.getFont().getSize(), 2.0);
        setfontSize.setOnMouseClicked(e -> {
            txtFont = new Font((String) setfontFace.getValue(), (double) setfontSize.getValue());
            chapter.setFont(txtFont);
            quiz.setFont(txtFont);
            settings.setFont(txtFont);
            exit.setFont(txtFont);
        });

        ColorPicker pickTextColor = new ColorPicker();
        pickTextColor.setValue(Color.web(chapter.getTextFill().toString(), 1.0));
        chapter.textFillProperty().bind(pickTextColor.valueProperty());
        quiz.textFillProperty().bind(pickTextColor.valueProperty());
        settings.textFillProperty().bind(pickTextColor.valueProperty());
        exit.textFillProperty().bind(pickTextColor.valueProperty());

        ColorPicker pickBaseColor = new ColorPicker();
        if (!btnBaseColor.equals("/")) {
            pickBaseColor.setValue(Color.web("rgb(" + btnBaseColor + ")", 1.0));
        }
        pickBaseColor.setOnAction(e -> {
            btnBaseColor = (int) (pickBaseColor.getValue().getRed() * 255) + ",";
            btnBaseColor += (int) (pickBaseColor.getValue().getGreen() * 255) + ",";
            btnBaseColor += (int) (pickBaseColor.getValue().getBlue() * 255);
            chapter.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            quiz.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            settings.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            exit.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
        });

        Slider arcWSlider = new Slider(0, 100, arcWidth);
        arcW = new Label(Integer.toString((int) arcWSlider.getValue()));
        arcWSlider.prefWidthProperty().subtract(arcW.widthProperty());
        arcWSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            btnShape.setArcWidth(new_val.doubleValue());
            arcW.setText(String.format("%.0f", new_val));
            arcWidth = new_val.intValue();
        });

        Slider arcHSlider = new Slider(0, 50, arcHeight);
        arcH = new Label(Integer.toString((int) arcHSlider.getValue()));
        arcHSlider.prefWidthProperty().subtract(arcH.widthProperty());
        arcHSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            btnShape.setArcHeight(new_val.doubleValue());
            arcH.setText(String.format("%.0f", new_val));
            arcHeight = new_val.intValue();
        });

        Slider spaceSlider = new Slider(0, 20, spacing);
        space = new Label(Integer.toString((int) spaceSlider.getValue()));
        spaceSlider.prefWidthProperty().subtract(space.widthProperty());
        spaceSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            buttonContainer.setVgap(new_val.doubleValue());
            space.setText(String.format("%.0f", new_val));
            spacing = new_val.intValue();
        });

        ColorPicker pickBgColor = new ColorPicker();
        if (!backColor.equals("/")) {
            pickBgColor.setValue(Color.web("rgb(" + backColor + ")", 1.0));
        }
        pickBgColor.setOnAction(event -> {
            backColor = (int) (pickBgColor.getValue().getRed() * 255) + ",";
            backColor += (int) (pickBgColor.getValue().getGreen() * 255) + ",";
            backColor += (int) (pickBgColor.getValue().getBlue() * 255);
            board.setStyle("-fx-background-color: rgb(" + backColor + ");");
        });

        Button loadImage = new Button("Browse");
        loadImage.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                File afile = new File(file.getPath());
                File bfile = new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + "/Image/" + file.getName());
                if (!bfile.getParentFile().exists()) {
                    bfile.getParentFile().mkdirs();
                }
                try {
                    bfile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(SerializedButton.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println("Present Project Directory : " + System.getProperty("user.dir"));
                try (InputStream inStream = new FileInputStream(afile); OutputStream outStream = new FileOutputStream(bfile);) {

                    byte[] buffer = new byte[1024];

                    int length;
                    //copy the file content in bytes
                    while ((length = inStream.read(buffer)) > 0) {

                        outStream.write(buffer, 0, length);

                    }
                } catch (IOException ex) {
                    Logger.getLogger(SerializedButton.class.getName()).log(Level.SEVERE, null, ex);
                }
                backImg = "Image/" + file.getName();
                Image img = new Image("file:" + Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + backImg);
                img.widthProperty().isEqualTo(board.prefWidthProperty());

                BackgroundImage myBI = new BackgroundImage(img,
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);
                //then you set to your node
                board.setBackground(new Background(myBI));
            }
        });

        editor.addColumn(1, setfontFace, setfontSize, pickTextColor, pickBaseColor,
                new HBox(arcWSlider, arcW), new HBox(arcHSlider, arcH), new HBox(spaceSlider, space),
                pickBgColor, loadImage);

        editor.setPadding(new Insets(10, 10, 10, 10));
        editor.setVgap(10);
        editor.setHgap(10);

        TitledPane t1 = new TitledPane("Page Editor", editor);
        TitledPane t2 = new TitledPane();
        t2.setText("Button Editor");
        accordion = new Accordion();
        accordion.getPanes().add(t1);
        accordion.getPanes().add(t2);

        chapter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            long currentTime = 0, lastTime = 0;

            @Override
            public void handle(MouseEvent t) {

                long diff = 0;

                currentTime = System.currentTimeMillis();

                if (lastTime != 0 && currentTime != 0) {
                    diff = currentTime - lastTime;

                    if (diff <= 215) {
                        chapterList.setEditorTab();
                        putNewPage(chapterList);
                        Settings.headerText.setText(chapter.getText());
                    } else {
                        chapter.createContent(t2, false);
                        chapter.delete.setDisable(true);
                    }
                } else {
                    chapter.createContent(t2, false);
                    chapter.delete.setDisable(true);
                }

                lastTime = currentTime;
            }

        });

        quiz.setOnMouseClicked(e -> {
            quizList.setEditorTab();
            putNewPage(quizList);
        });
        quiz.setOnMouseClicked(new EventHandler<MouseEvent>() {
            long currentTime = 0, lastTime = 0;

            @Override
            public void handle(MouseEvent t) {

                long diff = 0;

                currentTime = System.currentTimeMillis();

                if (lastTime != 0 && currentTime != 0) {
                    diff = currentTime - lastTime;

                    if (diff <= 215) {
                        quizList.setEditorTab();
                        putNewPage(quizList);
                        Settings.headerText.setText(quiz.getText());
                    } else {
                        quiz.createContent(t2, false);
                        quiz.delete.setDisable(true);
                    }
                } else {
                    quiz.createContent(t2, false);
                    quiz.delete.setDisable(true);
                }

                lastTime = currentTime;
            }

        });

        settings.setOnMouseClicked(event -> {
            settings.createContent(t2, false);
            settings.delete.setDisable(true);
            Settings.headerText.setText(settings.getText());
        });

        exit.setOnMouseClicked(event -> {
            exit.createContent(t2, false);
            exit.delete.setDisable(true);
        });

    }

    @Override
    public Parent getEditor() {
        return accordion;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeUTF(chapter.getFont().getName());
        s.writeDouble(chapter.getFont().getSize());
        s.writeUTF(chapter.getTextFill().toString());
        s.writeUTF(btnBaseColor);
        s.writeInt(arcWidth);
        s.writeInt(arcHeight);
        s.writeInt(spacing);
        s.writeUTF(backColor);
        s.writeUTF(backImg);

        s.writeUTF(chapter.getText());
        s.writeUTF(quiz.getText());
        s.writeUTF(settings.getText());
        s.writeUTF(exit.getText());

        s.writeObject(chapterList);
        s.writeObject(quizList);
        s.writeUTF(servIp);
        s.writeInt(servPort);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        String face = s.readUTF();
        double size = s.readDouble();
        txtColor = Paint.valueOf(s.readUTF());
        btnBaseColor = s.readUTF();
        arcWidth = s.readInt();
        arcHeight = s.readInt();
        spacing = s.readInt();
        backColor = s.readUTF();
        backImg = s.readUTF();

        chapterTxt = s.readUTF();
        quizTxt = s.readUTF();
        settingsTxt = s.readUTF();
        exitTxt = s.readUTF();

        chapterList = (ChaptersListPage) s.readObject();
        quizList = (QuizListPage) s.readObject();

        Settings.SERVER_NAME = servIp = s.readUTF();
        Settings.SERVER_PORT = servPort = s.readInt();
        
        setContent();
        
        buttonContainer.setVgap(spacing);
        if (!backColor.equals("/") && backImg.equals("/")) {
            board.setStyle("-fx-background-color: rgb(" + backColor + ");");
        }
        if (!backImg.equals("/")) {
            BackgroundImage myBI = new BackgroundImage(new Image("file:" + Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + backImg),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            //then you set to your node
            board.setBackground(new Background(myBI));
        }

        btnShape.setArcWidth(arcWidth);
        btnShape.setArcHeight(arcHeight);

        txtFont = Font.font(face, size);

        chapter.setFont(txtFont);
        chapter.setTextFill(txtColor);
        if (!btnBaseColor.equals("/")) {
            chapter.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
        }

        quiz.setFont(txtFont);
        quiz.setTextFill(txtColor);
        if (!btnBaseColor.equals("/")) {
            quiz.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
        }

        settings.setFont(txtFont);
        settings.setTextFill(txtColor);
        if (!btnBaseColor.equals("/")) {
            settings.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
        }

        exit.setFont(txtFont);
        exit.setTextFill(txtColor);
        if (!btnBaseColor.equals("/")) {
            exit.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
        }

        setEditor();
    }

}
