/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import java.io.ByteArrayInputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author User
 */
public class BookCard extends VBox{
    Books book;
    
    public BookCard(Books book)
    {
        super();
        
        this.book = book;
        
        this.setAlignment(Pos.CENTER);
        this.setWidth(200);
        this.setHeight(400);
        ImageView book_photo = new ImageView(new Image(new ByteArrayInputStream(book.photo)));
        book_photo.setFitHeight(150);
        book_photo.setFitWidth(150);
        book_photo.setPreserveRatio(true);
        Hyperlink photo = new Hyperlink("", book_photo);
        photo.setOnAction(e->{
            setBookPage();
        });
        this.getChildren().add(photo);
        
        String book_title = book.title;
        if(book.edition != null)
        {
            book_title.concat(" - "+book.edition);
        }
        Hyperlink title = new Hyperlink(book_title);
        title.setOnAction(e->{
            setBookPage();
        });
        this.getChildren().add(title);
        
        BookAuthors.getAllAuthors(book.id).forEach(auth->{
            Hyperlink author = new Hyperlink(auth.name);
            author.setOnAction(e->{
                AuthorCard.setAuthorPage(auth);
            });
            this.getChildren().add(author);
        });
                
        Label price = new Label("Price: "+book.price);
        this.getChildren().add(price);
        
        Label availability = new Label(book.availability);
        this.getChildren().add(availability);
        
        Label rating = new Label("Rating: "+book.rating);
        this.getChildren().add(rating);
        
        Button buy = new Button("Add to Cart");
        if(book.availability.equals("not available"))
            buy.setDisable(true);
        buy.setOnAction(e->{
            if(!Cart.getInstance().orders.contains(book))
                Cart.getInstance().addToCart(book);
        });
        this.getChildren().add(buy);
    }
    
    void setBookPage()
    {
        VBox body = new VBox();
        
        HBox top = new HBox();
        body.getChildren().add(top);
        
        ImageView book_photo = new ImageView(new Image(new ByteArrayInputStream(book.photo)));
        book_photo.setFitHeight(300);
        book_photo.setFitWidth(300);
        book_photo.setPreserveRatio(true);
        Label photo = new Label("", book_photo);
        top.getChildren().add(photo);
        
        VBox heading = new VBox();
        top.getChildren().add(heading);
        
        String book_title = book.title;
        if(book.edition != null)
        {
            book_title.concat(" - "+book.edition);
        }
        Label title = new Label(book_title);
        title.setFont(new Font(25));
        heading.getChildren().add(title);
        
        BookAuthors.getAllAuthors(book.id).forEach(auth->{
            Hyperlink author = new Hyperlink(auth.name);
            heading.getChildren().add(author);
        });
                
        Label price = new Label("Price: "+book.price);
        heading.getChildren().add(price);
        
        Label availability = new Label(book.availability);
        heading.getChildren().add(availability);
        
        Label rating = new Label("Rating: "+book.rating);
        heading.getChildren().add(rating);
        
        Button buy = new Button("Add to Cart");        
        if(book.availability.equals("not available"))
            buy.setDisable(true);
        buy.setOnAction(e->{
            if(!Cart.getInstance().orders.contains(book))
                Cart.getInstance().addToCart(book);
        });
        heading.getChildren().add(buy);
                
        Label review = new Label("Reviews");
        review.setFont(new Font(20));
        body.getChildren().add(review);
        
        HBox review_getter = new HBox();
        body.getChildren().add(review_getter);
        
        ImageView imgView = new ImageView();
        if(User.getInstance().getPhoto()!=null)
        {
            imgView.setImage(new Image(new ByteArrayInputStream(User.getInstance().getPhoto())));
            imgView.setFitWidth(25);
            imgView.setFitHeight(25);
        }
        review_getter.getChildren().add(imgView);
        
        TextArea review_message = new TextArea();
        review_message.setPromptText("Your Review Here");
        review_getter.getChildren().add(review_message);
        
        Button post = new Button("Post");
        if(!User.getInstance().isLoggedIn())
            post.setDisable(true);
        review_getter.getChildren().add(post);
        
        Label others_reviews = new Label("Others Reviews");
        body.getChildren().add(others_reviews);
        
        VBox reviewList = new VBox();
        body.getChildren().add(reviewList);
        
        BookReviews.getAllReviews(book.id).forEach(book_review->{
            reviewList.getChildren().add(new ReviewCard(book_review));
        });
        
        
        post.setOnAction(e->{
            if(!review_message.getText().equals(""))
            {
                reviewList.getChildren().add(new ReviewCard(User.getInstance().getFirst_name()+" "+User.getInstance().getLast_name(), User.getInstance().getPhoto(), review_message.getText()));
                review_message.setText("");
            }
        });
        
        View.root.setCenter(new ScrollPane(body));
    }
}
