/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;

/**
 *
 * @author User
 */
public class BookPane extends BorderPane{
    String heading;
    ArrayList<Books> books;

    public BookPane(String heading, ArrayList<Books> books) {
        super();
        
        this.heading = heading;
        this.books = books;
        
        Label head = new Label(heading);
        head.setFont(new Font(15));
        this.setTop(head);
        
        FlowPane fpane = new FlowPane();
        books.forEach(book->{
            fpane.getChildren().add(new BookCard(book));
        });
        this.setCenter(fpane);
    }
}
