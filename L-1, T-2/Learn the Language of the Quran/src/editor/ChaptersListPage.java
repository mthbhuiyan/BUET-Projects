package editor;

import static editor.PageProperties.classPosition;
import static editor.PageProperties.goBack;
import static editor.PageProperties.pageTransition;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class ChaptersListPage extends PageProperties implements Serializable {

    StackPane board;
    TilePane buttonContainer;
    ArrayList<ChapterPage> chapterList = new ArrayList<>();
    int chaptersNo = 0;

    boolean isLTR;

    Label space;
    Label arcW;
    Label arcH;

    Button nwButton;
    Accordion accordion;

    public ChaptersListPage() {
        txtFont = Font.getDefault();
        txtColor = Color.BLACK;
        isLTR = true;
        setContent();
        setEditor();
    }

    @Override
    void setContent() {
        board = new StackPane();
        buttonContainer = new TilePane();

        btnShape = new Rectangle(100, 50);
        chapterList = new ArrayList<>();

        buttonContainer.setAlignment(Pos.CENTER);
        board.getChildren().add(buttonContainer);
    }

    @Override
    Parent getContent() {
        return board;
    }

    @Override
    void setReader() {
        if (nwButton != null) {
            buttonContainer.getChildren().remove(nwButton);
        }
        chapterList.forEach(page -> {
            page.parentBtn.setOnMouseClicked(event -> {
                page.setReaderTab();
                putNewPage(page);
            });
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
        Label btnOrientation = new Label("Button Orientation");
        Label bgColor = new Label("Background Color");
        Label bgImage = new Label("Background Image");

        editor.addColumn(0, textFont, textSize, textColor, btnColor,
                btnArcWidth, btnArcHeight, btnSpacing, btnOrientation, bgColor, bgImage);

        ObservableList<String> faces = FXCollections.observableArrayList(Font.getFontNames());
        ChoiceBox setfontFace = new ChoiceBox(faces);
        setfontFace.setValue(txtFont.getName());
        setfontFace.setOnAction(e -> {
            txtFont = new Font((String) setfontFace.getValue(), (double) txtFont.getSize());
            chapterList.forEach(page -> {
                page.parentBtn.setFont(txtFont);
            });
        });

        Spinner setfontSize = new Spinner(5.0, 200.0, txtFont.getSize(), 2.0);
        setfontSize.setOnMouseClicked(e -> {
            txtFont = new Font((String) setfontFace.getValue(), (double) setfontSize.getValue());
            chapterList.forEach(page -> {
                page.parentBtn.setFont(txtFont);
            });
        });

        ColorPicker pickTextColor = new ColorPicker();
        pickTextColor.setValue(Color.web(txtColor.toString(), 1.0));
        pickTextColor.setOnAction(e -> {
            txtColor = pickTextColor.getValue();
            chapterList.forEach(page -> {
                page.parentBtn.setTextFill(txtColor);
            });
        });

        ColorPicker pickBaseColor = new ColorPicker();
        if (!btnBaseColor.equals("/")) {
            pickBaseColor.setValue(Color.web("rgb(" + btnBaseColor + ")", 1.0));
        }
        pickBaseColor.setOnAction(e -> {
            btnBaseColor = (int) (pickBaseColor.getValue().getRed() * 255) + ",";
            btnBaseColor += (int) (pickBaseColor.getValue().getGreen() * 255) + ",";
            btnBaseColor += (int) (pickBaseColor.getValue().getBlue() * 255);
            chapterList.forEach(page -> {
                page.parentBtn.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            });
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
            buttonContainer.setHgap(new_val.doubleValue());
            space.setText(String.format("%.0f", new_val));
            spacing = new_val.intValue();
        });

        ToggleGroup tg = new ToggleGroup();
        RadioButton ltr = new RadioButton("Left to Right");
        ltr.setToggleGroup(tg);
        ltr.setOnAction(event -> {
            buttonContainer.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            isLTR =true;
        });

        RadioButton rtl = new RadioButton("right to Left");
        rtl.setToggleGroup(tg);
        rtl.setOnAction(event -> {
            buttonContainer.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            isLTR = false;
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
                new VBox(ltr, rtl), pickBgColor, loadImage);

        editor.setPadding(new Insets(10, 10, 10, 10));
        editor.setVgap(10);
        editor.setHgap(10);

        TitledPane t1 = new TitledPane("Page Editor", editor);
        TitledPane t2 = new TitledPane("Button Editor", new StackPane());
        accordion = new Accordion();
        accordion.getPanes().add(t1);
        accordion.getPanes().add(t2);

        chapterList.forEach(page -> {
            page.parentBtn.delete.setOnMouseClicked(event -> {
                chapterList.remove(page);
                buttonContainer.getChildren().remove(page.parentBtn);
                chaptersNo--;
                t2.setContent(null);
            });
            page.parentBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                long currentTime = 0, lastTime = 0;

                @Override
                public void handle(MouseEvent t) {

                    long diff = 0;

                    currentTime = System.currentTimeMillis();

                    if (lastTime != 0 && currentTime != 0) {
                        diff = currentTime - lastTime;

                        if (diff <= 215) {
                            page.setEditorTab();
                            classPosition++;
                            ArrayList<PageProperties> temp = new ArrayList<>(pageTransition.subList(0, classPosition));
                            pageTransition = temp;
                            pageTransition.add(page);
                            goBack.setDisable(false);
                            goForward.setDisable(true);
                        } else {
                            page.parentBtn.createContent(t2, true);
                            
                        }
                    } else {
                        page.parentBtn.createContent(t2, true);
                    }

                    lastTime = currentTime;
                }

            });
        });

        if (nwButton != null) {
            buttonContainer.getChildren().remove(nwButton);
        }
        nwButton = new Button("+");
        nwButton.setFont(Font.font(20));
        nwButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nwButton.setShape(btnShape);
        nwButton.setOnAction(eventnw -> {
            ChapterPage page = new ChapterPage();
            page.parentBtn.setFont(txtFont);
            page.parentBtn.setTextFill(txtColor);
            if (!btnBaseColor.equals("/")) {
                page.parentBtn.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            }
            page.parentBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            page.parentBtn.setShape(btnShape);
            chapterList.add(page);
            page.parentBtn.requestFocus();
            page.parentBtn.createContent(t2, true);
            page.parentBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                long currentTime = 0, lastTime = 0;

                @Override
                public void handle(MouseEvent t) {

                    long diff = 0;

                    currentTime = System.currentTimeMillis();

                    if (lastTime != 0 && currentTime != 0) {
                        diff = currentTime - lastTime;

                        if (diff <= 215) {
                            page.setEditorTab();
                            classPosition++;
                            ArrayList<PageProperties> temp = new ArrayList<>(pageTransition.subList(0, classPosition));
                            pageTransition = temp;
                            pageTransition.add(page);
                            goBack.setDisable(false);
                            goForward.setDisable(true);
                        } else {
                            page.parentBtn.createContent(t2, true);
                        }
                    } else {
                        page.parentBtn.createContent(t2, true);
                    }

                    lastTime = currentTime;
                }

            });
            page.parentBtn.delete.setOnMouseClicked(event -> {
                chapterList.remove(page);
                buttonContainer.getChildren().remove(page.parentBtn);
                chaptersNo--;
                t2.setContent(null);
            });
            buttonContainer.getChildren().remove(chaptersNo);
            buttonContainer.getChildren().add(page.parentBtn);
            buttonContainer.getChildren().add(nwButton);
            chaptersNo++;
        });
        buttonContainer.getChildren().add(nwButton);
    }

    @Override
    Parent getEditor() {
        return accordion;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeUTF(txtFont.getName());
        s.writeDouble(txtFont.getSize());
        s.writeUTF(txtColor.toString());
        s.writeUTF(btnBaseColor);
        s.writeInt(arcWidth);
        s.writeInt(arcHeight);
        s.writeInt(spacing);
        s.writeBoolean(isLTR);
        s.writeUTF(backColor);
        s.writeUTF(backImg);
        s.writeInt(chapterList.size());
        chapterList.forEach(page -> {
            try {
                s.writeObject(page);
            } catch (IOException ex) {
                Logger.getLogger(ChaptersListPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        String face = s.readUTF();
        double size = s.readDouble();
        txtFont = Font.font(face, size);
        txtColor = Paint.valueOf(s.readUTF());
        btnBaseColor = s.readUTF();
        arcWidth = s.readInt();
        arcHeight = s.readInt();
        spacing = s.readInt();
        isLTR = s.readBoolean();
        backColor = s.readUTF();
        backImg = s.readUTF();
        chaptersNo = s.readInt();
        setContent();
        for (int i = 0; i < chaptersNo; i++) {
            ChapterPage page = (ChapterPage) s.readObject();
            chapterList.add(page);
        }

        chapterList.forEach(page -> {
            page.parentBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            page.parentBtn.setShape(btnShape);
            buttonContainer.getChildren().add(page.parentBtn);
        });
        
        buttonContainer.setHgap(spacing);
        buttonContainer.setVgap(spacing);
        
        if (isLTR) {
            buttonContainer.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        } else {
            buttonContainer.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
        
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

        chapterList.forEach(page -> {
            page.parentBtn.setFont(txtFont);
            page.parentBtn.setTextFill(txtColor);
            if (!btnBaseColor.equals("/")) {
                page.parentBtn.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            }
        });

        setEditor();
    }

}
