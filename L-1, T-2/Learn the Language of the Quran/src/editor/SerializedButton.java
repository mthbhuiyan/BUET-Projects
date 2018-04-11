package editor;

import java.awt.image.BufferedImage;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class SerializedButton extends Button implements Serializable {

    String buttonText;
    String textImage = "/";
    String audio = "/";
    double width = 50;
    double height = 50;

    Label widthLabel;
    Label heightLabel;
    TextField textField;
    ChoiceBox setfontFace;
    Spinner setfontSize;
    ColorPicker cpText;
    Spinner heightSp;
    Spinner widthSp;
    ColorPicker cpBackground;
    ImageView textImageView = null;

    Button delete = new Button("Delete");

    public SerializedButton() {
        super("//");
    }

    public SerializedButton(String text) {
        super(text);
    }

    public SerializedButton(double width, double height) {
        this.width = width;
        this.height = height;
        this.setPrefSize(width, height);
    }

    private void setButtonProperties() {
        delete = new Button("Delete");
        this.setText(buttonText);
        if (!textImage.equals("/")) {
            ImageView textImageView = new ImageView(new Image("file:" + Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + textImage));
            textImageView.setFitWidth(width);
            textImageView.setFitHeight(height);
            this.setGraphic(textImageView);
            this.setContentDisplay(ContentDisplay.TOP);
            this.setPrefSize(width, height);
        }
    }

    public void createContent(TitledPane t, boolean isAudible) {

        FileChooser imageChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.GIF");
        imageChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterGIF);

        FileChooser audioChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterMP3 = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        FileChooser.ExtensionFilter extFilterWAV = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
        audioChooser.getExtensionFilters().addAll(extFilterMP3, extFilterWAV);

        GridPane lines = new GridPane();
        lines.setPadding(new Insets(10, 10, 10, 10));
        lines.setHgap(10);
        lines.setVgap(10);

        Label textLabel = new Label("Text");
        lines.add(textLabel, 0, 0);
        Label wLabel = new Label("Width");
        lines.add(wLabel, 0, 1);
        Label hLabel = new Label("Height");
        lines.add(hLabel, 0, 2);
        Label imgLabel = new Label("Image");
        lines.add(imgLabel, 0, 3);
        Label audioLabel = new Label("Audio");
        if (isAudible) {
            lines.add(audioLabel, 0, 4);
        }

        lines.add(delete, 0, 5, 2, 1);

        textField = new TextField(this.getText());
        textField.setPromptText("Enter here");
        this.textProperty().bind(textField.textProperty());
        lines.add(textField, 1, 0);

        Slider wSlider = new Slider(10, 1000, width);
        widthLabel = new Label(Integer.toString((int) wSlider.getValue()));
        wSlider.prefWidthProperty().subtract(widthLabel.widthProperty());
        wSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            this.setPrefWidth(new_val.doubleValue());
            if (textImageView != null) {
                textImageView.setFitWidth(width - 5);
            }
            widthLabel.setText(String.format("%.0f", new_val));
            width = new_val.intValue();
        });
        lines.add(new HBox(wSlider, widthLabel), 1, 1);

        Slider hSlider = new Slider(10, 1000, height);
        heightLabel = new Label(Integer.toString((int) wSlider.getValue()));
        hSlider.prefWidthProperty().subtract(heightLabel.widthProperty());
        hSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            this.setPrefHeight(new_val.doubleValue());
            if (textImageView != null) {
                textImageView.setFitHeight(height);
            }
            heightLabel.setText(String.format("%.0f", new_val));
            height = new_val.intValue();
        });
        lines.add(new HBox(hSlider, heightLabel), 1, 2);

        Button loadImage = new Button("Browse");
        lines.add(loadImage, 1, 3);
        loadImage.setOnAction(e -> {
            File file = imageChooser.showOpenDialog(null);
            if (file != null) {
                File afile = new File(file.getPath());
                File bfile = new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/Image/" + file.getName());
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
                //try {

                //BufferedImage bufferedImage = ImageIO.read(file);
                textImage = "Image/" + file.getName();
                Image image = new Image("file:" + Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + textImage);
                textImageView = new ImageView(image);
                textImageView.setFitWidth(this.getWidth());
                textImageView.setFitHeight(this.getHeight());
                this.setGraphic(textImageView);
                this.setContentDisplay(ContentDisplay.TOP);
                //this.graphicProperty().bind(textImage.clipProperty());
                //textImage.layoutXProperty().bind(widthSp.editorProperty());
                //textImage.layoutYProperty().bind(heightSp.editorProperty());

//                } catch (IOException ex) {
//                    Logger.getLogger(SerializedButton.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        });
        Button playAudio = new Button();
        ImageView playBtnFace = new ImageView(new Image("file:Button Faces/Play.png"));
        playBtnFace.setFitWidth(30);
        playBtnFace.setFitHeight(30);
        playAudio.setGraphic(playBtnFace);
        playAudio.setStyle(
                "-fx-background-radius: 5em; "
                + "-fx-min-width: 30px; "
                + "-fx-min-height: 30px; "
                + "-fx-max-width: 30px; "
                + "-fx-max-height: 30px;"
        );
        playAudio.setOnAction(event -> {
            AudioClip alphAudio = new AudioClip(new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + audio).toURI().toString());
            alphAudio.play();
        });

        Button loadAudio = new Button("Browse");
        HBox audioBox = new HBox(loadAudio, playAudio);
        audioBox.setSpacing(10);
        if (isAudible) {
            lines.add(audioBox, 1, 4);
        }
        loadAudio.setOnAction(e -> {
            File file = audioChooser.showOpenDialog(null);
            if (file != null) {
                File afile = new File(file.getPath());
                File bfile = new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + "Audio/" + file.getName());
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

                audio = "Audio/" + file.getName();
                playAudio.setVisible(true);
                AudioClip alphAudio = new AudioClip(new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + audio).toURI().toString());
                alphAudio.play();
            }
        });
        if (audio.equals("/") || audio == null) {
            playAudio.setVisible(false);
        }

        t.setContent(lines);
        t.setExpanded(true);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeUTF(this.getText());
        s.writeUTF(textImage);
        s.writeUTF(audio);
        s.writeDouble(width);
        s.writeDouble(height);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        buttonText = s.readUTF();
        textImage = s.readUTF();
        audio = s.readUTF();
        width = s.readDouble();
        height = s.readDouble();
        setButtonProperties();
    }

}
