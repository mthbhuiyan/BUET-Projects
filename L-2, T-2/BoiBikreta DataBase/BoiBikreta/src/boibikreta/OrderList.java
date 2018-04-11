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
public class OrderList extends VBox {
    private static OrderList instance;
    private static ScrollPane view;
    private static VBox vbox;
    
    public static OrderList getInstance()
    {
        if(instance == null)
        {
            instance = new OrderList();
            instance.set();
        }
        return instance;
    }
    private void set()
    {
        vbox = new VBox();
        
        Orders.getCustomersAllOrders(User.getInstance().getPerson_id()).forEach(order->{
            vbox.getChildren().add(new OrderCard(order));
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
