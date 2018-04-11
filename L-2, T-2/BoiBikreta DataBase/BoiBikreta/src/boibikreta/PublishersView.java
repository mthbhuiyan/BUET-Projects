/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.Publishers;
import static boibikreta.BooksView.save;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author User
 */
public class PublishersView {
    static BorderPane view;
    static VBox publishersTable;
    static VBox cardList;
    static GridPane topLabel;
    static ArrayList<PublishersView> publishers;
    String name;
    String info;

    public PublishersView(String name, String info) {
        this.name = name;
        this.info = info;
    }
    static BorderPane GetView(boolean isEmployee)
    {
        view = new BorderPane();
        
        ToggleGroup tgp = new ToggleGroup();
        
        ToggleButton viewbtn = new ToggleButton("View");
        viewbtn.setToggleGroup(tgp);
        viewbtn.setOnAction(e->{
            if(tgp.getSelectedToggle()!=viewbtn)
                viewbtn.setSelected(true);
            else
            {
                view.setCenter(GetTable());
                publishers = new Publishers().getAll();
                publishers.forEach(pub->{
                    AddCard(pub);
                });
            }
        });
        
        ToggleButton insertbtn = new ToggleButton("Insert");
        insertbtn.setToggleGroup(tgp);
        insertbtn.setOnAction(e->{
            if(tgp.getSelectedToggle()!=insertbtn)
                insertbtn.setSelected(true);
            else
            {
                view.setCenter(GetTable());
                AddInsertField();
                publishers = new ArrayList<PublishersView>();
            }
        });
        
        view.setTop(new HBox(viewbtn, insertbtn));
        
        view.setCenter(GetTable());
        publishers = new Publishers().getAll();
        publishers.forEach(pub -> {
            AddCard(pub);
        });
        return view;
    }
    static void AddCard(PublishersView publisher)
    {
        GridPane card = new GridPane();
        card.add(new Label(publisher.name), 0, 0);
        card.add(new Label(publisher.info), 1, 0);
        card.setGridLinesVisible(true);
        cardList.getChildren().add(card);
    }
    static VBox GetTable()
    {
        publishers = new ArrayList<PublishersView>();
        
        topLabel = new GridPane();
        Label photolabel = new Label("Name");
        topLabel.add(photolabel, 0, 0);
        
        Label isbnlabel = new Label("Information");
        topLabel.add(isbnlabel, 1, 0);
        
        topLabel.setGridLinesVisible(true);
        
        cardList = new VBox();
        
        publishersTable = new VBox(topLabel, new ScrollPane(cardList));
        publishersTable.setAlignment(Pos.CENTER);
        return publishersTable;
    }
    static void AddInsertField()
    {
        TextField name = new TextField();
        TextArea info = new TextArea();
        
        Button done = new Button("Done");
        done.setOnAction(e->{
            if (name.getText().equals("")) {
                name.requestFocus();
            } else if (info.getText().equals("")) {
                info.requestFocus();
            } else {
                PublishersView publisher = new PublishersView(name.getText(), info.getText());

                AddCard(publisher);
                publishers.add(publisher);

                name.setText("");
                info.setText("");
            }
        });
        
        Button clear = new Button("Clear");
        clear.setOnAction(e->{
            name.setText("");
            info.setText("");
        });
        
        topLabel.add(name, 0, 1);
        topLabel.add(info, 1, 1);
        topLabel.add(done, 2, 1);
        topLabel.add(clear, 3, 1);
        
        Button save = new Button("Save");
        save.setOnAction(e->{
            save();
            cancel();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e->{
            cancel();
        });
        
        publishersTable.getChildren().add(new HBox(save, cancel));
    }
    static void save() {
        if (publishers.size() > 0) {
            publishers.forEach(pub -> {
                new Publishers().insert(pub.name, pub.info);
            });
        }
    }
    static void cancel()
    {
        cardList.getChildren().removeAll(cardList.getChildren());
        publishers.clear();
    }
}
