/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import java.io.ByteArrayInputStream;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author User
 */
public class AuthorCard extends Button{
    Authors author;
    
    public AuthorCard(Authors author)
    {
        super(author.name);
        
        this.author = author;
        
        this.setWrapText(true);
        
        ImageView photo = new ImageView();
        photo.setFitHeight(50);
        photo.setFitWidth(50);
        photo.setPreserveRatio(true);
        if(author.photo != null)
            photo.setImage(new Image(new ByteArrayInputStream(author.photo)));
        this.setGraphic(photo);
        
        this.setOnAction(e->{
            setAuthorPage(author);
        });
    }
    
    public static void setAuthorPage(Authors author)
    {
        VBox body = new VBox();
        body.setFillWidth(true);
        
        HBox top = new HBox();
        body.getChildren().add(top);
        
        ImageView photo = new ImageView();
        photo.setFitHeight(150);
        photo.setFitWidth(150);
        photo.setPreserveRatio(true);
        if(author.photo != null)
            photo.setImage(new Image(new ByteArrayInputStream(author.photo)));
        top.getChildren().add(photo);
        
        VBox heading = new VBox();
        top.getChildren().add(heading);
        HBox.setHgrow(heading, Priority.ALWAYS);
        
        Label name = new Label(author.name);
        name.setFont(new Font(20));
        heading.getChildren().add(name);
        
        Label information = new Label(author.information);
        information.setWrapText(true);
        heading.getChildren().add(information);
        
        BookPane authorsBooks = new BookPane(author.name, BookAuthors.getAllBooks(author.id));
        body.getChildren().add(authorsBooks);
        
        View.root.setCenter(body);
    }
}
