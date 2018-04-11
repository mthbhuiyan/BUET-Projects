/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.BookGenres;
import DB.Books;
import DB.Genres;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author User
 */
public class BookList {
    private static BookList instance;
    private static ScrollPane view;
    private static VBox vbox;
    
    public static BookList getInstance()
    {
        if(instance == null)
        {
            instance = new BookList();
            instance.set();
        }
        return instance;
    }
    private void set()
    {
        vbox = new VBox();
        
        Genres.getAllGenres().forEach(genre->{
            vbox.getChildren().add(new BookPane(genre.name, BookGenres.getAllBooks(genre.id)));
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
