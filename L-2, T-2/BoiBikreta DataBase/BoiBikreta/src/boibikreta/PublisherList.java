/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boibikreta;

import DB.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author User
 */
public class PublisherList {
    private static PublisherList instance;
    private static ScrollPane view;
    private static VBox vbox;
    
    public static PublisherList getInstance()
    {
        if(instance == null)
        {
            instance = new PublisherList();
            instance.set();
        }
        return instance;
    }
    private void set()
    {
        vbox = new VBox();
        
        Publishers.getAllPublishers().forEach(publisher->{
            PublisherCard card = new PublisherCard(publisher);
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
