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
public class PublisherCard extends Button{
    Publishers publisher;
    
    public PublisherCard(Publishers publisher)
    {
        super(publisher.name);
        
        this.publisher = publisher;
        
        this.setWrapText(true);
                
        this.setOnAction(e->{
            setPublisherPage(publisher);
        });
    }
    
    public static void setPublisherPage(Publishers publisher)
    {
        VBox body = new VBox();
        body.setFillWidth(true);
        
        HBox top = new HBox();
        body.getChildren().add(top);
                
        VBox heading = new VBox();
        top.getChildren().add(heading);
        HBox.setHgrow(heading, Priority.ALWAYS);
        
        Label name = new Label(publisher.name);
        name.setFont(new Font(20));
        heading.getChildren().add(name);
        
        Label information = new Label(publisher.info);
        information.setWrapText(true);
        heading.getChildren().add(information);
        
        BookPane publishersBooks = new BookPane(publisher.name, Books.getPublishersAllBooks(publisher.id));
        body.getChildren().add(publishersBooks);
        
        View.root.setCenter(body);
    }
}
