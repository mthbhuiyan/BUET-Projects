/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import java.io.ByteArrayInputStream;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author User
 */
public class ReviewCard extends VBox{
    public ReviewCard(BookReviews book_review) 
    {
        super();

        ImageView img = new ImageView();
        img.setFitWidth(25);
        img.setFitHeight(25);
        img.setPreserveRatio(true);
        if (book_review.reviewer_photo != null) {
            img.setImage(new Image(new ByteArrayInputStream(book_review.reviewer_photo)));
        }
        this.getChildren().add(img);
        
        Label reviewer = new Label(book_review.reviewer_title);
        reviewer.setFont(new Font(8));        
        this.getChildren().add(reviewer);


        Label message = new Label(book_review.review_message);
        message.setWrapText(true);
        this.getChildren().add(message);
    }
    
    public ReviewCard(String reviewer_title, byte[] reviewer_photo, String review_message) 
    {
        super();

        ImageView img = new ImageView();
        img.setFitWidth(25);
        img.setFitHeight(25);
        img.setPreserveRatio(true);
        if (reviewer_photo != null) {
            img.setImage(new Image(new ByteArrayInputStream(reviewer_photo)));
        }
        this.getChildren().add(img);
        
        Label reviewer = new Label(reviewer_title);
        reviewer.setFont(new Font(8));        
        this.getChildren().add(reviewer);


        Label message = new Label(review_message);
        message.setWrapText(true);
        this.getChildren().add(message);
    }
}
