/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.Authors;
import DB.BookGenres;
import DB.Genres;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author User
 */
public class AuthorList {
    private static AuthorList instance;
    private static ScrollPane view;
    private static VBox vbox;
    
    public static AuthorList getInstance()
    {
        if(instance == null)
        {
            instance = new AuthorList();
            instance.set();
        }
        return instance;
    }
    private void set()
    {
        vbox = new VBox();
        
        Authors.getAllAuthors().forEach(author->{
            AuthorCard card = new AuthorCard(author);
            card.setPrefWidth(200);
            card.setPrefHeight(50);
            vbox.getChildren().add(card);
        });
        
        view = new ScrollPane();
        view.setContent(vbox);
    }
    public ScrollPane getView()
    {
        view.setContent(vbox);
        return view;
    }
}
