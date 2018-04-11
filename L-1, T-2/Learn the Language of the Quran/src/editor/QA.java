package editor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class QA implements Serializable {

    SerializedButton question;
    ArrayList<String> answer;

    int pos;

    StringBuilder input = new StringBuilder();

    VBox content;
    Text txt;

    QA() {
        question = new SerializedButton();
        answer = new ArrayList<>();
        pos = 0;
    }

    void setAnswer(String s) {
        answer.add(s);
        input.append(s).append(",");
        txt.setText(input.toString());
    }

    boolean testAnswer(String s) {
        String temp = answer.get(pos);
        pos++;
        return s.equals(temp);
    }

    void setQuestion() {
        content = new VBox();
        question.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (!question.audio.equals("/")) {
            AudioClip alphAudio = new AudioClip(new File(Settings.PARENT_FILE_NAME + "/" + Settings.APP_FILE_NAME + "/" + question.audio).toURI().toString());
            question.setOnAction(event -> {
                alphAudio.play();
            });
            question.setFont(Font.font("Times new roman", 50));
            content.getChildren().add(question);
        } else if (!question.textImage.equals("/")) {
            question.setContentDisplay(ContentDisplay.TOP);
            content.getChildren().add(question);
        } else {
            question.setFont(Font.font("Times new roman", 50));
            content.getChildren().add(question);
        }
        txt = new Text();
        content.getChildren().add(txt);
        if (PageProperties.isEditable == true) {
            Button btn = new Button("Reset");
            btn.setOnAction(event -> {
                answer.clear();
                input.delete(0, input.length());
                txt.setText(input.toString());
            });
            content.getChildren().add(btn);
        }
        content.setAlignment(Pos.CENTER);
    }

    Node getQuestion() {
        question.fire();
        return content;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.writeObject(question);
        s.writeInt(answer.size());
        answer.forEach(element -> {
            try {
                s.writeUTF(element);
            } catch (IOException ex) {
                Logger.getLogger(QA.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        question = (SerializedButton) s.readObject();
        int n = s.readInt();
        answer = new ArrayList<>();
        input = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String temp = s.readUTF();
            answer.add(temp);
            input.append(temp).append(",");
        }
        txt = new Text(input.toString());
        pos = 0;
    }
}
