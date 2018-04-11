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
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class QuizPage extends PageProperties implements Serializable {

    SerializedButton parentBtn;
    BorderPane board;
    TilePane consoleLoader;
    TilePane questionLoader;
    ArrayList<SerializedButton> console;
    LinkedList<QA> quesAns;

    boolean hasAnswers;
    int consoleNo = 0;
    int questionNo = 0;
    int point;

    boolean isLTR;

    Label space;
    Label arcW;
    Label arcH;
    Label pointLabel;

    Button nwConsole;
    Button nwQuestion;
    QA questionIn;

    StackPane quizWindow;
    private Pagination pagination;

    Accordion accordion;

    public QuizPage() {
        parentBtn = new SerializedButton();
        hasAnswers = false;
        isLTR = true;
        setContent();
        setEditor();
    }

    public void setQuiz() {
        pointLabel = new Label(String.valueOf(point));
        pointLabel.setPadding(new Insets(10, 10, 10, 10));
        pointLabel.setFont(Font.font("Times new roman", FontWeight.EXTRA_BOLD, 20));
        pointLabel.setTextAlignment(TextAlignment.CENTER);
        pointLabel.setTextFill(txtColor);
        pointLabel.setStyle("-fx-background-color: rgb(" + btnBaseColor + ");");
        board.setRight(pointLabel);
        LinkedList<QA> random = new LinkedList<>(quesAns);
        for (int i = 0; i < questionNo; i++) {
            int r = (int) Math.round(Math.random() * (questionNo - i - 1));
            QA temp = random.remove(r);
            random.add(temp);
        }
        setPagination(random);
        board.setCenter(quizWindow);
    }

    public void setAnswerGetter() {
        setPagination(quesAns);
        board.setCenter(quizWindow);
    }

    public void setPagination(LinkedList<QA> list) {
        list.forEach(element -> {
            element.setQuestion();
        });

        pagination = new Pagination(questionNo);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory((Integer pageIndex) -> {
            questionIn = list.get(pageIndex);
            questionIn.pos = 0;
            return questionIn.getQuestion();
        });
        quizWindow = new StackPane(pagination);
    }

    public void endQuiz() {
        Stage stage = new Stage(StageStyle.TRANSPARENT);

        ImageView replayImg = new ImageView(new Image("file:Button Faces/Replay.png"));
        replayImg.setFitWidth(40);
        replayImg.setFitHeight(40);
        Button replay = new Button();
        replay.setGraphic(replayImg);
        replay.setOnAction(event -> {
            setQuiz();
            stage.close();
        });

        ImageView backImg = new ImageView(new Image("file:Button Faces/Back1.jpg"));
        backImg.setFitWidth(40);
        backImg.setFitHeight(40);
        Button back = new Button();
        back.setGraphic(backImg);
        back.setOnAction(event -> {
            board.setRight(null);
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
            stage.close();
        });

        HBox choice = new HBox(replay, back);
        choice.setSpacing(10);
        choice.setPadding(new Insets(10, 10, 10, 10));

        VBox quiz = new VBox(pointLabel, choice);
        quiz.setAlignment(Pos.CENTER);
        quiz.setPadding(new Insets(10, 10, 10, 10));
        quiz.setStyle("-fx-background-color: rgba(" + btnBaseColor + ",0.5);");

        Scene scene = new Scene(quiz);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @Override
    void setContent() {
        board = new BorderPane();
        consoleLoader = new TilePane();
        questionLoader = new TilePane();
        console = new ArrayList<>();
        quesAns = new LinkedList<>();

        consoleLoader.setAlignment(Pos.CENTER);
        consoleLoader.setPrefWidth(500);
        board.setLeft(consoleLoader);

        questionLoader.setAlignment(Pos.CENTER);
        board.setCenter(questionLoader);

        board.setRight(null);
    }

    @Override
    Parent getContent() {
        Button topButton = new Button(parentBtn.getText(), parentBtn.getGraphic());
        topButton.setFont(txtFont);
        topButton.setTextFill(txtColor);
        if (!btnBaseColor.equals("/")) {
            topButton.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
        }
        VBox tB = new VBox(topButton);
        tB.setAlignment(Pos.CENTER);
        board.setTop(tB);
        return board;
    }

    @Override
    void setReader() {
        if (nwConsole != null) {
            consoleLoader.getChildren().remove(nwConsole);
        }
        if (nwQuestion != null) {
            questionLoader.getChildren().remove(nwQuestion);
        }
        console.forEach(element -> {
            element.setOnMouseClicked(e -> {
                if (questionIn.testAnswer(element.buttonText)) {
                    point++;
                    pointLabel.setText(Integer.toString(point));
                    if (questionIn.answer.size() == questionIn.pos) {
                        int n = pagination.getCurrentPageIndex();
                        if (n < questionNo - 1) {
                            pagination.setCurrentPageIndex(n + 1);
                        } else {
                            endQuiz();
                        }
                    }
                } else {
                    questionIn.question.fire();
                    questionIn.pos = 0;
                    point--;
                }

            });
        });
        point = 0;
        setQuiz();
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
            console.forEach(el -> {
                el.setFont(txtFont);
            });
        });

        Spinner setfontSize = new Spinner(5.0, 200.0, txtFont.getSize(), 2.0);
        setfontSize.setOnMouseClicked(e -> {
            txtFont = new Font((String) setfontFace.getValue(), (double) setfontSize.getValue());
            console.forEach(item -> {
                item.setFont(txtFont);
            });
        });

        ColorPicker pickTextColor = new ColorPicker();
        pickTextColor.setValue(Color.web(txtColor.toString(), 1.0));
        pickTextColor.setOnAction(e -> {
            txtColor = pickTextColor.getValue();
            console.forEach(item -> {
                item.setTextFill(txtColor);
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
            console.forEach(item -> {
                item.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
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
            consoleLoader.setVgap(new_val.doubleValue());
            consoleLoader.setHgap(new_val.doubleValue());
            space.setText(String.format("%.0f", new_val));
            spacing = new_val.intValue();
        });

        ToggleGroup tg = new ToggleGroup();
        RadioButton ltr = new RadioButton("Left to Right");
        ltr.setToggleGroup(tg);
        ltr.setOnAction(event -> {
            consoleLoader.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            questionLoader.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            isLTR = true;
        });

        RadioButton rtl = new RadioButton("right to Left");
        rtl.setToggleGroup(tg);
        rtl.setOnAction(event -> {
            consoleLoader.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            questionLoader.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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
        TitledPane t3 = new TitledPane("Question Editor", new StackPane());
        accordion = new Accordion();
        accordion.getPanes().add(t1);
        accordion.getPanes().add(t2);
        accordion.getPanes().add(t3);

        if (nwConsole != null) {
            consoleLoader.getChildren().remove(nwConsole);
        }
        nwConsole = new Button("     +\ncontroller");
        nwConsole.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nwConsole.setShape(btnShape);
        nwConsole.setOnAction(eventnw -> {
            consoleLoader.getChildren().remove(nwConsole);
            SerializedButton element = new SerializedButton();
            element.setFont(txtFont);
            element.setTextFill(txtColor);
            if (!btnBaseColor.equals("/")) {
                element.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            }
            element.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            element.setShape(btnShape);
            console.add(element);
            element.requestFocus();
            element.createContent(t2, false);
            element.setOnMouseClicked(eventb -> {
                element.createContent(t2, false);
            });
            element.delete.setOnMouseClicked(event -> {
                console.remove(element);
                consoleLoader.getChildren().remove(element);
                consoleNo--;
                t2.setContent(null);
            });
            consoleLoader.getChildren().add(element);
            consoleLoader.getChildren().add(nwConsole);
            consoleNo++;
        });

        console.forEach(element -> {
            element.setOnMouseClicked(e -> {
                element.createContent(t2, false);
            });
            element.delete.setOnMouseClicked(event -> {
                console.remove(element);
                consoleLoader.getChildren().remove(element);
                consoleNo--;
                t2.setContent(null);
            });
        });

        consoleLoader.getChildren().add(nwConsole);

        if (nwQuestion != null) {
            questionLoader.getChildren().remove(nwQuestion);
        }
        nwQuestion = new Button("+ Question");
        nwQuestion.setFont(Font.font(20));
        nwQuestion.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nwQuestion.setOnAction(eventnw -> {
            hasAnswers = false;
            questionLoader.getChildren().remove(nwQuestion);
            QA element = new QA();
            element.question.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            quesAns.add(element);
            element.question.setFont(Font.font(20));
            element.question.requestFocus();
            element.question.createContent(t3, true);
            element.question.setOnMouseClicked(eventb -> {
                element.question.createContent(t3, true);
            });
            element.question.delete.setOnMouseClicked(event -> {
                quesAns.remove(element);
                questionLoader.getChildren().remove(element.question);
                questionNo--;
                t3.setContent(null);
            });
            questionLoader.getChildren().add(element.question);
            questionLoader.getChildren().add(nwQuestion);
            questionNo++;
        });

        questionLoader.getChildren().add(nwQuestion);

        quesAns.forEach(element -> {
            element.question.setOnMouseClicked(e -> {
                element.question.createContent(t3, true);
            });
            element.question.delete.setOnMouseClicked(event -> {
                quesAns.remove(element);
                questionLoader.getChildren().remove(element.question);
                questionNo--;
                t3.setContent(null);
            });
        });
        board.setCenter(questionLoader);

    }

    @Override
    public void setOkButtonAction() {

        PageProperties.done.setOnAction(event -> {
            if (nwConsole != null) {
                consoleLoader.getChildren().remove(nwConsole);
                nwConsole = null;
                if (nwQuestion != null) {
                    questionLoader.getChildren().remove(nwQuestion);
                }
                for (int i = 0; i < consoleNo; i++) {
                    if (console.get(i).getText() == null || console.get(i).getText().equals("//")) {
                        console.get(i).buttonText = String.valueOf(i);
                        console.get(i).textField.setText(String.valueOf(i));
                        console.get(i).setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    } else {
                        console.get(i).buttonText = console.get(i).getText();
                    }
                }
                console.forEach(element -> {
                    element.setOnMouseClicked(e -> {
                        questionIn.setAnswer(element.buttonText);
                    });
                });
                goBack.setDisable(true);
                setAnswerGetter();
            } else if (hasAnswers) {
                goBack.setDisable(false);
                setEditable(false);
            } else {
                for (int i = 0; i < questionNo; i++) {
                    if (quesAns.get(i).answer.isEmpty()) {
                        hasAnswers = false;
                        pagination.setCurrentPageIndex(i);
                        break;
                    } else {
                        hasAnswers = true;
                    }
                }
                if (hasAnswers) {
                    goBack.setDisable(false);
                    setEditable(false);
                }

            }
        });
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
        s.writeObject(parentBtn);
        s.writeInt(console.size());
        console.forEach(item -> {
            try {
                s.writeObject(item);
            } catch (IOException ex) {
                Logger.getLogger(ChaptersListPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        s.writeInt(quesAns.size());
        quesAns.forEach(ques -> {
            try {
                s.writeObject(ques);
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
        parentBtn = (SerializedButton) s.readObject();
        consoleNo = s.readInt();
        setContent();
        for (int i = 0; i < consoleNo; i++) {
            SerializedButton element = (SerializedButton) s.readObject();
            console.add(element);
        }

        questionNo = s.readInt();
        for (int i = 0; i < questionNo; i++) {
            QA ques = (QA) s.readObject();
            quesAns.add(ques);
        }

        console.forEach(item -> {
            item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            item.setShape(btnShape);
        });
        console.forEach(e -> {
            consoleLoader.getChildren().add(e);
        });

        quesAns.forEach(ques -> {
            ques.question.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            questionLoader.getChildren().add(ques.question);
        });

        consoleLoader.setHgap(spacing);
        consoleLoader.setVgap(spacing);

        if (isLTR) {
            consoleLoader.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            questionLoader.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        } else {
            consoleLoader.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            questionLoader.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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

        console.forEach(item -> {
            item.setFont(txtFont);
            item.setTextFill(txtColor);
            if (!btnBaseColor.equals("/")) {
                item.setStyle("-fx-base: rgb(" + btnBaseColor + ");");
            }
        });

        setEditor();
    }

}
